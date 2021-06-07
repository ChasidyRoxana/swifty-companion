package com.example.companion.data.model

class CourseApi(
    val level: Double,
    val skills: List<SkillApi>,
    val cursus_id: Int,
    val cursus: CourseDetailApi,
)