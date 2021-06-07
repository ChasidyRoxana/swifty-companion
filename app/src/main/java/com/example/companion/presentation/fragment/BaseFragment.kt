package com.example.companion.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.companion.presentation.command.BaseCommand
import com.example.companion.presentation.model.BaseScreenState
import com.example.companion.presentation.viewmodel.BaseViewModel

abstract class BaseFragment<
        ScreenState : BaseScreenState,
        Command : BaseCommand,
        ViewModel : BaseViewModel<ScreenState, Command>>(
    @LayoutRes layoutRes: Int,
    viewModelClass: Class<ViewModel>
) : Fragment(layoutRes) {

    protected lateinit var navController: NavController
        private set
    protected val viewModel: ViewModel by lazy { ViewModelProvider(this).get(viewModelClass) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        setupViewModelObservers()
    }

    private fun setupViewModelObservers() {
        viewModel.modelEvent.observe(viewLifecycleOwner, Observer(::renderView))
        viewModel.commandEvent.observe(viewLifecycleOwner, Observer(::executeCommand))
    }

    protected abstract fun renderView(model: ScreenState)

    protected abstract fun executeCommand(command: Command)
}