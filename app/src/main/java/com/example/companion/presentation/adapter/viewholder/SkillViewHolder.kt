package com.example.companion.presentation.adapter.viewholder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.companion.databinding.ItemSkillBinding
import com.example.companion.presentation.model.UserSkill

class SkillViewHolder(private val binding: ItemSkillBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(skill: UserSkill) {
        with(binding) {
            tvName.text = skill.name
            tvLevel.text = skill.level
            vDelimiter.isVisible = skill.isDelimiterVisible
            ivTopSkill.isVisible = skill.isTopSkill
        }
    }
}