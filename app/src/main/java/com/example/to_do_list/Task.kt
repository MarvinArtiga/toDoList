package com.example.to_do_list

data class Task(
    var description: String,
    var isCompleted: Boolean = false
)