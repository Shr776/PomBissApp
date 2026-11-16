package com.pombiss.app.ui.theme

import android.content.Context
import androidx.compose.ui.graphics.Color

data class PomTheme(
    val id: String,
    val name: String,
    val primary: Color,
    val accent: Color
)

object ThemeManager {

    val themes = listOf(
        PomTheme("blue", "Ocean Blue", OceanBlue_Primary, OceanBlue_Accent),
        PomTheme("crimson", "Crimson", Crimson_Primary, Crimson_Accent),
        PomTheme("turquoise", "Turquoise", Turquoise_Primary, Turquoise_Accent),
        PomTheme("jade", "Jade Green", Jade_Primary, Jade_Accent),
        PomTheme("violet", "Violet", Violet_Primary, Violet_Accent),
        PomTheme("gold", "Amber Gold", Gold_Primary, Gold_Accent),
        PomTheme("sunset", "Sunset Orange", Sunset_Primary, Sunset_Accent),
        PomTheme("rose", "Rose Pink", Rose_Primary, Rose_Accent),
        PomTheme("graphite", "Graphite", Graphite_Primary, Graphite_Accent),
        PomTheme("lime", "Lime Neon", Lime_Primary, Lime_Accent)
    )

    // ذخیره تم انتخابی توی حافظه گوشی
    fun saveTheme(context: Context, themeId: String) {
        val prefs = context.getSharedPreferences("PomBissPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("selected_theme", themeId).apply()
    }

    // خوندن تم انتخابی
    fun getSelectedTheme(context: Context): PomTheme {
        val prefs = context.getSharedPreferences("PomBissPrefs", Context.MODE_PRIVATE)
        val selectedId = prefs.getString("selected_theme", "blue") ?: "blue"
        return themes.find { it.id == selectedId } ?: themes[0]
    }
}
