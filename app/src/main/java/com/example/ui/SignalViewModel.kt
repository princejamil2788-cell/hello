package com.example.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DemoSignalsProvider
import com.example.data.SignalAnalyzerRepository
import com.example.model.AnalysisStatus
import com.example.model.ImpactFilter
import com.example.model.ImpactLevel
import com.example.model.SignalBatch
import com.example.model.TerminalTab
import com.example.model.formatBdTime
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class SignalTerminalUiState(
    val bdClock: String = formatBdTime(),
    val selectedTab: TerminalTab = TerminalTab.SIGNALS,
    val selectedBitmap: Bitmap? = null,
    val selectedFileName: String? = null,
    val analysisStatus: AnalysisStatus = AnalysisStatus.Idle,
    val activeBatch: SignalBatch = DemoSignalsProvider.demoBatch,
    val impactFilter: ImpactFilter = ImpactFilter.ALL,
    val showExtractedText: Boolean = false
) {
    val highCount: Int
        get() = activeBatch.signals.count { it.impact == ImpactLevel.HIGH }

    val mediumCount: Int
        get() = activeBatch.signals.count { it.impact == ImpactLevel.MEDIUM }

    val totalCount: Int
        get() = activeBatch.signals.size

    val filteredSignals
        get() = when (impactFilter) {
            ImpactFilter.ALL -> activeBatch.signals
            ImpactFilter.HIGH -> activeBatch.signals.filter { it.impact == ImpactLevel.HIGH }
            ImpactFilter.MEDIUM -> activeBatch.signals.filter { it.impact == ImpactLevel.MEDIUM }
        }
}

class SignalViewModel(
    private val repository: SignalAnalyzerRepository = SignalAnalyzerRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignalTerminalUiState())
    val uiState: StateFlow<SignalTerminalUiState> = _uiState.asStateFlow()

    init {
        startBdClockTicker()
    }

    private fun startBdClockTicker() {
        viewModelScope.launch {
            while (isActive) {
                _uiState.update { it.copy(bdClock = formatBdTime()) }
                delay(1_000L)
            }
        }
    }

    fun selectTab(tab: TerminalTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun setImpactFilter(filter: ImpactFilter) {
        _uiState.update { it.copy(impactFilter = filter) }
    }

    fun toggleExtractedText() {
        _uiState.update { it.copy(showExtractedText = !it.showExtractedText) }
    }

    fun clearSelectedImage() {
        _uiState.update {
            it.copy(
                selectedBitmap = null,
                selectedFileName = null,
                analysisStatus = AnalysisStatus.Idle
            )
        }
    }

    fun loadDemoPreset() {
        _uiState.update {
            it.copy(
                activeBatch = DemoSignalsProvider.demoBatch,
                impactFilter = ImpactFilter.ALL,
                analysisStatus = AnalysisStatus.Idle
            )
        }
    }

    fun onScreenshotPicked(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val (bitmap, fileName) = withContext(Dispatchers.IO) {
                    val decoded = decodeScaledBitmap(context, uri, maxDimension = 1600)
                    val resolvedName = resolveFileName(context, uri) ?: "calendar_screenshot.png"
                    decoded to resolvedName
                }
                _uiState.update {
                    it.copy(
                        selectedBitmap = bitmap,
                        selectedFileName = fileName,
                        analysisStatus = AnalysisStatus.Idle
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        analysisStatus = AnalysisStatus.Error(
                            e.localizedMessage ?: "স্ক্রিনশট ছবি লোড করা যায়নি।"
                        )
                    )
                }
            }
        }
    }

    fun analyzeSelectedScreenshot() {
        val currentState = _uiState.value
        val bitmap = currentState.selectedBitmap
        if (bitmap == null) {
            _uiState.update {
                it.copy(
                    analysisStatus = AnalysisStatus.Error(
                        "প্রথমে একটি স্ক্রিনশট নির্বাচন করুন অথবা 'ডেমো ৫টি সিগন্যাল' বোতামে চাপ দিন।"
                    )
                )
            }
            return
        }

        val label = currentState.selectedFileName ?: "Screenshot"
        viewModelScope.launch {
            _uiState.update { it.copy(analysisStatus = AnalysisStatus.Loading) }
            try {
                val batch = repository.analyzeScreenshot(bitmap, label)
                _uiState.update {
                    it.copy(
                        activeBatch = batch,
                        impactFilter = ImpactFilter.ALL,
                        analysisStatus = AnalysisStatus.Success
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        analysisStatus = AnalysisStatus.Error(
                            e.localizedMessage ?: "স্ক্রিনশট বিশ্লেষণে ত্রুটি হয়েছে।"
                        )
                    )
                }
            }
        }
    }

    private fun decodeScaledBitmap(context: Context, uri: Uri, maxDimension: Int): Bitmap {
        val rawBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                val width = info.size.width
                val height = info.size.height
                val largest = maxOf(width, height)
                if (largest > maxDimension) {
                    val scale = maxDimension.toFloat() / largest.toFloat()
                    decoder.setTargetSize(
                        (width * scale).toInt().coerceAtLeast(1),
                        (height * scale).toInt().coerceAtLeast(1)
                    )
                }
            }
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }

        val largest = maxOf(rawBitmap.width, rawBitmap.height)
        if (largest <= maxDimension) {
            return rawBitmap
        }
        val scale = maxDimension.toFloat() / largest.toFloat()
        return Bitmap.createScaledBitmap(
            rawBitmap,
            (rawBitmap.width * scale).toInt().coerceAtLeast(1),
            (rawBitmap.height * scale).toInt().coerceAtLeast(1),
            true
        )
    }

    private fun resolveFileName(context: Context, uri: Uri): String? {
        return try {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex >= 0 && cursor.moveToFirst()) {
                    cursor.getString(nameIndex)
                } else {
                    null
                }
            }
        } catch (_: Exception) {
            null
        }
    }
}
