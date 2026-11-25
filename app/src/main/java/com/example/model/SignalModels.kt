package com.example.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

enum class SignalDirection {
    BUY,
    SELL
}

enum class ImpactLevel {
    HIGH,
    MEDIUM,
    LOW
}

enum class CurrencyBias {
    BULLISH,
    BEARISH,
    NEUTRAL
}

enum class ImpactFilter {
    ALL,
    HIGH,
    MEDIUM
}

enum class TerminalTab {
    SIGNALS,
    GUIDE
}

data class ForexSignal(
    val id: String,
    val newsTitle: String,
    val currency: String,
    val suggestedPair: String,
    val event: String,
    val newsTime: String,
    val entryTime: String,
    val timeframe: String,
    val actual: String?,
    val forecast: String?,
    val previous: String?,
    val impact: ImpactLevel,
    val currencyBias: CurrencyBias,
    val signal: SignalDirection,
    val confidence: String,
    val howToEnterBn: String,
    val reasonBn: String,
    val riskBn: String
)

data class SignalBatch(
    val title: String,
    val calendarSummaryBn: String,
    val extractedText: String,
    val signals: List<ForexSignal>,
    val isDemo: Boolean = false
)

sealed interface AnalysisStatus {
    data object Idle : AnalysisStatus
    data object Loading : AnalysisStatus
    data object Success : AnalysisStatus
    data class Error(val message: String) : AnalysisStatus
}

data class OneMinuteEntryGuide(
    val entryWindow: String,
    val expiryNote: String
)

fun formatBdTime(date: Date = Date()): String {
    return try {
        val formatter = SimpleDateFormat("HH:mm:ss", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("Asia/Dhaka")
        formatter.format(date)
    } catch (_: Exception) {
        "00:00:00"
    }
}

fun computeOneMinuteEntryGuide(newsTimeStr: String?): OneMinuteEntryGuide {
    val trimmed = newsTimeStr?.trim().orEmpty()
    if (trimmed.isEmpty()) {
        return OneMinuteEntryGuide(
            entryWindow = "নিউজ রিলিজের +০৩ সেকেন্ডে",
            expiryNote = "১ মিনিট (1M) এক্সপায়ারি / ক্যান্ডেল ক্লোজ"
        )
    }

    val regex24 = Regex("^(\\d{1,2}):(\\d{2})(?::(\\d{2}))?$")
    val regex12 = Regex("^(\\d{1,2}):(\\d{2})(?::(\\d{2}))?\\s*(AM|PM)$", RegexOption.IGNORE_CASE)

    var hours: Int? = null
    var minutes: Int? = null

    val match24 = regex24.matchEntire(trimmed)
    val match12 = regex12.matchEntire(trimmed)

    if (match24 != null) {
        hours = match24.groupValues[1].toIntOrNull()
        minutes = match24.groupValues[2].toIntOrNull()
    } else if (match12 != null) {
        hours = match12.groupValues[1].toIntOrNull()
        minutes = match12.groupValues[2].toIntOrNull()
        val period = match12.groupValues[4].uppercase(Locale.US)
        if (hours != null) {
            if (period == "PM" && hours != 12) hours += 12
            if (period == "AM" && hours == 12) hours = 0
        }
    }

    if (hours == null || minutes == null || hours !in 0..23 || minutes !in 0..59) {
        return OneMinuteEntryGuide(
            entryWindow = "$trimmed এর ঠিক ৩–৫ সেকেন্ড পর",
            expiryNote = "১ মিনিট (1M) ট্রেড ডিউরেশন"
        )
    }

    val hh = String.format(Locale.US, "%02d", hours)
    val mm = String.format(Locale.US, "%02d", minutes)
    val nextMin = String.format(Locale.US, "%02d", (minutes + 1) % 60)

    return OneMinuteEntryGuide(
        entryWindow = "$hh:$mm:03 – $hh:$mm:08",
        expiryNote = "১ মিনিট (1M) — পরবর্তী ক্যান্ডেল ($hh:$nextMin:00) পর্যন্ত"
    )
}
