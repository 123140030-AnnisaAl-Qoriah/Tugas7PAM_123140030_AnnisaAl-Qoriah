package com.example.pam7sql.domain.model

data class Note(
    val id: Long = 0L,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long
)
