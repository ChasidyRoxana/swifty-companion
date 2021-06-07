package com.example.companion.presentation.model

class UserSkill(
    val name: String,
    val level: String,
    val isTopSkill: Boolean,
    isFirstSkill: Boolean
) {
    val isDelimiterVisible = !isFirstSkill
}