package com.example.companion.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.companion.R
import com.example.companion.databinding.FragmentSearchBinding
import com.example.companion.presentation.command.SearchCommand
import com.example.companion.presentation.model.SearchScreenState
import com.example.companion.presentation.viewmodel.SearchViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchScreenState, SearchCommand, SearchViewModel>(
    R.layout.fragment_search,
    SearchViewModel::class.java
) {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    private lateinit var dialog: MaterialAlertDialogBuilder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupDialog()
    }

    private fun setupView() {
        binding.bSearch.setOnClickListener {
            viewModel.onSearchClicked()
        }
        binding.etUserLogin.doAfterTextChanged { text -> viewModel.onTextChanged(text) }
        binding.etUserLogin.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.onSearchClicked()
                true
            } else {
                false
            }
        }
    }

    private fun setupDialog() {
        dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.title_empty_login)
            .setPositiveButton(R.string.text_ok, null)
            .setOnDismissListener { viewModel.onDialogDismissed() }
    }

    override fun renderView(model: SearchScreenState) {
        if (model.isErrorDialogVisible) {
            showErrorLoginDialog()
        }
    }

    override fun executeCommand(command: SearchCommand) {
        when (command) {
            is SearchCommand.ClearFocusAndCloseKeyboard -> clearFocusAndCloseKeyboard()
            is SearchCommand.OpenProfileScreen -> openProfileScreen(command.login)
        }
    }

    private fun clearFocusAndCloseKeyboard() {
        activity?.let { activity ->
            binding.etUserLogin.clearFocus()
            val inputMethodService = activity.getSystemService(Context.INPUT_METHOD_SERVICE)
            val inputMethodManager = inputMethodService as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.etUserLogin.windowToken, 0)
        }
    }

    private fun showErrorLoginDialog() {
        dialog.show()
    }

    private fun openProfileScreen(login: String) {
        val args = ProfileFragmentArgs(login).toBundle()
        navController.navigate(R.id.action_searchFragment_to_profileFragment, args)
    }
}