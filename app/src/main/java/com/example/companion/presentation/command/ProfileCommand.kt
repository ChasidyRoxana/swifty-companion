package com.example.companion.presentation.command

sealed class ProfileCommand : BaseCommand {
    object NavigateToPreviousScreen : ProfileCommand()
}