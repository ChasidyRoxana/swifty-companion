package com.example.companion.presentation.viewmodel

import androidx.annotation.StringRes
import com.example.companion.R
import com.example.companion.data.model.EmptyResponseBody
import com.example.companion.data.network.RequestListener
import com.example.companion.data.repository.Repository
import com.example.companion.domain.model.User
import com.example.companion.presentation.command.ProfileCommand
import com.example.companion.presentation.mapper.ProfileMapper
import com.example.companion.presentation.model.Profile
import com.example.companion.presentation.model.ProfileScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository,
    private val mapper: ProfileMapper
) : BaseViewModel<ProfileScreenState, ProfileCommand>(
    ProfileScreenState()
) {

    private val requestListener = createRequestListener()

    fun init(login: String) {
        updateScreenState(login = login)
        repository.setRequestListener(requestListener)
        fetchUserProfile()
    }

    override fun onCleared() {
        super.onCleared()
        repository.clearRequestListener()
    }

    @Synchronized
    private fun updateScreenState(
        login: String = model.login,
        isLoading: Boolean = model.isLoading,
        currentCourseId: Int = model.currentCourseId,
        profile: Profile? = model.profile,
        error: Throwable? = model.error,
        @StringRes errorMessageRes: Int? = model.errorMessageRes,
        shouldRefreshView: Boolean = true
    ) {
        model = ProfileScreenState(
            login = login,
            isLoading = isLoading,
            currentCourseId = currentCourseId,
            profile = profile,
            error = error,
            errorMessageRes = errorMessageRes
        )
        if (shouldRefreshView) {
            refreshView()
        }
    }

    fun onCourseClicked(courseId: Int) {
        model.profile?.let { profile ->
            val previousCourse = profile.courses.find { it.isCurrentCourse }
            previousCourse?.isCurrentCourse = false
            val currentCourse = profile.courses.find { it.id == courseId }
            currentCourse?.isCurrentCourse = true
            updateScreenState(currentCourseId = courseId)
        }
    }

    fun onTryAgainClicked() {
        if (model.error is EmptyResponseBody) {
            executeCommand(ProfileCommand.NavigateToPreviousScreen)
        } else {
            fetchUserProfile()
        }
    }

    fun onNavigationBackClicked() {
        executeCommand(ProfileCommand.NavigateToPreviousScreen)
    }

    private fun fetchUserProfile() {
        repository.getUserProfile(model.login)
    }

    private fun createRequestListener() =
        object : RequestListener<User> {
            override fun onLoading(isLoading: Boolean) {
                updateScreenState(isLoading = isLoading)
            }

            override fun onError(t: Throwable) {
                @StringRes val errorMessageRes = when (t) {
                    is UnknownHostException -> R.string.title_error_loading
                    is EmptyResponseBody -> R.string.title_error_empty_body
                    else -> R.string.title_error_other
                }
                updateScreenState(error = t, errorMessageRes = errorMessageRes)
            }

            override fun onSuccess(data: User, action: (() -> Unit)?) {
                val profile = mapper.mapToProfile(data)
                val course = profile.courses.firstOrNull()
                course?.isCurrentCourse = true
                val currentCourseId: Int = course?.id ?: 0
                updateScreenState(
                    currentCourseId = currentCourseId,
                    profile = profile,
                    error = null
                )
            }
        }
}