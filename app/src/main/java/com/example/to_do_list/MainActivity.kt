package com.example.to_do_list

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var editTextTask: EditText
    private lateinit var buttonAdd: Button
    private lateinit var buttonDelete: Button
    private lateinit var listViewTasks: ListView
    private lateinit var tasks: MutableList<Task>
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        editTextTask = findViewById(R.id.editTextTask)
        buttonAdd = findViewById(R.id.buttonAdd)
        buttonDelete = findViewById(R.id.buttonDelete)
        listViewTasks = findViewById(R.id.listViewTasks)

        // Cargar tareas guardadas
        tasks = SharedPrefsHelper.loadTasks(this)
        adapter = TaskAdapter(this, tasks)
        listViewTasks.adapter = adapter
        listViewTasks.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Configurar listeners
        buttonAdd.setOnClickListener { addTask() }
        buttonDelete.setOnClickListener { deleteSelectedTasks() }
    }

    fun saveTasks() {
        SharedPrefsHelper.saveTasks(this, tasks)
    }

    private fun addTask() {
        val taskDescription = editTextTask.text.toString().trim()
        if (taskDescription.isNotEmpty()) {
            tasks.add(Task(taskDescription))
            adapter.notifyDataSetChanged()
            editTextTask.text.clear()
            saveTasks()
        }
    }

    private fun deleteSelectedTasks() {
        val checkedPositions = listViewTasks.checkedItemPositions
        val itemsToRemove = mutableListOf<Task>()

        for (i in 0 until listViewTasks.count) {
            if (checkedPositions.get(i)) {
                itemsToRemove.add(tasks[i])
            }
        }

        tasks.removeAll(itemsToRemove)
        adapter.notifyDataSetChanged()
        listViewTasks.clearChoices()
        saveTasks()
    }
}