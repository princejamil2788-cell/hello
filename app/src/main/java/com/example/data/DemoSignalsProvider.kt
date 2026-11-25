package com.example.data

import com.example.model.CurrencyBias
import com.example.model.ForexSignal
import com.example.model.ImpactLevel
import com.example.model.SignalBatch
import com.example.model.SignalDirection

object DemoSignalsProvider {

    val demoBatch: SignalBatch = SignalBatch(
        title = "ডেমো ক্যালেন্ডার স্ক্রিনশট (৫টি ১-মিনিট সিগন্যাল)",
        calendarSummaryBn = "আজকের ইকোনমিক ক্যালেন্ডার সেশন: US Core Retail Sales (Red), EU ZEW Economic Sentiment (Medium), GBP Claimant Count Change (Red), JPY Trade Balance (Medium), এবং US Industrial Production (Medium)।",
        extractedText = """[Demo Calendar Screenshot Data]
19:30 | USD | Core Retail Sales m/m | Actual: 0.5% | Forecast: 0.2% | Prev: 0.1% | Impact: High (Red)
20:00 | EUR | German ZEW Economic Sentiment | Actual: 8.4 | Forecast: 14.0 | Prev: 13.1 | Impact: Medium
20:30 | GBP | Claimant Count Change | Actual: 38.2K | Forecast: 15.0K | Prev: 18.4K | Impact: High (Red)
21:00 | JPY | Adjusted Merchandise Trade Balance | Actual: -0.42T | Forecast: -0.25T | Prev: -0.31T | Impact: Medium
21:30 | USD | Industrial Production m/m | Actual: -0.3% | Forecast: 0.1% | Prev: 0.2% | Impact: Medium""",
        isDemo = true,
        signals = listOf(
            ForexSignal(
                id = "sig-1",
                newsTitle = "US Core Retail Sales m/m (রিলিজ সম্পন্ন)",
                currency = "USD",
                suggestedPair = "EUR/USD",
                event = "Core Retail Sales m/m",
                newsTime = "19:30",
                entryTime = "19:30:04",
                timeframe = "1 Minute (1M)",
                actual = "0.5%",
                forecast = "0.2%",
                previous = "0.1%",
                impact = ImpactLevel.HIGH,
                currencyBias = CurrencyBias.BULLISH,
                signal = SignalDirection.SELL,
                confidence = "high",
                howToEnterBn = "19:30 এ ডাটা প্রকাশের পর চার্টে প্রথম ৩-৪ সেকেন্ডের প্রাথমিক স্পাইক শেষ হতে দিন। 19:30:04 সেকেন্ডে EUR/USD চার্টে 1M SELL / PUT এন্ট্রি নিন এবং ঠিক ১ মিনিট (60s) হোল্ড করুন।",
                reasonBn = "Actual (0.5%) Forecast (0.2%) এবং Previous (0.1%) এর তুলনায় অনেক বেশি এসেছে। এতে USD শক্তিশালী হওয়ায় EUR/USD ১ মিনিটের জন্য নিচের দিকে চাপ পেতে পারে।",
                riskBn = "হাই/রেড ইমপ্যাক্ট নিউজে প্রথম ২ সেকেন্ডে স্প্রেড বাড়তে পারে ও হঠাৎ উইক (wick) তৈরি হতে পারে। নিশ্চিত লাভের কোনো নিশ্চয়তা নেই; ছোট অ্যামাউন্টে ট্রেড করুন।"
            ),
            ForexSignal(
                id = "sig-2",
                newsTitle = "German ZEW Economic Sentiment",
                currency = "EUR",
                suggestedPair = "EUR/USD",
                event = "ZEW Economic Sentiment",
                newsTime = "20:00",
                entryTime = "20:00:05",
                timeframe = "1 Minute (1M)",
                actual = "8.4",
                forecast = "14.0",
                previous = "13.1",
                impact = ImpactLevel.MEDIUM,
                currencyBias = CurrencyBias.BEARISH,
                signal = SignalDirection.SELL,
                confidence = "medium",
                howToEnterBn = "20:00 বাজে ইভেন্ট আসার পর ক্যান্ডেলের দিক নিশ্চিত করুন। 20:00:05 সেকেন্ডে EUR/USD-এ 1M SELL / PUT নিন; ক্যান্ডেল উপরের দিকে রিজেকশন দিলে এন্ট্রি আরও সতর্কতার সাথে যাচাই করুন।",
                reasonBn = "ZEW সেন্টিমেন্ট 8.4 এসেছে যা Forecast (14.0) এর চেয়ে বেশ কম। এতে EUR-এর ওপর স্বল্পমেয়াদী নেতিবাচক চাপ তৈরি হতে পারে।",
                riskBn = "মিডিয়াম ইমপ্যাক্ট নিউজে মুভমেন্ট মাঝে মাঝে ধীর বা সাইডওয়েজ হতে পারে। ১ মিনিটের বেশি সময় ধরে রাখবেন না।"
            ),
            ForexSignal(
                id = "sig-3",
                newsTitle = "UK Claimant Count Change",
                currency = "GBP",
                suggestedPair = "GBP/USD",
                event = "Claimant Count Change",
                newsTime = "20:30",
                entryTime = "20:30:03",
                timeframe = "1 Minute (1M)",
                actual = "38.2K",
                forecast = "15.0K",
                previous = "18.4K",
                impact = ImpactLevel.HIGH,
                currencyBias = CurrencyBias.BEARISH,
                signal = SignalDirection.SELL,
                confidence = "high",
                howToEnterBn = "বেকারত্ব আবেদন সংখ্যা প্রত্যাশার চেয়ে বেশি আসায় 20:30:03 সেকেন্ডে GBP/USD চার্টে 1M SELL / PUT সিগন্যাল অনুসরণ করুন। ১ মিনিটের এক্সপায়ারি বা ১M ক্যান্ডেল ক্লোজে নজর রাখুন।",
                reasonBn = "Claimant Count 38.2K এসেছে যা Forecast (15.0K) এর দ্বিগুণেরও বেশি—এটি শ্রমবাজারের দুর্বলতা ও GBP-এর ওপর বিয়ারিশ চাপের ইঙ্গিত দেয়।",
                riskBn = "রেড ইমপ্যাক্ট সময়ে দ্রুত রিভার্সাল বা স্লিপেজ হতে পারে। রিস্ক ম্যানেজমেন্ট ছাড়া ট্রেড করবেন না।"
            ),
            ForexSignal(
                id = "sig-4",
                newsTitle = "Japan Adjusted Merchandise Trade Balance",
                currency = "JPY",
                suggestedPair = "USD/JPY",
                event = "Trade Balance",
                newsTime = "21:00",
                entryTime = "21:00:05",
                timeframe = "1 Minute (1M)",
                actual = "-0.42T",
                forecast = "-0.25T",
                previous = "-0.31T",
                impact = ImpactLevel.MEDIUM,
                currencyBias = CurrencyBias.BEARISH,
                signal = SignalDirection.BUY,
                confidence = "medium",
                howToEnterBn = "21:00 বাজে ট্রেড ব্যালেন্স ঘাটতি বাড়ার তথ্য এলে 21:00:05 সেকেন্ডে USD/JPY-এ 1M BUY / CALL ট্রেড নিন (কারণ JPY দুর্বল হলে USD/JPY উপরের দিকে যেতে পারে)।",
                reasonBn = "বাণিজ্য ঘাটতি (-0.42T) পূর্বাভাসের (-0.25T) চেয়ে বেশি হওয়ায় JPY দুর্বল হওয়ার সম্ভাবনা তৈরি হয়েছে, যা USD/JPY পেয়ারে ঊর্ধ্বমুখী বায়াস (BUY) সমর্থন করে।",
                riskBn = "মিডিয়াম ইমপ্যাক্ট ডাটার প্রতিক্রিয়া সবসময় তীব্র হয় না; লিকুইডিটি ও স্প্রেড যাচাই করে নিন।"
            ),
            ForexSignal(
                id = "sig-5",
                newsTitle = "US Industrial Production m/m",
                currency = "USD",
                suggestedPair = "USD/CHF",
                event = "Industrial Production m/m",
                newsTime = "21:30",
                entryTime = "21:30:04",
                timeframe = "1 Minute (1M)",
                actual = "-0.3%",
                forecast = "0.1%",
                previous = "0.2%",
                impact = ImpactLevel.MEDIUM,
                currencyBias = CurrencyBias.BEARISH,
                signal = SignalDirection.SELL,
                confidence = "medium",
                howToEnterBn = "21:30 বাজে ডাটা প্রকাশের পর 21:30:04 সেকেন্ডে USD/CHF-এ 1M SELL / PUT এন্ট্রি বিবেচনা করুন। যদি প্রথম ২ সেকেন্ডে বড় বিপরীত স্পাইক দেখেন তবে সাবধান থাকুন।",
                reasonBn = "Industrial Production -0.3% এসেছে যা Forecast (0.1%) এর নিচে। এতে USD-এর ওপর সামান্য বিয়ারিশ বায়াস তৈরি হতে পারে।",
                riskBn = "মিশ্র বা দুর্বল সংকেতে ক্ষতির ঝুঁকি থাকে; এটি শুধুমাত্র বিশ্লেষণ ও শিক্ষামূলক সহায়তা, নিশ্চিত লাভের গ্যারান্টি নয়।"
            )
        )
    )
}
