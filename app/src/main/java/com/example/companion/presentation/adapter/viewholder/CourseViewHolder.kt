package com.example.companion.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.companion.databinding.ItemCourseBinding
import com.example.companion.presentation.model.UserCourse

class CourseViewHolder(private val binding: ItemCourseBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(course: UserCourse, onCourseClicked: (Int) -> Unit) {
        with(binding.bCurse) {
            setOnClickListener { onCourseClicked(course.id) }
            text = course.name
            isChecked = course.isCurrentCourse
        }
    }
}