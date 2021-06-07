package com.example.companion.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.companion.presentation.command.BaseCommand
import com.example.companion.presentation.model.BaseScreenState
import com.example.companion.presentation.model.SingleEventLiveData

abstract class BaseViewModel<
        ScreenState : BaseScreenState,
        Command : BaseCommand>(
    initState: ScreenState
) : ViewModel() {

    protected var model: ScreenState = initState

    private val modelLiveData = MutableLiveData<ScreenState>()
    private val commandLiveData = SingleEventLiveData<Command>()

    val modelEvent: LiveData<ScreenState>
        get() = modelLiveData
    val commandEvent: LiveData<Command>
        get() = commandLiveData

    protected fun refreshView() {
        modelLiveData.value = model
    }

    protected fun executeCommand(command: Command) {
        commandLiveData.value = command
    }
}