package com.example.data

import android.graphics.Bitmap
import android.util.Log
import com.example.model.CurrencyBias
import com.example.model.ForexSignal
import com.example.model.ImpactLevel
import com.example.model.SignalBatch
import com.example.model.SignalDirection
import com.example.model.computeOneMinuteEntryGuide
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import java.util.Locale
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.json.JSONObject

private const val TAG = "SignalAnalyzerRepo"

data class RawSignalDto(
    val id: String? = null,
    val newsTitle: String? = null,
    val currency: String? = null,
    val suggestedPair: String? = null,
    val event: String? = null,
    val newsTime: String? = null,
    val entryTime: String? = null,
    val timeframe: String? = null,
    val actual: String? = null,
    val forecast: String? = null,
    val previous: String? = null,
    val impact: String? = null,
    val currencyBias: String? = null,
    val signal: String? = null,
    val confidence: String? = null,
    val howToEnterBn: String? = null,
    val reasonBn: String? = null,
    val riskBn: String? = null
)

class SignalAnalyzerRepository {

    private val signalItemSchema = Schema.obj(
        properties = mapOf(
            "news_title" to Schema.string(description = "Title of the economic news event"),
            "currency" to Schema.string(description = "3-letter currency code such as USD, EUR, GBP, JPY"),
            "suggested_pair" to Schema.string(description = "Recommended forex pair such as EUR/USD"),
            "event" to Schema.string(description = "Short event name"),
            "news_time" to Schema.string(description = "Release time e.g. 19:30"),
            "entry_time" to Schema.string(description = "Exact 1-minute entry time e.g. 19:30:04"),
            "timeframe" to Schema.string(description = "1 Minute (1M)"),
            "actual" to Schema.string(description = "Actual value or null", nullable = true),
            "forecast" to Schema.string(description = "Forecast value or null", nullable = true),
            "previous" to Schema.string(description = "Previous value or null", nullable = true),
            "impact" to Schema.enumeration(
                values = listOf("high", "medium", "low"),
                description = "Event impact level"
            ),
            "currency_bias" to Schema.enumeration(
                values = listOf("bullish", "bearish", "neutral"),
                description = "Bias of the affected currency"
            ),
            "signal" to Schema.enumeration(
                values = listOf("BUY", "SELL"),
                description = "Actionable 1-minute direction, never WAIT"
            ),
            "confidence" to Schema.enumeration(
                values = listOf("high", "medium", "low"),
                description = "Signal confidence level"
            ),
            "how_to_enter_bn" to Schema.string(description = "Step-by-step 1M entry instructions in Bengali"),
            "reason_bn" to Schema.string(description = "Analysis reasoning in Bengali"),
            "risk_bn" to Schema.string(description = "Risk warning in Bengali")
        )
    )

    private val analysisResponseSchema = Schema.obj(
        properties = mapOf(
            "calendar_summary_bn" to Schema.string(description = "Summary of the screenshot session in Bengali"),
            "extracted_text" to Schema.string(description = "Raw text extracted from the economic calendar screenshot"),
            "signals" to Schema.array(
                items = signalItemSchema,
                description = "4 to 5 actionable 1-minute BUY or SELL signals"
            )
        )
    )

