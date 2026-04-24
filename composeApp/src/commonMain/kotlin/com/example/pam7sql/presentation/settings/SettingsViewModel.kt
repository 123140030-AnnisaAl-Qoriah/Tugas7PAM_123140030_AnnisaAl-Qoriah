package com.example.pam7sql.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pam7sql.data.local.SettingsManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsManager: SettingsManager) : ViewModel() {

    val currentTheme: StateFlow<String> = settingsManager.themeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsManager.THEME_SYSTEM)

    val currentSortOrder: StateFlow<String> = settingsManager.sortOrderFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsManager.SORT_UPDATED)

    fun changeTheme(theme: String) {
        viewModelScope.launch { settingsManager.setTheme(theme) }
    }

    fun changeSortOrder(order: String) {
        viewModelScope.launch { settingsManager.setSortOrder(order) }
    }
}
