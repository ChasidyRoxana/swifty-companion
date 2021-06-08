package com.example.companion.presentation.model

import androidx.annotation.StringRes

class ProfileScreenState(
    val login: String = "",
    val isLoading: Boolean = false,
    val currentCourseId: Int = 21,
    val profile: Profile? = null,
    val error: Throwable? = null,
    @StringRes val errorMessageRes: Int? = null
) : BaseScreenState {
    val isErrorVisible: Boolean = error != null && !isLoading
    val isProfileInfoVisible: Boolean = profile != null
}