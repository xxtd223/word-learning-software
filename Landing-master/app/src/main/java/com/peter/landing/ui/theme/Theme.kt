package com.peter.landing.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.peter.landing.data.util.ThemeMode
import com.google.accompanist.systemuicontroller.rememberSystemUiController


private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,



)


private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)
private val EyeCareColors = lightColorScheme(
    primary = Color(0xFFFFC107),  // 温暖的黄色
    onPrimary = Color.Black,      // 文字颜色
    background = Color(0xFFFFF8E1),  // 浅黄色背景
    onBackground = Color.Black,       // 背景上的文字颜色
    surface = Color(0xFFFFF8E1),      // 表面颜色
    onSurface = Color.Black,          // 表面上的文字颜色
    // 可以为其他颜色元素也设置类似的温暖色调
)
// **彩色模式（活力模式 Vibrant）**
private val VibrantColors = lightColorScheme(
    primary = Color(0xFF2196F3),  // 亮蓝色
    onPrimary = Color.White,
    background = Color(0xFFE3F2FD),  // 浅蓝色背景
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    secondary = Color(0xFFFF9800),  // 橙色
    onSecondary = Color.White
)
private val DefaultColors = lightColorScheme(
    primary = Color(0xFF9E9E9E),  // 中性灰
    onPrimary = Color.Black,
    background = Color(0xFFF5F5F5),
    onBackground = Color.Black,
    surface = Color(0xFFE0E0E0),
    onSurface = Color.Black
)





@Composable
fun LandingAppTheme(
    themeMode: ThemeMode = ThemeMode.DEFAULT,  // 使用 ThemeMode 枚举
    content: @Composable () -> Unit
) {
    // 选择颜色方案
    val colors = when (themeMode) {
        ThemeMode.LIGHT -> LightColors
        ThemeMode.DARK -> DarkColors
        ThemeMode.EYE_CARE -> EyeCareColors
        ThemeMode.VIBRANT -> VibrantColors
        ThemeMode.DEFAULT -> if (isSystemInDarkTheme()) DarkColors else DefaultColors
    }

    // 设置系统 UI 颜色
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = themeMode == ThemeMode.LIGHT || themeMode == ThemeMode.DEFAULT || themeMode == ThemeMode.VIBRANT
    )

    // 应用 MaterialTheme
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}


