package com.example.cp

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cp.databinding.ItemTaskBinding

class TaskAdapter(
    private val list: MutableList<TaskModel>,
    private val onUpdate: (MutableList<TaskModel>) -> Unit
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.binding.tvTask.text = item.text
        holder.binding.cbDone.isChecked = item.checked

        holder.binding.cbDone.setOnCheckedChangeListener { _, isChecked ->
            list[holder.adapterPosition].checked = isChecked
            onUpdate(list)
        }

        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context

            AlertDialog.Builder(context)
                .setTitle("Delete List?")
                .setMessage("Are you sure you want to delete this list?")
                .setPositiveButton("Yes") { _, _ ->
                    val pos = holder.adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        list.removeAt(pos)
                        notifyItemRemoved(pos)
                        onUpdate(list)
                    }
                }
                .setNegativeButton("No", null)
                .show()
            true
        }
    }

    override fun getItemCount(): Int = list.size

    fun addTask(text: String) {
        list.add(0, TaskModel(text, false))
        notifyItemInserted(0)
        onUpdate(list)
    }
}
