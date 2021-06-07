package com.example.companion.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.companion.databinding.ItemSkillBinding
import com.example.companion.presentation.adapter.viewholder.SkillViewHolder
import com.example.companion.presentation.model.UserSkill

class SkillAdapter : RecyclerView.Adapter<SkillViewHolder>() {

    private var skills: List<UserSkill> = emptyList()

    override fun getItemCount(): Int = skills.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSkillBinding.inflate(layoutInflater, parent, false)
        return SkillViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        holder.bind(skills[position])
    }

    fun setSkills(skills: List<UserSkill>) {
        this.skills = skills
    }
}