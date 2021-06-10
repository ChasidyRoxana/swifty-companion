package com.example.companion.presentation.viewmodel

import android.text.Editable
import com.example.companion.data.repository.Repository
import com.example.companion.presentation.command.SearchCommand
import com.example.companion.presentation.model.SearchScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel<SearchScreenState, SearchCommand>(
    SearchScreenState()
) {

    fun init(uriData: String?) {
        uriData?.let {
            val isErrorAuthorization: Boolean = uriData.contains("access_denied")
            if (isErrorAuthorization) {
                updateScreenState(isErrorAuthorizationVisible = true)
            } else {
                val code = uriData.substringAfterLast("code=")
                repository.setUserCode(code)
                updateScreenState(isAuthorized = true, isErrorAuthorizationVisible = false)
            }
        }
    }

    private fun updateScreenState(
        login: String = model.login,
        isAuthorized: Boolean = model.isAuthorized,
        isErrorEmptyLoginVisible: Boolean = model.isErrorEmptyLoginVisible,
        isErrorAuthorizationVisible: Boolean = model.isErrorAuthorizationVisible,
        shouldRefreshView: Boolean = true
    ) {
        model = SearchScreenState(
            login = login,
            isAuthorized = isAuthorized,
            isErrorEmptyLoginVisible = isErrorEmptyLoginVisible,
            isErrorAuthorizationVisible = isErrorAuthorizationVisible
        )
        if (shouldRefreshView) {
            refreshView()
        }
    }

    fun onLogInClicked() =
        executeCommand(SearchCommand.OpenAuthorization)

    fun onTextChanged(enteredText: Editable?) {
        val login = enteredText?.toString() ?: ""
        updateScreenState(login = login, shouldRefreshView = false)
    }

    fun onSearchClicked() {
        executeCommand(SearchCommand.ClearFocusAndCloseKeyboard)
        if (model.login.isNotBlank()) {
            executeCommand(SearchCommand.OpenProfileScreen(model.login))
        } else {
            updateScreenState(isErrorEmptyLoginVisible = true)
        }
    }

    fun onDialogDismissed() =
        updateScreenState(isErrorAuthorizationVisible = false, isErrorEmptyLoginVisible = false)
}