package com.example.pam7sql

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform