package com.example.to_do_list

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat

class TaskAdapter(
    private val context: Context,
    private var tasks: MutableList<Task>,
    private val onTaskChecked: (Int, Boolean) -> Unit,
    private val onTaskEdit: (Int) -> Unit,
    private val onTaskSelected: (Int) -> Unit
) : BaseAdapter() {

    private val selectedPositions = mutableSetOf<Int>()

    override fun getCount(): Int = tasks.size
    override fun getItem(position: Int): Task = tasks[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)

        val checkBox = view.findViewById<CheckBox>(R.id.checkBoxCompleted)
        val textView = view.findViewById<TextView>(R.id.textViewTask)
        val editButton = view.findViewById<ImageButton>(R.id.buttonEdit)
        val task = tasks[position]

        // Configurar texto y estado del checkbox
        textView.text = task.description
        checkBox.isChecked = task.isCompleted

        // Aplicar estilo tachado si está completada
        if (task.isCompleted) {
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        // Manejar cambios en el checkbox
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            onTaskChecked(position, isChecked)
        }

        // Configurar botón de edición
        editButton.setOnClickListener {
            onTaskEdit(position)
        }

        // Manejar selección para eliminar (clic largo)
        view.setOnLongClickListener {
            onTaskSelected(position)
            true
        }

        // Resaltar selección
        view.setBackgroundColor(
            if (selectedPositions.contains(position)) {
                ContextCompat.getColor(context, android.R.color.holo_blue_light)
            } else {
                ContextCompat.getColor(context, android.R.color.transparent)
            }
        )

        return view
    }

    fun toggleSelection(position: Int) {
        if (selectedPositions.contains(position)) {
            selectedPositions.remove(position)
        } else {
            selectedPositions.add(position)
        }
        notifyDataSetChanged()
    }

    fun getSelectedTasks(): List<Task> {
        return selectedPositions.map { tasks[it] }
    }

    fun clearSelections() {
        selectedPositions.clear()
        notifyDataSetChanged()
    }
}