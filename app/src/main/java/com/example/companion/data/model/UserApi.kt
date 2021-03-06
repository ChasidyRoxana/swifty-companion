package com.example.companion.data.model

import com.google.gson.annotations.SerializedName

class UserApi(
    val image_url: String,
    val email: String,
    val displayname: String,
    @SerializedName("staff?")
    val staff: Boolean,
    val correction_point: Int,
    val wallet: Int,
    val cursus_users: List<CourseApi>,
    val projects_users: List<ProjectApi>,
    val campus: List<CampusApi>,
)