package com.example.companion.domain.model

class Project(
    val name: String,
    val status: Status,
    val validated: Boolean?,
    val finalMark: Long?,
    val hasParent: Boolean,
    val coursesIds: List<Int>,
)