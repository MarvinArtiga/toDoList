package com.example.to_do_list

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPrefsHelper {
    private const val PREFS_NAME = "TodoListPrefs"
    private const val TASKS_KEY = "tasks"

    fun saveTasks(context: Context, tasks: List<Task>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val json = Gson().toJson(tasks)
        editor.putString(TASKS_KEY, json)
        editor.apply()
    }

    fun loadTasks(context: Context): MutableList<Task> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(TASKS_KEY, null)
        val type = object : TypeToken<MutableList<Task>>() {}.type
        return Gson().fromJson(json, type) ?: mutableListOf()
    }
}