package com.example.companion.presentation.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.core.view.isVisible
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

        if (savedInstanceState == null) {
            val uriData: String? = activity?.intent?.data?.toString()
            viewModel.init(uriData)
        }
        setupView()
        setupDialog()
    }

    private fun setupView() {
        with(binding) {
            bSearch.setOnClickListener {
                viewModel.onSearchClicked()
            }
            etUserLogin.doAfterTextChanged { text -> viewModel.onTextChanged(text) }
            etUserLogin.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.onSearchClicked()
                    true
                } else {
                    false
                }
            }
            bLogIn.setOnClickListener {
                openAuthorization()
            }
        }
    }

    private fun openAuthorization() {
        val link = URL_AUTH
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        activity?.startActivity(browserIntent)
    }

    private fun setupDialog() {
        dialog = MaterialAlertDialogBuilder(requireContext())
            .setPositiveButton(R.string.text_ok, null)
            .setOnDismissListener { viewModel.onDialogDismissed() }
    }

    override fun renderView(model: SearchScreenState) {
        when {
            model.isErrorEmptyLoginVisible -> showErrorDialog(R.string.title_error_empty_login)
            model.isErrorAuthorizationVisible -> showErrorDialog(R.string.title_error_authorization)
        }
        with(binding) {
            bLogIn.isVisible = !model.isAuthorized
            cvUserLogin.isVisible = model.isAuthorized
            bSearch.isVisible = model.isAuthorized
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

    private fun showErrorDialog(@StringRes errorMessageRes: Int) {
        dialog.setTitle(errorMessageRes).show()
    }

    private fun openProfileScreen(login: String) {
        val args = ProfileFragmentArgs(login).toBundle()
        navController.navigate(R.id.action_searchFragment_to_profileFragment, args)
    }

    companion object {
        private const val URL_AUTH =
            "https://api.intra.42.fr/oauth/authorize?client_id=d1c70c9241f1b831db9beed6f31037caec861db66847fb090597777f64671f6a&redirect_uri=http%3A%2F%2Fwww.example.com%2Fmyapp&response_type=code"
    }
}