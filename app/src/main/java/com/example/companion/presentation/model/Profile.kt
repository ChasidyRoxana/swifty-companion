package com.example.companion.presentation.model

class Profile(
    val imageUrl: String,
    val name: String,
    val email: String,
    val staff: Boolean,
    val correctionPoint: String,
    val wallet: String,
    val city: String,
    val courses: List<UserCourse>,
) {
    val isCorrectionPointVisible = !staff
    val isWalletVisible = !staff
    val isCoursesVisible = courses.isNotEmpty()
}