package com.example.to_do_list

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
        tasks = SharedPrefsHelper.loadTasks(this).toMutableList()
        setupAdapter()

        // Configurar listeners
        buttonAdd.setOnClickListener { addTask() }
        buttonDelete.setOnClickListener { deleteSelectedTasks() }
        updateDeleteButton()
    }

    private fun setupAdapter() {
        adapter = TaskAdapter(
            context = this,
            tasks = tasks,
            onTaskChecked = { position, isChecked ->
                tasks[position].isCompleted = isChecked
                saveTasks()
            },
            onTaskEdit = { position ->
                showEditDialog(position)
            },
            onTaskSelected = { position ->
                adapter.toggleSelection(position)
                updateDeleteButton()
            }
        )
        listViewTasks.adapter = adapter
    }

    private fun updateDeleteButton() {
        buttonDelete.isEnabled = adapter.getSelectedTasks().isNotEmpty()
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
        val selectedTasks = adapter.getSelectedTasks()
        if (selectedTasks.isEmpty()) return

        AlertDialog.Builder(this)
            .setTitle("Eliminar tareas")
            .setMessage("¿Estás seguro de eliminar ${selectedTasks.size} tareas?")
            .setPositiveButton("Eliminar") { _, _ ->
                tasks.removeAll(selectedTasks)
                adapter.clearSelections()
                adapter.notifyDataSetChanged()
                saveTasks()
                updateDeleteButton()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showEditDialog(position: Int) {
        val task = tasks[position]
        val editText = EditText(this).apply {
            setText(task.description)
        }

        AlertDialog.Builder(this)
            .setTitle("Editar tarea")
            .setView(editText)
            .setPositiveButton("Guardar") { _, _ ->
                val newText = editText.text.toString().trim()
                if (newText.isNotEmpty()) {
                    task.description = newText
                    adapter.notifyDataSetChanged()
                    saveTasks()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun saveTasks() {
        SharedPrefsHelper.saveTasks(this, tasks)
    }
}