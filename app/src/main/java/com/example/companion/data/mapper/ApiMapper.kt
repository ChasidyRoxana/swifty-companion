package com.example.companion.data.mapper

import com.example.companion.data.model.*
import com.example.companion.domain.model.*

class ApiMapper {

    fun mapToToken(tokenApi: TokenApi): Token =
        Token(
            accessToken = "Bearer ${tokenApi.access_token}",
            expiresIn = tokenApi.expires_in,
            createdAt = tokenApi.created_at
        )

    fun mapToUser(userApi: UserApi): User =
        User(
            imageUrl = userApi.image_url,
            name = userApi.displayname,
            email = userApi.email,
            staff = userApi.staff,
            correctionPoint = userApi.correction_point,
            wallet = userApi.wallet,
            city = userApi.campus.last().city,
            courses = userApi.cursus_users.map { mapToCourse(it) },
            projects = userApi.projects_users.map { mapToProject(it) }
        )

    private fun mapToCourse(coursesApi: CourseApi): Course =
        Course(
            id = coursesApi.cursus_id,
            name = coursesApi.cursus.name,
            level = coursesApi.level,
            skills = coursesApi.skills.map { mapToSkill(it) }
        )

    private fun mapToSkill(skillApi: SkillApi): Skill =
        Skill(
            name = skillApi.name,
            level = skillApi.level
        )

    private fun mapToProject(projectsApi: ProjectApi): Project =
        Project(
            name = projectsApi.project.name,
            status = mapToStatus(projectsApi.status),
            validated = projectsApi.validated,
            finalMark = projectsApi.final_mark,
            hasParent = projectsApi.project.parent_id != null,
            coursesIds = projectsApi.cursus_ids // можно не копировать?
        )

    private fun mapToStatus(statusApi: String): Status {
        return when (statusApi) {
            STATUS_FINISHED -> Status.Finished
            STATUS_IN_PROGRESS -> Status.InProgress
            STATUS_SEARCHING_GROUP -> Status.SearchingGroup
            STATUS_WAITING_CORRECTION -> Status.WaitingCorrection
            else -> Status.Unknown
        }
    }

    companion object {
        private const val STATUS_FINISHED = "finished"
        private const val STATUS_IN_PROGRESS = "in_progress"
        private const val STATUS_SEARCHING_GROUP = "searching_a_group"
        private const val STATUS_WAITING_CORRECTION = "waiting_for_correction"
    }
}