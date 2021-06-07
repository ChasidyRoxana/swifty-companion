package com.example.companion.presentation.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.example.companion.R
import com.example.companion.domain.model.Status

class UserProject(
    val name: String,
    status: Status,
    validated: Boolean?,
    val finalMark: String,
    isFirstProject: Boolean
) {

    val isDelimiterVisible = !isFirstProject

    @DrawableRes
    val statusIcon =
        when {
            validated == true && status == Status.Finished -> R.drawable.ic_success_24
            status != Status.Finished -> R.drawable.ic_time_24
            else -> R.drawable.ic_fail_24
        }

    @ColorRes
    val finalMarkColor =
        when {
            validated == true && status == Status.Finished -> R.color.green_700
            else -> R.color.red_500
        }
}