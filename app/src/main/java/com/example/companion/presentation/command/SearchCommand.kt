package com.example.companion.presentation.command

sealed class SearchCommand : BaseCommand {
    object ClearFocusAndCloseKeyboard : SearchCommand()
    object OpenAuthorization : SearchCommand()
    class OpenProfileScreen(val login: String) : SearchCommand()
}