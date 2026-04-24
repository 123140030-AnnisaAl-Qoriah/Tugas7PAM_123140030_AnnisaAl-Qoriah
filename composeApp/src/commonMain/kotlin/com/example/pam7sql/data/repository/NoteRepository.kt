package com.example.pam7sql.data.repository

import com.example.pam7sql.data.local.NoteLocalDataSource
import com.example.pam7sql.data.local.SettingsManager
import com.example.pam7sql.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.Clock

class NoteRepository(
    private val localDataSource: NoteLocalDataSource,
    private val settingsManager: SettingsManager
) {
    fun getNotes(): Flow<List<Note>> = combine(
        localDataSource.getAllNotes(),
        settingsManager.sortOrderFlow
    ) { notes, sortOrder ->
        when (sortOrder) {
            SettingsManager.SORT_CREATED -> notes.sortedByDescending { it.createdAt }
            SettingsManager.SORT_TITLE -> notes.sortedBy { it.title.lowercase() }
            else -> notes.sortedByDescending { it.updatedAt }
        }
    }

    fun searchNotes(query: String): Flow<List<Note>> =
        localDataSource.searchNotes(query)

    suspend fun getNoteById(id: Long): Note? = localDataSource.getNoteById(id)

    suspend fun addNote(title: String, content: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        localDataSource.insertNote(title.trim(), content.trim(), now)
    }

    suspend fun updateNote(id: Long, title: String, content: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        localDataSource.updateNote(id, title.trim(), content.trim(), now)
    }

    suspend fun deleteNote(id: Long) = localDataSource.deleteNote(id)
}
