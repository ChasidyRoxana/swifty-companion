package com.example.companion.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.companion.databinding.ItemProjectBinding
import com.example.companion.presentation.adapter.viewholder.ProjectViewHolder
import com.example.companion.presentation.model.UserProject

class ProjectAdapter : RecyclerView.Adapter<ProjectViewHolder>() {

    private var projects: List<UserProject> = emptyList()

    override fun getItemCount(): Int =
        projects.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProjectBinding.inflate(layoutInflater, parent, false)
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(projects[position])
    }

    fun setProjects(projects: List<UserProject>) {
        this.projects = projects
    }
}