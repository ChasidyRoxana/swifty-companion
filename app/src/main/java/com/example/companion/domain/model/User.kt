package com.example.companion.domain.model

class User(
    val name: String,
    val email: String,
    val staff: Boolean,
    val correctionPoint: Int,
    val wallet: Int,
    val city: String,
    val courses: List<Course>,
    val projects: List<Project>,
)