package com.example.pam7sql

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pam7sql.data.local.DatabaseDriverFactory
import com.example.pam7sql.di.AppContainer
import com.example.pam7sql.di.initSettings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initSettings(applicationContext)
        val appContainer = AppContainer(DatabaseDriverFactory(applicationContext))
        setContent {
            App(appContainer)
        }
    }
}
