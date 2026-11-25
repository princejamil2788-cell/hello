package com.example.ui

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.ShowChart
import androidx.compose.material.icons.rounded.UploadFile
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.model.AnalysisStatus
import com.example.model.ImpactFilter
import com.example.model.TerminalTab
import com.example.ui.components.DisclaimerFooterCard
import com.example.ui.components.GuideSection
import com.example.ui.components.SignalCard
import com.example.ui.theme.AmberMedium
import com.example.ui.theme.AmberMediumBg
import com.example.ui.theme.AmberMediumLight
import com.example.ui.theme.EmeraldBuy
import com.example.ui.theme.EmeraldBuyBg
import com.example.ui.theme.EmeraldBuyLight
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.RedImpact
import com.example.ui.theme.RedImpactLight
import com.example.ui.theme.RoseSell
import com.example.ui.theme.RoseSellBg
import com.example.ui.theme.RoseSellLight
import com.example.ui.theme.SkyAccent
import com.example.ui.theme.SkyAccentBg
import com.example.ui.theme.TerminalBorder
import com.example.ui.theme.TerminalObsidian
import com.example.ui.theme.TerminalSurface
import com.example.ui.theme.TerminalSurfaceElevated
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SignalTerminalScreen(
    viewModel: SignalViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.onScreenshotPicked(context, uri)
        }
    }

    val openPhotoPicker: () -> Unit = {
        photoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    BackHandler(enabled = uiState.selectedTab != TerminalTab.SIGNALS) {
        viewModel.selectTab(TerminalTab.SIGNALS)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(TerminalObsidian)
            .drawBehind {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            EmeraldBuy.copy(alpha = 0.14f),
                            SkyAccent.copy(alpha = 0.05f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.2f, 0f),
                        radius = size.maxDimension * 0.65f
                    )
                )
            }
    ) {
        val isCompact = maxWidth < 600.dp
        val isExpanded = maxWidth >= 840.dp

        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                if (isCompact) {
                    TerminalBottomBar(
                        selectedTab = uiState.selectedTab,
                        onSelectTab = viewModel::selectTab
                    )
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (!isCompact && !isExpanded) {
                    TerminalNavigationRail(
                        selectedTab = uiState.selectedTab,
                        onSelectTab = viewModel::selectTab
                    )
                }

                if (isExpanded) {
                    // Expanded two-pane layout: Signals feed on left, Guide & Disclaimer supporting pane on right
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        SignalsFeedPane(
                            uiState = uiState,
                            onPickScreenshot = openPhotoPicker,
                            onClearScreenshot = viewModel::clearSelectedImage,
                            onAnalyzeClick = viewModel::analyzeSelectedScreenshot,
                            onLoadDemoClick = viewModel::loadDemoPreset,
                            onSelectImpactFilter = viewModel::setImpactFilter,
                            onToggleExtractedText = viewModel::toggleExtractedText,
                            modifier = Modifier
                                .weight(1.45f)
                                .fillMaxHeight()
                        )

                        Column(
                            modifier = Modifier
                                .weight(0.95f)
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState())
                                .padding(top = 20.dp, bottom = 28.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            GuideSection()
                            DisclaimerFooterCard()
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        when (uiState.selectedTab) {
                            TerminalTab.SIGNALS -> {
                                SignalsFeedPane(
                                    uiState = uiState,
                                    onPickScreenshot = openPhotoPicker,
                                    onClearScreenshot = viewModel::clearSelectedImage,
                                    onAnalyzeClick = viewModel::analyzeSelectedScreenshot,
                                    onLoadDemoClick = viewModel::loadDemoPreset,
                                    onSelectImpactFilter = viewModel::setImpactFilter,
                                    onToggleExtractedText = viewModel::toggleExtractedText,
                                    modifier = Modifier
                                        .widthIn(max = 680.dp)
                                        .fillMaxSize()
                                )
                            }

                            TerminalTab.GUIDE -> {
                                Column(
                                    modifier = Modifier
                                        .widthIn(max = 680.dp)
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                        .padding(top = 20.dp, bottom = 24.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    TerminalHeaderCard(bdClock = uiState.bdClock)
                                    GuideSection()
                                    DisclaimerFooterCard()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SignalsFeedPane(
    uiState: SignalTerminalUiState,
    onPickScreenshot: () -> Unit,
    onClearScreenshot: () -> Unit,
    onAnalyzeClick: () -> Unit,
    onLoadDemoClick: () -> Unit,
    onSelectImpactFilter: (ImpactFilter) -> Unit,
    onToggleExtractedText: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = 20.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = "header") {
            TerminalHeaderCard(bdClock = uiState.bdClock)
        }

        item(key = "upload_panel") {
            ScreenshotUploadCard(
                selectedBitmap = uiState.selectedBitmap,
                selectedFileName = uiState.selectedFileName,
                analysisStatus = uiState.analysisStatus,
                onPickScreenshot = onPickScreenshot,
                onClearScreenshot = onClearScreenshot,
                onAnalyzeClick = onAnalyzeClick,
                onLoadDemoClick = onLoadDemoClick
            )
        }

        item(key = "batch_banner") {
            ActiveBatchAndFilterCard(
                uiState = uiState,
                onSelectImpactFilter = onSelectImpactFilter
            )
        }

        if (uiState.filteredSignals.isEmpty()) {
            item(key = "empty_filter") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = TerminalSurface),
                    border = BorderStroke(1.dp, TerminalBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "এই ইমপ্যাক্ট ক্যাটাগরিতে বর্তমানে কোনো সিগন্যাল নেই।",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { onSelectImpactFilter(ImpactFilter.ALL) },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("reset_filter_button")
                        ) {
                            Text("সব সিগন্যাল দেখুন")
                        }
                    }
                }
            }
        } else {
            itemsIndexed(
                items = uiState.filteredSignals,
                key = { _, item -> item.id }
            ) { index, signal ->
                SignalCard(signal = signal, index = index)
            }
        }

        item(key = "extracted_text") {
            ExtractedTextAccordion(
                extractedText = uiState.activeBatch.extractedText,
                expanded = uiState.showExtractedText,
                onToggle = onToggleExtractedText
            )
        }

        item(key = "disclaimer") {
            DisclaimerFooterCard()
        }
    }
}

@Composable
private fun TerminalHeaderCard(bdClock: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            color = EmeraldBuyBg.copy(alpha = 0.5f),
            shape = RoundedCornerShape(999.dp),
            border = BorderStroke(1.dp, EmeraldBuy.copy(alpha = 0.35f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Bolt,
                    contentDescription = null,
                    tint = EmeraldBuyLight,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "১-মিনিট (1M) ফরেক্স ও বাইনারি নিউজ সিগন্যাল",
                    style = MaterialTheme.typography.bodySmall,
                    color = EmeraldBuyLight,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Text(
            text = "Forex News Signal AI",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )

        Text(
            text = "একটি ইকোনমিক ক্যালেন্ডার বা নিউজ স্ক্রিনশট থেকে ৪–৫টি ১-মিনিট BUY/SELL সিগন্যাল তৈরি করুন এবং রেড বনাম মিডিয়াম ইমপ্যাক্ট আলাদা করে দেখুন।",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted
        )

        // Live BD Time Clock Strip
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("live_bd_clock_card"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = TerminalSurface),
            border = BorderStroke(1.dp, TerminalBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "বর্তমান সময় (BDT / UTC+6)",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                    Text(
                        text = "স্ক্রিনশটের সময় ও ব্রোকার টাইম সিঙ্ক রাখুন",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextDim
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(TerminalObsidian)
                        .border(1.dp, EmeraldBuy.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AccessTime,
                        contentDescription = stringResource(R.string.cd_live_clock),
                        tint = EmeraldBuy,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = bdClock,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = EmeraldBuyLight
                    )
                }
            }
        }
    }
}

@Composable
private fun ScreenshotUploadCard(
    selectedBitmap: Bitmap?,
    selectedFileName: String?,
    analysisStatus: AnalysisStatus,
    onPickScreenshot: () -> Unit,
    onClearScreenshot: () -> Unit,
    onAnalyzeClick: () -> Unit,
    onLoadDemoClick: () -> Unit
) {
    val isLoading = analysisStatus is AnalysisStatus.Loading

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = TerminalSurface),
        border = BorderStroke(1.dp, TerminalBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "নিউজ স্ক্রিনশট আপলোড",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )

                OutlinedButton(
                    onClick = onLoadDemoClick,
                    enabled = !isLoading,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, SkyAccent.copy(alpha = 0.45f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("load_demo_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Layers,
                        contentDescription = null,
                        tint = SkyAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ডেমো ৫টি সিগন্যাল",
                        style = MaterialTheme.typography.bodySmall,
                        color = SkyAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Photo Picker Dropzone Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(TerminalObsidian.copy(alpha = 0.72f))
                    .border(
                        width = 1.5.dp,
                        color = if (selectedBitmap != null) EmeraldBuy.copy(alpha = 0.5f) else TerminalBorder,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable(enabled = !isLoading, onClick = onPickScreenshot)
                    .minimumInteractiveComponentSize()
                    .testTag("pick_screenshot_box")
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (selectedBitmap != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            bitmap = selectedBitmap.asImageBitmap(),
                            contentDescription = stringResource(R.string.cd_selected_screenshot),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, TerminalBorder, RoundedCornerShape(10.dp))
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldBuy,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = selectedFileName ?: "screenshot.png",
                                style = MaterialTheme.typography.bodySmall,
                                color = EmeraldBuyLight,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            TextButton(
                                onClick = onClearScreenshot,
                                enabled = !isLoading,
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("clear_screenshot_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.DeleteOutline,
                                    contentDescription = stringResource(R.string.cd_clear_screenshot),
                                    tint = RoseSellLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "ছবি সরান",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = RoseSellLight
                                )
                            }
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(TerminalSurfaceElevated)
                                .border(1.dp, TerminalBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.UploadFile,
                                contentDescription = stringResource(R.string.cd_upload_screenshot),
                                tint = EmeraldBuy,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Text(
                            text = "ক্যালেন্ডার বা নিউজ স্ক্রিনশট বেছে নিতে ট্যাপ করুন",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "একটি স্ক্রিনশট থেকেই ৪–৫টি ১-মিনিট (1M) সিগন্যাল তৈরি হবে",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Firebase AI Logic Status Row (replaces raw API Key input)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(TerminalObsidian.copy(alpha = 0.6f))
                    .border(1.dp, TerminalBorder, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.VerifiedUser,
                    contentDescription = null,
                    tint = EmeraldBuy,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Firebase AI Logic (Gemini 3.8 Flash) সংযুক্ত — কোনো API Key প্রয়োজন নেই",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            AnimatedVisibility(visible = analysisStatus is AnalysisStatus.Error) {
                val errorMessage = (analysisStatus as? AnalysisStatus.Error)?.message.orEmpty()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(RoseSellBg.copy(alpha = 0.55f))
                        .border(1.dp, RoseSell.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                        .testTag("analysis_error_banner"),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Rounded.WarningAmber,
                        contentDescription = null,
                        tint = RoseSellLight,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = RoseSellLight
                    )
                }
            }

            Button(
                onClick = onAnalyzeClick,
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldBuy,
                    contentColor = TerminalObsidian,
                    disabledContainerColor = EmeraldBuy.copy(alpha = 0.45f),
                    disabledContentColor = TerminalObsidian
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .minimumInteractiveComponentSize()
                    .testTag("analyze_screenshot_button")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = TerminalObsidian
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "স্ক্রিনশট থেকে ৪–৫টি ১M সিগন্যাল তৈরি হচ্ছে...",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "স্ক্রিনশট বিশ্লেষণ করে ৪–৫টি ১M সিগন্যাল নিন",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ActiveBatchAndFilterCard(
    uiState: SignalTerminalUiState,
    onSelectImpactFilter: (ImpactFilter) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = TerminalSurface),
        border = BorderStroke(1.dp, TerminalBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    color = TerminalSurfaceElevated,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "মোট ${uiState.totalCount}টি ১-মিনিট সিগন্যাল",
                        style = MaterialTheme.typography.bodySmall,
                        color = EmeraldBuyLight,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
                Surface(
                    color = RedImpact.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "রেড: ${uiState.highCount}টি",
                        style = MaterialTheme.typography.bodySmall,
                        color = RedImpactLight,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
                Surface(
                    color = AmberMedium.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "মিডিয়াম: ${uiState.mediumCount}টি",
                        style = MaterialTheme.typography.bodySmall,
                        color = AmberMediumLight,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Text(
                text = uiState.activeBatch.title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = uiState.activeBatch.calendarSummaryBn,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.impactFilter == ImpactFilter.ALL,
                    onClick = { onSelectImpactFilter(ImpactFilter.ALL) },
                    label = { Text("সব (${uiState.totalCount})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TextPrimary,
                        selectedLabelColor = TerminalObsidian,
                        containerColor = TerminalSurfaceElevated,
                        labelColor = TextSecondary
                    ),
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("filter_all_chip")
                )

                FilterChip(
                    selected = uiState.impactFilter == ImpactFilter.HIGH,
                    onClick = { onSelectImpactFilter(ImpactFilter.HIGH) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.LocalFireDepartment,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = { Text("রেড ইমপ্যাক্ট (${uiState.highCount})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = RedImpact,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White,
                        containerColor = RedImpact.copy(alpha = 0.12f),
                        labelColor = RedImpactLight,
                        iconColor = RedImpactLight
                    ),
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("filter_high_chip")
                )

                FilterChip(
                    selected = uiState.impactFilter == ImpactFilter.MEDIUM,
                    onClick = { onSelectImpactFilter(ImpactFilter.MEDIUM) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.ShowChart,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = { Text("মিডিয়াম (${uiState.mediumCount})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AmberMedium,
                        selectedLabelColor = TerminalObsidian,
                        selectedLeadingIconColor = TerminalObsidian,
                        containerColor = AmberMedium.copy(alpha = 0.12f),
                        labelColor = AmberMediumLight,
                        iconColor = AmberMediumLight
                    ),
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("filter_medium_chip")
                )
            }
        }
    }
}

@Composable
private fun ExtractedTextAccordion(
    extractedText: String,
    expanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = TerminalSurface),
        border = BorderStroke(1.dp, TerminalBorder)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .minimumInteractiveComponentSize()
                    .testTag("toggle_extracted_text_button")
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Description,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "স্ক্রিনশট / ক্যালেন্ডার থেকে প্রাপ্ত তথ্য দেখুন",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                    contentDescription = stringResource(R.string.cd_expand_raw),
                    tint = TextMuted
                )
            }

            AnimatedVisibility(visible = expanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(TerminalObsidian)
                        .border(1.dp, TerminalBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = extractedText,
                        fontFamily = JetBrainsMonoFontFamily,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun TerminalBottomBar(
    selectedTab: TerminalTab,
    onSelectTab: (TerminalTab) -> Unit
) {
    NavigationBar(
        containerColor = TerminalSurface,
        contentColor = TextPrimary,
        windowInsets = WindowInsets.navigationBars
    ) {
        NavigationBarItem(
            selected = selectedTab == TerminalTab.SIGNALS,
            onClick = { onSelectTab(TerminalTab.SIGNALS) },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Bolt,
                    contentDescription = stringResource(R.string.tab_signals)
                )
            },
            label = { Text(stringResource(R.string.tab_signals)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EmeraldBuyLight,
                selectedTextColor = EmeraldBuyLight,
                indicatorColor = EmeraldBuyBg,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_tab_signals")
        )

        NavigationBarItem(
            selected = selectedTab == TerminalTab.GUIDE,
            onClick = { onSelectTab(TerminalTab.GUIDE) },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.MenuBook,
                    contentDescription = stringResource(R.string.tab_guide)
                )
            },
            label = { Text(stringResource(R.string.tab_guide)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SkyAccent,
                selectedTextColor = SkyAccent,
                indicatorColor = SkyAccentBg,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_tab_guide")
        )
    }
}

@Composable
private fun TerminalNavigationRail(
    selectedTab: TerminalTab,
    onSelectTab: (TerminalTab) -> Unit
) {
    NavigationRail(
        containerColor = TerminalSurface,
        contentColor = TextPrimary
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        NavigationRailItem(
            selected = selectedTab == TerminalTab.SIGNALS,
            onClick = { onSelectTab(TerminalTab.SIGNALS) },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Bolt,
                    contentDescription = stringResource(R.string.tab_signals)
                )
            },
            label = { Text(stringResource(R.string.tab_signals)) },
            colors = NavigationRailItemDefaults.colors(
                selectedIconColor = EmeraldBuyLight,
                selectedTextColor = EmeraldBuyLight,
                indicatorColor = EmeraldBuyBg,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("rail_tab_signals")
        )

        NavigationRailItem(
            selected = selectedTab == TerminalTab.GUIDE,
            onClick = { onSelectTab(TerminalTab.GUIDE) },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.MenuBook,
                    contentDescription = stringResource(R.string.tab_guide)
                )
            },
            label = { Text(stringResource(R.string.tab_guide)) },
            colors = NavigationRailItemDefaults.colors(
                selectedIconColor = SkyAccent,
                selectedTextColor = SkyAccent,
                indicatorColor = SkyAccentBg,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("rail_tab_guide")
        )
    }
}
