package com.example.companion.presentation.model

class UserCourse(
    val id: Int,
    val name: String,
    val isLevelVisible: Boolean,
    val level: String,
    val levelProgress: Int,
    val skills: List<UserSkill>,
    val projects: List<UserProject>
) {
    val isProjectVisible: Boolean = projects.isNotEmpty()
    val isSkillVisible: Boolean = skills.isNotEmpty()
    var isCurrentCourse: Boolean = false
}