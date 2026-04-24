package com.example.pam7sql.di

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import platform.Foundation.NSUserDefaults

actual fun createObservableSettings(): ObservableSettings {
    return NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
}
