package com.example.pam7sql.di

import com.example.pam7sql.data.local.DatabaseDriverFactory
import com.example.pam7sql.data.local.NoteLocalDataSource
import com.example.pam7sql.data.local.SettingsManager
import com.example.pam7sql.data.repository.NoteRepository
import com.russhwolf.settings.ObservableSettings

expect fun createObservableSettings(): ObservableSettings

class AppContainer(driverFactory: DatabaseDriverFactory) {
    val settingsManager = SettingsManager(createObservableSettings())
    private val localDataSource = NoteLocalDataSource(driverFactory)
    val noteRepository = NoteRepository(localDataSource, settingsManager)
}
