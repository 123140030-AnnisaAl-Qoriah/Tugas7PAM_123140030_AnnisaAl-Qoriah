package com.example.pam7sql.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.pam7sql.db.NotesDatabase
import com.example.pam7sql.domain.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import com.example.pam7sql.db.NoteEntity

class NoteLocalDataSource(driverFactory: DatabaseDriverFactory) {

    private val database = NotesDatabase(driverFactory.createDriver())
    private val queries = database.noteQueries

    fun getAllNotes(): Flow<List<Note>> =
        queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toDomain() } }

    fun searchNotes(query: String): Flow<List<Note>> =
        queries.search(query, query)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toDomain() } }

    suspend fun getNoteById(id: Long): Note? = withContext(Dispatchers.Default) {
        queries.selectById(id).executeAsOneOrNull()?.toDomain()
    }

    suspend fun insertNote(title: String, content: String, now: Long) =
        withContext(Dispatchers.Default) {
            queries.insert(title, content, now, now)
        }

    suspend fun updateNote(id: Long, title: String, content: String, now: Long) =
        withContext(Dispatchers.Default) {
            queries.update(title, content, now, id)
        }

    suspend fun deleteNote(id: Long) = withContext(Dispatchers.Default) {
        queries.deleteById(id)
    }

    private fun NoteEntity.toDomain() = Note(
        id = id,
        title = title,
        content = content,
        createdAt = created_at,
        updatedAt = updated_at
    )
}
