package com.example.companion.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.companion.R
import com.example.companion.databinding.FragmentProfileBinding
import com.example.companion.presentation.adapter.CourseAdapter
import com.example.companion.presentation.adapter.ProjectAdapter
import com.example.companion.presentation.adapter.SkillAdapter
import com.example.companion.presentation.command.ProfileCommand
import com.example.companion.presentation.model.*
import com.example.companion.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileScreenState, ProfileCommand, ProfileViewModel>(
    R.layout.fragment_profile,
    ProfileViewModel::class.java
) {

    private val args by navArgs<ProfileFragmentArgs>()
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private lateinit var courseAdapter: CourseAdapter
    private val projectAdapter = ProjectAdapter()
    private val skillAdapter = SkillAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.init(args.login)
        }
        setupAppBar(args.login)
        setupView()
        setupAdapters()
    }

    private fun setupAppBar(login: String) {
        with(binding.appBar.mtAppBar) {
            title = login
            setNavigationOnClickListener { viewModel.onNavigationBackClicked() }
        }
    }

    private fun setupView() =
        with(binding) {
            layoutError.bTryAgain.setOnClickListener { viewModel.onTryAgainClicked() }
        }

    private fun setupAdapters() {
        with(binding) {
            courseAdapter = CourseAdapter(viewModel::onCourseClicked)
            rvCourses.adapter = courseAdapter
            rvProjects.adapter = projectAdapter
            rvSkills.adapter = skillAdapter
        }
    }

    override fun renderView(model: ProfileScreenState) {
        with(binding) {
            cvProgressBar.isVisible = model.isLoading
            layoutError.clError.isVisible = model.isErrorVisible
            model.errorMessageRes?.let {
                layoutError.tvErrorMessage.setText(it)
            }
        }
        updateUserInfo(model)
    }

    override fun executeCommand(command: ProfileCommand) {
        when (command) {
            is ProfileCommand.NavigateToPreviousScreen -> navigateToPreviousScreen()
        }
    }

    private fun updateUserInfo(model: ProfileScreenState) {
        model.profile?.let { profile ->
            with(binding) {
                cvUserInfo.isVisible = model.isProfileInfoVisible
                with(layoutUserInfo) {
                    updateUserImage(profile.imageUrl)
                    tvStaffBadge.isVisible = profile.staff
                    tvName.text = profile.name
                    tvMail.text = profile.email
                    tvCity.text = profile.city
                    tvWallet.isVisible = profile.isWalletVisible
                    tvWalletCount.isVisible = profile.isWalletVisible
                    tvWalletCount.text = profile.wallet
                    tvCorrection.isVisible = profile.isCorrectionPointVisible
                    tvCorrectionCount.isVisible = profile.isCorrectionPointVisible
                    tvCorrectionCount.text = profile.correctionPoint
                }
            }
            updateCourses(profile)
            val course = profile.courses.find { it.id == model.currentCourseId }
            updateCourseInfo(course)
        }
    }

    private fun updateUserImage(imageUrl: String) {
        Glide.with(requireContext())
            .load(imageUrl)
            .centerCrop()
            .error(R.drawable.ic_pizza)
            .into(binding.layoutUserInfo.ivImage)
    }

    private fun updateCourseInfo(course: UserCourse?) {
        course?.let {
            with(binding.layoutUserInfo) {
                tvLevel.isVisible = course.isLevelVisible
                tvLevel.text = getString(R.string.text_level, course.level)
                pbLevel.isVisible = course.isLevelVisible
                pbLevel.progress = course.levelProgress
            }
            updateProject(course.projects, course.isProjectVisible)
            updateSkill(course.skills, course.isSkillVisible)
        }
    }

    private fun updateCourses(profile: Profile) {
        with(binding) {
            tvCourses.isVisible = profile.isCoursesVisible
            cvCourses.isVisible = profile.isCoursesVisible
        }
        courseAdapter.setCourses(profile.courses)
        courseAdapter.notifyDataSetChanged()
    }

    private fun updateProject(projects: List<UserProject>, isProjectVisible: Boolean) {
        with(binding) {
            tvProjects.isVisible = isProjectVisible
            cvUserProjects.isVisible = isProjectVisible
        }
        projectAdapter.setProjects(projects)
        projectAdapter.notifyDataSetChanged()
    }

    private fun updateSkill(skills: List<UserSkill>, isSkillVisible: Boolean) {
        with(binding) {
            tvSkills.isVisible = isSkillVisible
            cvUserSkills.isVisible = isSkillVisible
        }
        skillAdapter.setSkills(skills)
        skillAdapter.notifyDataSetChanged()
    }

    private fun navigateToPreviousScreen() {
        navController.popBackStack()
    }
}