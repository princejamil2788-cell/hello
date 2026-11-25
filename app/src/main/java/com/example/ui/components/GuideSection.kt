package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.AmberMedium
import com.example.ui.theme.AmberMediumBg
import com.example.ui.theme.AmberMediumLight
import com.example.ui.theme.EmeraldBuyLight
import com.example.ui.theme.SkyAccent
import com.example.ui.theme.TerminalBorder
import com.example.ui.theme.TerminalObsidian
import com.example.ui.theme.TerminalSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun GuideSection(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("guide_section_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = TerminalSurface),
        border = BorderStroke(1.dp, TerminalBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Timer,
                    contentDescription = null,
                    tint = SkyAccent,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "১-মিনিট (1M) নিউজ ট্রেডিং সময় ও নিয়ম গাইড",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            GuideStepCard(
                title = "১. সময় কীভাবে বুঝবেন? (নিউজ টাইম বনাম এন্ট্রি টাইম)",
                titleColor = EmeraldBuyLight,
                body = "প্রতিটি কার্ডে নিউজ সময় হলো ইভেন্ট প্রকাশের সময়, আর ১M এন্ট্রি সময় হলো নিউজ রিলিজের ঠিক ৩–৫ সেকেন্ড পর প্রথম প্রতিক্রিয়া/ক্যান্ডেল দেখে ট্রেড নেওয়ার সময়।"
            )

            GuideStepCard(
                title = "২. কীভাবে ১-মিনিটের ট্রেড নেবেন?",
                titleColor = SkyAccent,
                body = "চার্ট বা অপশন টার্মিনালে 1M (১ মিনিট) টাইমফ্রেম/এক্সপায়ারি সেট রাখুন। রিলিজের প্রথম ১–২ সেকেন্ডের অস্থির স্প্রেড বা হঠাৎ স্পাইক খেয়াল করে নির্ধারিত এন্ট্রি সময়ে BUY/CALL বা SELL/PUT দিন।"
            )

            GuideStepCard(
                title = "৩. রেড ইমপ্যাক্ট বনাম মিডিয়াম ইমপ্যাক্ট",
                titleColor = AmberMediumLight,
                body = "রেড ইমপ্যাক্ট (High) নিউজে গতি ও স্লিপেজ বেশি থাকে; মিডিয়াম ইমপ্যাক্ট (Medium) নিউজে মুভমেন্ট তুলনামূলক সহনীয় হতে পারে, তবে ফলস মুভও সম্ভব।"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AmberMediumBg.copy(alpha = 0.45f))
                    .border(1.dp, AmberMedium.copy(alpha = 0.38f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Rounded.Shield,
                        contentDescription = stringResource(R.string.cd_risk_warning),
                        tint = AmberMedium,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "সতর্কতা: কোনো সিগন্যালই নিশ্চিত লাভের গ্যারান্টি নয়। ১-মিনিট ট্রেডিং অত্যন্ত ঝুঁকিপূর্ণ—নিজস্ব রিস্ক ম্যানেজমেন্ট মেনে চলুন।",
                        style = MaterialTheme.typography.bodySmall,
                        color = AmberMediumLight
                    )
                }
            }
        }
    }
}

@Composable
fun DisclaimerFooterCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(TerminalSurface.copy(alpha = 0.7f))
            .border(1.dp, TerminalBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Text(
            text = "অস্বীকৃতি (Disclaimer): এই অ্যাপটি শুধুমাত্র শিক্ষামূলক ও নিউজ বিশ্লেষণ সহায়তার জন্য তৈরি। ফরেক্স ও ১-মিনিট (1M) ট্রেডিংয়ে উচ্চ মাত্রার আর্থিক ঝুঁকি রয়েছে এবং কোনো সিগন্যালই নিশ্চিত বা গ্যারান্টিড লাভের প্রতিশ্রুতি দেয় না। নিজস্ব দায়িত্বে ও ঝুঁকি ব্যবস্থাপনা মেনে সিদ্ধান্ত নিন।",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )
    }
}

@Composable
private fun GuideStepCard(
    title: String,
    titleColor: androidx.compose.ui.graphics.Color,
    body: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(TerminalObsidian.copy(alpha = 0.72f))
            .border(1.dp, TerminalBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = titleColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted
        )
    }
}
