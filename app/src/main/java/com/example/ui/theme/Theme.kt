package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val VibrantDarkColorScheme = darkColorScheme(
  primary = VibrantPurple,
  onPrimary = VibrantPurpleContainer,
  primaryContainer = VibrantPurpleContainer,
  onPrimaryContainer = VibrantPurple,
  secondary = VibrantTextSecondary,
  onSecondary = VibrantDarkBackground,
  secondaryContainer = VibrantNavPill,
  onSecondaryContainer = VibrantTextPrimary,
  tertiary = VibrantAccentPink,
  onTertiary = VibrantDarkBackground,
  background = VibrantDarkBackground,
  onBackground = VibrantTextPrimary,
  surface = VibrantDarkSurface,
  onSurface = VibrantTextPrimary,
  surfaceVariant = VibrantDarkSurfaceVariant,
  onSurfaceVariant = VibrantTextSecondary,
  outline = VibrantDarkSurfaceVariant,
  outlineVariant = VibrantDarkActiveSurface
)

val VibrantLightColorScheme = lightColorScheme(
  primary = VibrantPurpleDark,
  onPrimary = VibrantTextPrimary,
  primaryContainer = VibrantPurpleLight,
  onPrimaryContainer = VibrantPurpleContainer,
  secondary = VibrantDarkSurfaceVariant,
  onSecondary = VibrantTextPrimary,
  background = VibrantDarkBackground, // Keep immersive vibrant dark feel
  onBackground = VibrantTextPrimary,
  surface = VibrantDarkSurface,
  onSurface = VibrantTextPrimary
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) VibrantDarkColorScheme else VibrantLightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

