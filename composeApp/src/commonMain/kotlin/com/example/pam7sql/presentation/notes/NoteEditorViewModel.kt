package com.example.pam7sql.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pam7sql.data.repository.NoteRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface EditorUiState {
    data object Idle : EditorUiState
    data object Loading : EditorUiState
    data object Saved : EditorUiState
    data class Error(val message: String) : EditorUiState
}

data class NoteEditorState(
    val title: String = "",
    val content: String = "",
    val isEditMode: Boolean = false,
    val noteId: Long = 0L
)

class NoteEditorViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _editorState = MutableStateFlow(NoteEditorState())
    val editorState: StateFlow<NoteEditorState> = _editorState.asStateFlow()

    private val _uiState = MutableStateFlow<EditorUiState>(EditorUiState.Idle)
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    fun loadNote(id: Long) {
        viewModelScope.launch {
            _uiState.value = EditorUiState.Loading
            try {
                val note = repository.getNoteById(id)
                if (note != null) {
                    _editorState.value = NoteEditorState(
                        title = note.title,
                        content = note.content,
                        isEditMode = true,
                        noteId = note.id
                    )
                }
                _uiState.value = EditorUiState.Idle
            } catch (e: Exception) {
                _uiState.value = EditorUiState.Error(e.message ?: "Failed to load note")
            }
        }
    }

    fun onTitleChange(title: String) {
        _editorState.update { it.copy(title = title) }
    }

    fun onContentChange(content: String) {
        _editorState.update { it.copy(content = content) }
    }

    fun saveNote() {
        val state = _editorState.value
        if (state.title.isBlank() && state.content.isBlank()) {
            _uiState.value = EditorUiState.Error("Title or content cannot be empty")
            return
        }
        viewModelScope.launch {
            _uiState.value = EditorUiState.Loading
            try {
                if (state.isEditMode) {
                    repository.updateNote(state.noteId, state.title, state.content)
                } else {
                    repository.addNote(state.title, state.content)
                }
                _uiState.value = EditorUiState.Saved
            } catch (e: Exception) {
                _uiState.value = EditorUiState.Error(e.message ?: "Failed to save note")
            }
        }
    }
}
