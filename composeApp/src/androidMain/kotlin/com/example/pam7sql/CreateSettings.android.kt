package com.example.pam7sql.di

import android.content.Context
import android.content.SharedPreferences
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings

// Android uses SharedPreferencesSettings which IS ObservableSettings
private lateinit var appContext: Context

fun initSettings(context: Context) {
    appContext = context.applicationContext
}

actual fun createObservableSettings(): ObservableSettings {
    val prefs: SharedPreferences = appContext.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return SharedPreferencesSettings(prefs)
}
