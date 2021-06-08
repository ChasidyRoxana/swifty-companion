package com.example.companion.presentation.mapper

import com.example.companion.domain.model.Course
import com.example.companion.domain.model.Project
import com.example.companion.domain.model.Skill
import com.example.companion.domain.model.User
import com.example.companion.presentation.model.Profile
import com.example.companion.presentation.model.UserCourse
import com.example.companion.presentation.model.UserProject
import com.example.companion.presentation.model.UserSkill
import javax.inject.Inject

class ProfileMapper @Inject constructor() {

    fun mapToProfile(user: User): Profile =
        Profile(
            imageUrl = user.imageUrl,
            name = user.name,
            email = user.email,
            staff = user.staff,
            correctionPoint = user.correctionPoint.toString(),
            wallet = user.wallet.toString(),
            city = user.city,
            courses = mapToUserCourse(user.courses, user.projects, user.staff).reversed()
        )

    private fun mapToUserCourse(
        courses: List<Course>,
        projects: List<Project>,
        isStaff: Boolean
    ): List<UserCourse> {
        val mapProject: Map<Int, List<Project>> = createProjectMap(projects)
        return courses.map { course ->
            val topSkillLevel: Double = course.skills.maxOfOrNull { it.level } ?: 0.0
            UserCourse(
                id = course.id,
                name = course.name,
                isLevelVisible = !isStaff,
                level = String.format("%.2f", course.level),
                levelProgress = (course.level * 100).toInt() % 100,
                skills = parseSkills(course.skills, topSkillLevel),
                projects = parseProjects(mapProject, course.id)
            )
        }
    }

    private fun createProjectMap(projectList: List<Project>): Map<Int, List<Project>> {
        val projects: MutableMap<Int, MutableList<Project>> = mutableMapOf()
        for (project in projectList) {
            if (!project.hasParent) {
                for (coursesId in project.coursesIds) {
                    if (projects.containsKey(coursesId)) {
                        projects[coursesId]?.add(project)
                    } else {
                        projects[coursesId] = mutableListOf(project)
                    }
                }
            }
        }
        return projects
    }

    private fun parseSkills(skills: List<Skill>, topSkillLevel: Double): List<UserSkill> =
        skills.mapIndexed { index, skill ->
            val isFirstSkill = index == 0
            val isTopSkill = skill.level == topSkillLevel
            mapToUserSkill(
                skill,
                isTopSkill,
                isFirstSkill
            )
        }

    private fun mapToUserSkill(
        skill: Skill,
        isTopSkill: Boolean,
        isFirstSkill: Boolean
    ): UserSkill =
        UserSkill(
            name = skill.name,
            level = String.format("%.2f", skill.level),
            isTopSkill = isTopSkill,
            isFirstSkill = isFirstSkill
        )

    private fun parseProjects(
        mapProject: Map<Int, List<Project>>,
        courseId: Int
    ): List<UserProject> {
        val sortedProject = mapProject[courseId]?.sortedBy { it.name }
        return sortedProject?.mapIndexed { index, project ->
            val isFirstProject = index == 0
            mapToUserProject(project, isFirstProject)
        } ?: emptyList()
    }

    private fun mapToUserProject(project: Project, isFirstProject: Boolean): UserProject =
        UserProject(
            name = project.name,
            status = project.status,
            validated = project.validated,
            finalMark = project.finalMark?.toString() ?: "",
            isFirstProject = isFirstProject
        )
}