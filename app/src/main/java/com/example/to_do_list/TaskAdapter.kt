package com.example.to_do_list

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView

class TaskAdapter(context: Context, private val tasks: MutableList<Task>) :
    ArrayAdapter<Task>(context, android.R.layout.simple_list_item_multiple_choice, tasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            android.R.layout.simple_list_item_multiple_choice,
            parent,
            false
        )

        val checkedTextView = view as CheckedTextView
        val task = tasks[position]

        // Configurar el texto
        checkedTextView.text = task.description

        // Aplicar estilo tachado si está completada
        if (task.isCompleted) {
            checkedTextView.paintFlags = checkedTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            checkedTextView.paintFlags = checkedTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        // Marcar el estado actual
        checkedTextView.isChecked = task.isCompleted

        // Manejar el cambio de estado al hacer clic
        view.setOnClickListener {
            // Cambiar el estado de la tarea
            task.isCompleted = !task.isCompleted
            // Actualizar la vista
            notifyDataSetChanged()
            // Actualizar las preferencias
            (context as MainActivity).saveTasks()
        }

        return view
    }
}