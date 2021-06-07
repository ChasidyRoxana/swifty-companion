package com.example.companion.data.model

import com.google.gson.annotations.SerializedName

class ProjectApi(
    val final_mark: Long?,
    val status: String,
    @SerializedName("validated?")
    val validated: Boolean?,
    val project: ProjectDetailApi,
    val cursus_ids: List<Int>,
)