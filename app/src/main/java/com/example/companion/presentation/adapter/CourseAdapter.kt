package com.example.companion.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.companion.databinding.ItemCourseBinding
import com.example.companion.presentation.adapter.viewholder.CourseViewHolder
import com.example.companion.presentation.model.UserCourse

class CourseAdapter(private val onCourseClicked: (Int) -> Unit) :
    RecyclerView.Adapter<CourseViewHolder>() {

    private var courses: List<UserCourse> = emptyList()

    override fun getItemCount(): Int =
        courses.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCourseBinding.inflate(layoutInflater, parent, false)
        return CourseViewHolder(binding)

    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(courses[position], onCourseClicked)
    }

    fun setCourses(courses: List<UserCourse>) {
        this.courses = courses
    }
}