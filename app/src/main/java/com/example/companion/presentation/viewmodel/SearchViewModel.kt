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

    fun onTextChanged(enteredText: Editable?) {
        model.login = enteredText?.toString() ?: ""
    }

    fun onSearchClicked() {
        executeCommand(SearchCommand.ClearFocusAndCloseKeyboard)
        if (model.login.isNotBlank()) {
            executeCommand(SearchCommand.OpenProfileScreen(model.login))
        } else {
            executeCommand(SearchCommand.ShowErrorLoginDialog)
        }
    }
}