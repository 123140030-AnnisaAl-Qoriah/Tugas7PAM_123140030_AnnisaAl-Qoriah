package com.example.pam7sql

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pam7sql.data.local.SettingsManager
import com.example.pam7sql.di.AppContainer
import com.example.pam7sql.presentation.notes.NoteEditorViewModel
import com.example.pam7sql.presentation.notes.NoteEditorScreen
import com.example.pam7sql.presentation.notes.NotesListScreen
import com.example.pam7sql.presentation.notes.NotesViewModel
import com.example.pam7sql.presentation.settings.SettingsScreen
import com.example.pam7sql.presentation.settings.SettingsViewModel

// Simple in-memory navigation
sealed interface Screen {
    data object NotesList : Screen
    data class NoteEditor(val noteId: Long? = null) : Screen
    data object Settings : Screen
}

// Pink Pastel Color Palette
private val PinkPrimary = Color(0xFFE91E8C)
private val PinkPastelLight = Color(0xFFF8BBD9)
private val PinkPastelLighter = Color(0xFFFCE4EC)
private val PinkPastelSurface = Color(0xFFFFF0F5)
private val PinkDeep = Color(0xFFC2185B)
private val PinkAccent = Color(0xFFFF80AB)
private val PinkContainer = Color(0xFFFFD6E8)
private val PinkOnContainer = Color(0xFF880E4F)
private val PinkSecondary = Color(0xFFFF6F9C)
private val PinkSecondaryContainer = Color(0xFFFFE0EE)
private val SoftPurple = Color(0xFFCE93D8)
private val PurpleContainer = Color(0xFFF3E5F5)
private val White = Color(0xFFFFFFFF)
private val DarkText = Color(0xFF4A1942)
private val SubtleText = Color(0xFF9C4D7A)
private val OutlineColor = Color(0xFFE8A0C0)

private val PinkLightColorScheme = lightColorScheme(
    primary = PinkPrimary,
    onPrimary = White,
    primaryContainer = PinkContainer,
    onPrimaryContainer = PinkOnContainer,
    secondary = PinkSecondary,
    onSecondary = White,
    secondaryContainer = PinkSecondaryContainer,
    onSecondaryContainer = Color(0xFF7B1450),
    tertiary = SoftPurple,
    onTertiary = White,
    tertiaryContainer = PurpleContainer,
    onTertiaryContainer = Color(0xFF4A0072),
    background = PinkPastelSurface,
    onBackground = DarkText,
    surface = White,
    onSurface = DarkText,
    surfaceVariant = PinkPastelLighter,
    onSurfaceVariant = SubtleText,
    outline = OutlineColor,
    error = Color(0xFFE53935),
    onError = White
)

private val PinkDarkColorScheme = darkColorScheme(
    primary = PinkAccent,
    onPrimary = Color(0xFF5C0035),
    primaryContainer = PinkDeep,
    onPrimaryContainer = PinkPastelLight,
    secondary = Color(0xFFFFB3C8),
    onSecondary = Color(0xFF68003F),
    secondaryContainer = Color(0xFF8F1858),
    onSecondaryContainer = Color(0xFFFFD9E4),
    background = Color(0xFF1E1218),
    onBackground = Color(0xFFFFD6E8),
    surface = Color(0xFF251620),
    onSurface = Color(0xFFFFD6E8),
    surfaceVariant = Color(0xFF3D2030),
    onSurfaceVariant = Color(0xFFE8B4CB),
    outline = Color(0xFF9E7085),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun App(appContainer: AppContainer) {
    val themeFlow = remember { appContainer.settingsManager.themeFlow }
    val theme by themeFlow.collectAsState(initial = SettingsManager.THEME_SYSTEM)

    val colorScheme = when (theme) {
        SettingsManager.THEME_DARK -> PinkDarkColorScheme
        SettingsManager.THEME_LIGHT -> PinkLightColorScheme
        else -> if (isSystemInDarkTheme()) PinkDarkColorScheme else PinkLightColorScheme
    }

    MaterialTheme(colorScheme = colorScheme) {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.NotesList) }

        when (val screen = currentScreen) {
            is Screen.NotesList -> {
                val vm = viewModel {
                    NotesViewModel(appContainer.noteRepository)
                }
                NotesListScreen(
                    viewModel = vm,
                    onAddNote = { currentScreen = Screen.NoteEditor(null) },
                    onEditNote = { id -> currentScreen = Screen.NoteEditor(id) },
                    onSettingsClick = { currentScreen = Screen.Settings }
                )
            }
            is Screen.NoteEditor -> {
                val vm = viewModel(key = "editor_${screen.noteId}") {
                    NoteEditorViewModel(appContainer.noteRepository)
                }
                NoteEditorScreen(
                    viewModel = vm,
                    noteId = screen.noteId,
                    onNavigateBack = { currentScreen = Screen.NotesList }
                )
            }
            is Screen.Settings -> {
                val vm = viewModel {
                    SettingsViewModel(appContainer.settingsManager)
                }
                SettingsScreen(
                    viewModel = vm,
                    onNavigateBack = { currentScreen = Screen.NotesList }
                )
            }
        }
    }
}

// expect/actual for system dark theme detection
@Composable
expect fun isSystemInDarkTheme(): Boolean
