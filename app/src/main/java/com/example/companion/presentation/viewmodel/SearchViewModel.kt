package com.example.companion.presentation.viewmodel

import android.text.Editable
import com.example.companion.presentation.command.SearchCommand
import com.example.companion.presentation.model.SearchScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : BaseViewModel<SearchScreenState, SearchCommand>(
    SearchScreenState()
) {

    private fun updateScreenState(
        login: String = model.login,
        isErrorDialogVisible: Boolean = model.isErrorDialogVisible,
        shouldRefreshView: Boolean = true
    ) {
        model = SearchScreenState(login = login, isErrorDialogVisible = isErrorDialogVisible)
        if (shouldRefreshView) {
            refreshView()
        }
    }

    fun onTextChanged(enteredText: Editable?) {
        val login = enteredText?.toString() ?: ""
        updateScreenState(login = login, shouldRefreshView = false)
    }

    fun onSearchClicked() {
        executeCommand(SearchCommand.ClearFocusAndCloseKeyboard)
        if (model.login.isNotBlank()) {
            executeCommand(SearchCommand.OpenProfileScreen(model.login))
        } else {
            updateScreenState(isErrorDialogVisible = true)
        }
    }

    fun onDialogDismissed() {
        updateScreenState(isErrorDialogVisible = false)
    }
}