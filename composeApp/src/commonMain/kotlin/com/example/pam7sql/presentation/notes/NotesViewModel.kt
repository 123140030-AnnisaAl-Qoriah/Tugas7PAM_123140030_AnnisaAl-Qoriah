package com.example.pam7sql.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pam7sql.data.repository.NoteRepository
import com.example.pam7sql.domain.model.Note
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface NotesUiState {
    data object Loading : NotesUiState
    data object Empty : NotesUiState
    data class Content(val notes: List<Note>) : NotesUiState
    data class Error(val message: String) : NotesUiState
}

@OptIn(FlowPreview::class)
class NotesViewModel(private val repository: NoteRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val uiState: StateFlow<NotesUiState> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) repository.getNotes()
            else repository.searchNotes(query)
        }
        .map<List<Note>, NotesUiState> { notes ->
            if (notes.isEmpty()) NotesUiState.Empty else NotesUiState.Content(notes)
        }
        .catch { e -> emit(NotesUiState.Error(e.message ?: "Unknown error")) }
        .onStart { emit(NotesUiState.Loading) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NotesUiState.Loading)

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }
}
