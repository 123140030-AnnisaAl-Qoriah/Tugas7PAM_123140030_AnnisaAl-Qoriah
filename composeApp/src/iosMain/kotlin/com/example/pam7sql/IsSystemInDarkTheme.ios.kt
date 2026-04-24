package com.example.pam7sql

import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme as composeIsSystemInDarkTheme

@Composable
actual fun isSystemInDarkTheme(): Boolean = composeIsSystemInDarkTheme()
