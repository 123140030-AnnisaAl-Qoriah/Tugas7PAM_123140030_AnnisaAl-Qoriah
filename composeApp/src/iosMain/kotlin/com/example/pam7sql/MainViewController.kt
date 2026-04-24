package com.example.pam7sql

import androidx.compose.ui.window.ComposeUIViewController
import com.example.pam7sql.data.local.DatabaseDriverFactory
import com.example.pam7sql.di.AppContainer

fun MainViewController() = ComposeUIViewController {
    val appContainer = AppContainer(DatabaseDriverFactory())
    App(appContainer)
}
