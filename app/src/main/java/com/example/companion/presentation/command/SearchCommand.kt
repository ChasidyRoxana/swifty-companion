package com.example.companion.presentation.command

sealed class SearchCommand : BaseCommand {
    object ClearFocusAndCloseKeyboard : SearchCommand()
    class OpenProfileScreen(val login: String) : SearchCommand()
}