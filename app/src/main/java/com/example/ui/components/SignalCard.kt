package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.ShowChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.CurrencyBias
import com.example.model.ForexSignal
import com.example.model.ImpactLevel
import com.example.model.SignalDirection
import com.example.ui.theme.AmberMedium
import com.example.ui.theme.AmberMediumBg
import com.example.ui.theme.AmberMediumLight
import com.example.ui.theme.EmeraldBuy
import com.example.ui.theme.EmeraldBuyBg
import com.example.ui.theme.EmeraldBuyLight
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.RedImpact
import com.example.ui.theme.RedImpactBg
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignalCard(
    signal: ForexSignal,
    index: Int,
    modifier: Modifier = Modifier
) {
    val borderColor = when (signal.impact) {
        ImpactLevel.HIGH -> RedImpact.copy(alpha = 0.42f)
        ImpactLevel.MEDIUM -> AmberMedium.copy(alpha = 0.36f)
        ImpactLevel.LOW -> TerminalBorder
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("signal_card_${signal.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = TerminalSurface),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Row: Index + Impact + 1M Pill + BUY/SELL Signal Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                FlowRow(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = TerminalSurfaceElevated,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "#${index + 1}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    ImpactBadge(impact = signal.impact)

                    Surface(
                        color = SkyAccentBg.copy(alpha = 0.55f),
                        shape = RoundedCornerShape(999.dp),
                        border = BorderStroke(1.dp, SkyAccent.copy(alpha = 0.35f))
                    ) {
                        Text(
                            text = signal.timeframe,
                            style = MaterialTheme.typography.labelSmall,
                            color = SkyAccent,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                SignalDirectionBadge(direction = signal.signal)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // News Title & Currency/Pair/Bias Metadata
            Text(
                text = signal.newsTitle,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "কারেন্সি: ${signal.currency}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextDim
                )
                Text(
                    text = "পেয়ার: ${signal.suggestedPair}",
                    style = MaterialTheme.typography.bodySmall,
                    color = EmeraldBuyLight,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextDim
                )
                val (biasText, biasColor) = when (signal.currencyBias) {
                    CurrencyBias.BULLISH -> "Bullish (বুলিশ)" to EmeraldBuyLight
                    CurrencyBias.BEARISH -> "Bearish (বিয়ারিশ)" to RoseSellLight
                    CurrencyBias.NEUTRAL -> "Neutral (নিউট্রাল)" to TextSecondary
                }
                Text(
                    text = "বায়াস: $biasText",
                    style = MaterialTheme.typography.bodySmall,
                    color = biasColor,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // News Time vs 1M Entry Time Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(TerminalObsidian.copy(alpha = 0.85f))
                    .border(1.dp, TerminalBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ইভেন্ট / নিউজ সময়",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.AccessTime,
                            contentDescription = null,
                            tint = SkyAccent,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = signal.newsTime,
                            fontFamily = JetBrainsMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary
                        )
                    }
                }

                Column(modifier = Modifier.weight(1.25f)) {
                    Text(
                        text = "১-মিনিট এন্ট্রি সময়",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = EmeraldBuy,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = signal.entryTime,
                            fontFamily = JetBrainsMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall,
                            color = EmeraldBuyLight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3-Column Actual / Forecast / Previous Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricCell(
                    label = "ACTUAL",
                    value = signal.actual ?: "N/A",
                    valueColor = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                MetricCell(
                    label = "FORECAST",
                    value = signal.forecast ?: "N/A",
                    valueColor = TextSecondary,
                    modifier = Modifier.weight(1f)
                )
                MetricCell(
                    label = "PREVIOUS",
                    value = signal.previous ?: "N/A",
                    valueColor = TextMuted,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Step-by-step 1M Entry Instructions
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(EmeraldBuyBg.copy(alpha = 0.25f))
                    .border(1.dp, EmeraldBuy.copy(alpha = 0.28f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "কীভাবে ১-মিনিটের ট্রেড নেবেন:",
                        style = MaterialTheme.typography.labelLarge,
                        color = EmeraldBuyLight
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = signal.howToEnterBn,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Analysis Reasoning
            Text(
                text = "বিশ্লেষণের কারণ:",
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = signal.reasonBn,
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = TerminalBorder)
            Spacer(modifier = Modifier.height(10.dp))

            // Risk Warning Footer
            Text(
                text = "ঝুঁকি সতর্কতা: ${signal.riskBn}",
                style = MaterialTheme.typography.bodySmall,
                color = AmberMediumLight.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun ImpactBadge(impact: ImpactLevel) {
    val (label, bgColor, borderColor, textColor, icon, cd) = when (impact) {
        ImpactLevel.HIGH -> ImpactStyle(
            label = "রেড ইমপ্যাক্ট (High)",
            bg = RedImpactBg.copy(alpha = 0.7f),
            border = RedImpact.copy(alpha = 0.55f),
            text = RedImpactLight,
            icon = Icons.Rounded.LocalFireDepartment,
            cd = stringResource(R.string.cd_high_impact)
        )
        ImpactLevel.MEDIUM -> ImpactStyle(
            label = "মিডিয়াম ইমপ্যাক্ট",
            bg = AmberMediumBg.copy(alpha = 0.7f),
            border = AmberMedium.copy(alpha = 0.55f),
            text = AmberMediumLight,
            icon = Icons.Rounded.ShowChart,
            cd = stringResource(R.string.cd_medium_impact)
        )
        ImpactLevel.LOW -> ImpactStyle(
            label = "লো ইমপ্যাক্ট",
            bg = TerminalSurfaceElevated,
            border = TerminalBorder,
            text = TextMuted,
            icon = Icons.Rounded.Info,
            cd = stringResource(R.string.cd_low_impact)
        )
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(999.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = cd,
                tint = textColor,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = textColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SignalDirectionBadge(direction: SignalDirection) {
    val isBuy = direction == SignalDirection.BUY
    val bgColor = if (isBuy) EmeraldBuyBg.copy(alpha = 0.85f) else RoseSellBg.copy(alpha = 0.85f)
    val borderColor = if (isBuy) EmeraldBuy.copy(alpha = 0.65f) else RoseSell.copy(alpha = 0.65f)
    val textColor = if (isBuy) EmeraldBuyLight else RoseSellLight
    val icon = if (isBuy) Icons.AutoMirrored.Rounded.TrendingUp else Icons.AutoMirrored.Rounded.TrendingDown
    val label = if (isBuy) "BUY / CALL" else "SELL / PUT"
    val cd = stringResource(if (isBuy) R.string.cd_buy_signal else R.string.cd_sell_signal)

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = cd,
                tint = textColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun MetricCell(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(TerminalObsidian.copy(alpha = 0.65f))
            .border(1.dp, TerminalBorder, RoundedCornerShape(10.dp))
            .padding(vertical = 8.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextDim,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = value,
            fontFamily = JetBrainsMonoFontFamily,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodySmall,
            color = valueColor,
            textAlign = TextAlign.Center
        )
    }
}

private data class ImpactStyle(
    val label: String,
    val bg: androidx.compose.ui.graphics.Color,
    val border: androidx.compose.ui.graphics.Color,
    val text: androidx.compose.ui.graphics.Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val cd: String
)
