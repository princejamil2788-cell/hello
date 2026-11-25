package com.example

import com.example.data.DemoSignalsProvider
import com.example.data.RawSignalDto
import com.example.data.SignalAnalyzerRepository
import com.example.model.ImpactLevel
import com.example.model.SignalDirection
import com.example.model.computeOneMinuteEntryGuide
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun `computeOneMinuteEntryGuide formats 24h and 12h news times`() {
        val guide24 = computeOneMinuteEntryGuide("19:30")
        assertEquals("19:30:03 – 19:30:08", guide24.entryWindow)

        val guide12 = computeOneMinuteEntryGuide("07:30 PM")
        assertEquals("19:30:03 – 19:30:08", guide12.entryWindow)
    }

    @Test
    fun `demo preset contains 5 actionable BUY or SELL signals`() {
        val signals = DemoSignalsProvider.demoBatch.signals
        assertEquals(5, signals.size)
        assertTrue(signals.any { it.impact == ImpactLevel.HIGH })
        assertTrue(signals.any { it.impact == ImpactLevel.MEDIUM })
        assertTrue(signals.all { it.signal == SignalDirection.BUY || it.signal == SignalDirection.SELL })
    }

    @Test
    fun `normalizeSignals filters WAIT and normalizes impact and signal`() {
        val dtos = listOf(
            RawSignalDto(
                newsTitle = "Wait Noise",
                signal = "WAIT",
                impact = "medium"
            ),
            RawSignalDto(
                newsTitle = "US CPI m/m",
                currency = "USD",
                suggestedPair = "EUR/USD",
                newsTime = "18:30",
                signal = "PUT",
                impact = "red",
                currencyBias = "bullish"
            )
        )

        val normalized = SignalAnalyzerRepository.normalizeSignals(dtos)
        assertEquals(1, normalized.size)
        val item = normalized.first()
        assertEquals(SignalDirection.SELL, item.signal)
        assertEquals(ImpactLevel.HIGH, item.impact)
        assertEquals("18:30:03 – 18:30:08", item.entryTime)
    }
}
