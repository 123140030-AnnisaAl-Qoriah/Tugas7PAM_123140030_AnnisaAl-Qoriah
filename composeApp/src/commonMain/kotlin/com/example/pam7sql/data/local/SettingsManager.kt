package com.example.pam7sql.data.local

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.flow.Flow

class SettingsManager(settings: ObservableSettings) {

    private val flowSettings: FlowSettings = settings.toFlowSettings()

    companion object {
        const val KEY_THEME = "app_theme"
        const val KEY_SORT_ORDER = "sort_order"

        const val THEME_SYSTEM = "system"
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"

        const val SORT_UPDATED = "updated"
        const val SORT_CREATED = "created"
        const val SORT_TITLE = "title"
    }

    val themeFlow: Flow<String> = flowSettings.getStringFlow(KEY_THEME, THEME_SYSTEM)
    val sortOrderFlow: Flow<String> = flowSettings.getStringFlow(KEY_SORT_ORDER, SORT_UPDATED)

    suspend fun setTheme(theme: String) {
        flowSettings.putString(KEY_THEME, theme)
    }

    suspend fun setSortOrder(order: String) {
        flowSettings.putString(KEY_SORT_ORDER, order)
    }
}
