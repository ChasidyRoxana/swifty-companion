package com.example.companion.presentation.viewmodel

import com.example.companion.data.network.RequestListener
import com.example.companion.data.repository.Repository
import com.example.companion.domain.model.User
import com.example.companion.presentation.command.ProfileCommand
import com.example.companion.presentation.mapper.ProfileMapper
import com.example.companion.presentation.model.Profile
import com.example.companion.presentation.model.ProfileScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
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
        shouldRefreshView: Boolean = true
    ) {
        model = ProfileScreenState(
            login = login,
            isLoading = isLoading,
            currentCourseId = currentCourseId,
            profile = profile,
            error = error
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
        fetchUserProfile()
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
                updateScreenState(error = t)
            }

            override fun onSuccess(data: User) {
                val profile = mapper.mapToProfile(data)
                val currentCourseId =
                    if (profile.courses.isNotEmpty()) {
                        val course = profile.courses.first()
                        course.isCurrentCourse = true
                        course.id
                    } else {
                        0
                    }
                updateScreenState(
                    currentCourseId = currentCourseId,
                    profile = profile,
                    error = null
                )
            }
        }
}