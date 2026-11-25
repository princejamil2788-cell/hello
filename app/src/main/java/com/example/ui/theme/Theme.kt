package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

private val TerminalDarkColorScheme = darkColorScheme(
    primary = EmeraldBuy,
    onPrimary = TerminalObsidian,
    primaryContainer = EmeraldBuyBg,
    onPrimaryContainer = EmeraldBuyLight,
    secondary = SkyAccent,
    onSecondary = TerminalObsidian,
    secondaryContainer = SkyAccentBg,
    onSecondaryContainer = TextPrimary,
    tertiary = AmberMedium,
    onTertiary = TerminalObsidian,
    tertiaryContainer = AmberMediumBg,
    onTertiaryContainer = AmberMediumLight,
    error = RoseSell,
    onError = TextPrimary,
    errorContainer = RoseSellBg,
    onErrorContainer = RoseSellLight,
    background = TerminalObsidian,
    onBackground = TextPrimary,
    surface = TerminalSurface,
    onSurface = TextPrimary,
    surfaceVariant = TerminalSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = TerminalBorder,
    outlineVariant = TerminalBorderSubtle
)

val TerminalShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun FxSignalTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TerminalDarkColorScheme,
        typography = AppTypography,
        shapes = TerminalShapes,
        content = content
    )
}
