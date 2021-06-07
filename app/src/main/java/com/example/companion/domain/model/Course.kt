package com.example.companion.domain.model

class Course(
    val id: Int,
    val name: String,
    val level: Double,
    val skills: List<Skill>,
)