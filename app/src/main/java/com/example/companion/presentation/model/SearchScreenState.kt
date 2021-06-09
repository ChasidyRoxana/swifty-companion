package com.example.companion.presentation.model

class SearchScreenState(
    val login: String = "",
    val isAuthorized: Boolean = false,
    val isErrorEmptyLoginVisible: Boolean = false,
    val isErrorAuthorizationVisible: Boolean = false
) : BaseScreenState