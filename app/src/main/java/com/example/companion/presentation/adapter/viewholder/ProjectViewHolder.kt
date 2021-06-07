package com.example.companion.presentation.adapter.viewholder

import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.companion.databinding.ItemProjectBinding
import com.example.companion.presentation.model.UserProject

class ProjectViewHolder(private val binding: ItemProjectBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(project: UserProject) {
        with(binding) {
            tvName.text = project.name
            vDelimiter.isVisible = project.isDelimiterVisible

            tvFinalMark.text = project.finalMark
            @ColorInt val color = binding.tvFinalMark.context.getColor(project.finalMarkColor)
            tvFinalMark.setTextColor(color)

            val icon = AppCompatResources.getDrawable(binding.ivStatus.context, project.statusIcon)
            ivStatus.setImageDrawable(icon)
        }
    }
}