    private val systemInstructionText = """
        তুমি একজন দক্ষ ১-মিনিট (1M) ফরেক্স নিউজ ও বাইনারি অপশন সিগন্যাল বিশ্লেষক।
        ব্যবহারকারী একটি ফরেক্স নিউজ বা ইকোনমিক ক্যালেন্ডারের স্ক্রিনশট দিয়েছে।

        কঠোর নিয়মাবলী:
        ১. স্ক্রিনশট থেকে কমপক্ষে ৪টি এবং সর্বোচ্চ ৫টি (4 to 5) পরিষ্কার BUY অথবা SELL ১-মিনিট সিগন্যাল তৈরি করো।
        ২. "signal" হিসেবে কখনোই "WAIT" বা "NEUTRAL" দেওয়া যাবে না। শুধুমাত্র "BUY" অথবা "SELL" দিতে হবে। নিউজ ডেটা কম থাকলেও মোমেন্টাম ও বায়াস বিশ্লেষণ করে স্পষ্ট BUY বা SELL সিদ্ধান্ত নির্ধারণ করো।
        ৩. প্রতিটি সিগন্যালের impact অবশ্যই "high" (Red / High Impact) অথবা "medium" (Medium Impact) হিসেবে চিহ্নিত করো।
        ৪. সময় সূচি:
           - "news_time": ইভেন্ট প্রকাশের সময় (যেমন "19:30")
           - "entry_time": ১-মিনিট এন্ট্রি নেওয়ার সুনির্দিষ্ট সময় (যেমন "19:30:04")
           - "timeframe": "1 Minute (1M)"
           - "how_to_enter_bn": ১-মিনিটে কখন ও কীভাবে BUY/SELL দিবেন তার ধাপে ধাপে নিয়ম বাংলায় স্পষ্ট করে দাও।
        ৫. সব উত্তর বাংলায় লেখো। কখনোই গ্যারান্টিড লাভের কথা বলবে না।
    """.trimIndent()

    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = analysisResponseSchema
            },
            systemInstruction = content {
                text(systemInstructionText)
            }
        )
    }

    suspend fun analyzeScreenshot(bitmap: Bitmap, fileLabel: String): SignalBatch {
        try {
            val userPrompt =
                "এই স্ক্রিনশটটি বিশ্লেষণ করে ৪-৫টি সুনির্দিষ্ট ১-মিনিট BUY বা SELL সিগন্যাল তৈরি করো। কোনো WAIT সিগন্যাল দিও না। Red এবং Medium Impact আলাদা করে দেখাও এবং নিখুঁত এন্ট্রি সময় দাও।"

            val response = withTimeout(60_000L) {
                model.generateContent(
                    content {
                        image(bitmap)
                        text(userPrompt)
                    }
                )
            }

            val jsonString = response.text
                ?: throw IllegalStateException("মডেল থেকে কোনো JSON উত্তর পাওয়া যায়নি।")

            return parseResponseJson(jsonString, fileLabel)
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Screenshot analysis timed out", e)
            throw IllegalStateException("বিশ্লেষণে ৬০ সেকেন্ডের বেশি সময় লেগেছে। আবার চেষ্টা করুন।", e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Firebase AI screenshot analysis failed", e)
            throw IllegalStateException(
                e.localizedMessage ?: "স্ক্রিনশট বিশ্লেষণে সমস্যা হয়েছে। ইন্টারনেট সংযোগ যাচাই করুন।",
                e
            )
        }
    }

    internal fun parseResponseJson(jsonString: String, fileLabel: String): SignalBatch {
        val root = JSONObject(jsonString)
        val summaryBn = root.optString("calendar_summary_bn")
            .takeIf { it.isNotBlank() }
            ?: "আপলোড করা স্ক্রিনশট থেকে ৪-৫টি সুনির্দিষ্ট ১-মিনিট সিগন্যাল ফিল্টার করে দেখানো হচ্ছে।"
        val extractedText = root.optString("extracted_text")
            .takeIf { it.isNotBlank() }
            ?: jsonString
        val rawArray = root.optJSONArray("signals") ?: JSONArray()
        val dtos = (0 until rawArray.length()).mapNotNull { idx ->
            rawArray.optJSONObject(idx)?.toRawSignalDto()
        }
        val normalized = normalizeSignals(dtos)

        if (normalized.isEmpty()) {
            throw IllegalStateException("স্ক্রিনশট থেকে কোনো কার্যকর সিগন্যাল তৈরি করা যায়নি।")
        }

        return SignalBatch(
            title = "স্ক্রিনশট বিশ্লেষণ: $fileLabel",
            calendarSummaryBn = summaryBn,
            extractedText = extractedText,
            signals = normalized,
            isDemo = false
        )
    }

    private fun JSONObject.toRawSignalDto(): RawSignalDto {
        return RawSignalDto(
            id = optStringOrNull("id"),
            newsTitle = optStringOrNull("news_title"),
            currency = optStringOrNull("currency"),
            suggestedPair = optStringOrNull("suggested_pair"),
            event = optStringOrNull("event"),
            newsTime = optStringOrNull("news_time"),
            entryTime = optStringOrNull("entry_time"),
            timeframe = optStringOrNull("timeframe"),
            actual = optStringOrNull("actual"),
            forecast = optStringOrNull("forecast"),
            previous = optStringOrNull("previous"),
            impact = optStringOrNull("impact"),
            currencyBias = optStringOrNull("currency_bias"),
            signal = optStringOrNull("signal"),
            confidence = optStringOrNull("confidence"),
            howToEnterBn = optStringOrNull("how_to_enter_bn"),
            reasonBn = optStringOrNull("reason_bn"),
            riskBn = optStringOrNull("risk_bn")
        )
    }

    private fun JSONObject.optStringOrNull(key: String): String? {
        if (!has(key) || isNull(key)) return null
        val value = optString(key)
        return value.takeIf { it.isNotBlank() && !it.equals("null", ignoreCase = true) }
    }

    companion object {
        fun normalizeSignals(
            rawItems: List<RawSignalDto>,
            fallbackTitle: String = "ইকোনমিক নিউজ সিগন্যাল"
        ): List<ForexSignal> {
            if (rawItems.isEmpty()) return emptyList()

            val actionable = rawItems.filter { item ->
                val sig = item.signal.orEmpty().uppercase(Locale.US)
                sig.contains("BUY") || sig.contains("SELL") || sig.contains("CALL") || sig.contains("PUT")
            }
            val sourceList = (if (actionable.isNotEmpty()) actionable else rawItems).take(5)

            return sourceList.mapIndexed { index, item ->
                val impactRaw = (item.impact ?: "medium").lowercase(Locale.US)
                val impact = when {
                    impactRaw.contains("high") || impactRaw.contains("red") -> ImpactLevel.HIGH
                    impactRaw.contains("low") -> ImpactLevel.LOW
                    else -> ImpactLevel.MEDIUM
                }

                val biasRaw = (item.currencyBias ?: "bullish").lowercase(Locale.US)
                val currencyBias = when {
                    biasRaw.contains("bear") -> CurrencyBias.BEARISH
                    biasRaw.contains("neut") -> CurrencyBias.NEUTRAL
                    else -> CurrencyBias.BULLISH
                }

                val sigRaw = item.signal.orEmpty().uppercase(Locale.US)
                val signal = if (sigRaw.contains("SELL") || sigRaw.contains("PUT")) {
                    SignalDirection.SELL
                } else {
                    SignalDirection.BUY
                }

                val confRaw = (item.confidence ?: "medium").lowercase(Locale.US)
                val confidence = when {
                    confRaw.contains("high") -> "high"
                    confRaw.contains("low") -> "low"
                    else -> "medium"
                }

                val newsTime = item.newsTime?.takeIf { it.isNotBlank() } ?: "রিলিজ সময়ে"
                val computedTiming = computeOneMinuteEntryGuide(newsTime)
                val entryTime = item.entryTime?.takeIf { it.isNotBlank() }
                    ?: computedTiming.entryWindow

                val defaultPair = if (signal == SignalDirection.BUY) "EUR/USD" else "GBP/USD"

                ForexSignal(
                    id = item.id?.takeIf { it.isNotBlank() } ?: "gen-sig-${index + 1}",
                    newsTitle = item.newsTitle?.takeIf { it.isNotBlank() }
                        ?: "$fallbackTitle #${index + 1}",
                    currency = item.currency?.takeIf { it.isNotBlank() } ?: "USD",
                    suggestedPair = item.suggestedPair?.takeIf { it.isNotBlank() } ?: defaultPair,
                    event = item.event?.takeIf { it.isNotBlank() } ?: "Economic Release",
                    newsTime = newsTime,
                    entryTime = entryTime,
                    timeframe = item.timeframe?.takeIf { it.isNotBlank() } ?: "1 Minute (1M)",
                    actual = item.actual,
                    forecast = item.forecast,
                    previous = item.previous,
                    impact = impact,
                    currencyBias = currencyBias,
                    signal = signal,
                    confidence = confidence,
                    howToEnterBn = item.howToEnterBn?.takeIf { it.isNotBlank() }
                        ?: "$newsTime নিউজের ঠিক ৩–৫ সেকেন্ড পর ক্যান্ডেলের প্রাথমিক স্পাইক দেখে 1M (${signal.name}) এন্ট্রি নিন। ১ মিনিট (60s) পর্যন্ত হোল্ড করুন।",
                    reasonBn = item.reasonBn?.takeIf { it.isNotBlank() }
                        ?: "নিউজ ডেটা ও ক্যান্ডেল বায়াসের ভিত্তি ধরে ১-মিনিট স্কেলিং সিগন্যাল নির্ধারণ করা হয়েছে।",
                    riskBn = item.riskBn?.takeIf { it.isNotBlank() }
                        ?: "১-মিনিট ট্রেডিংয়ে স্প্রেড পরিবর্তন ও রিভার্সাল হতে পারে। মনি ম্যানেজমেন্ট বজায় রাখুন।"
                )
            }
        }
    }
}
