package com.example.cp

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cp.databinding.ItemPrivateBinding

class PrivateAdapter(
    private val list: MutableList<Note>,
    private val context: Context,
    private val onClick: (Note, Int) -> Unit
) : RecyclerView.Adapter<PrivateAdapter.PrivateViewHolder>() {

    inner class PrivateViewHolder(val binding: ItemPrivateBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrivateViewHolder {
        val binding = ItemPrivateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PrivateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PrivateViewHolder, position: Int) {
        val item = list[position]

        holder.binding.PrivateTitle.text = item.title
        holder.binding.PrivateDesc.text = item.content

        // ================================
        //  CLICK → EDIT PRIVATE NOTE
        // ================================
        holder.binding.root.setOnClickListener {
            onClick(item, position)
        }

        // ================================
        //  LONG CLICK → DELETE PRIVATE NOTE
        // ================================
        holder.binding.root.setOnLongClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete Private Note")
                .setMessage("Are you sure you want to delete this private note?")
                .setPositiveButton("Yes") { _, _ ->

                    // Hapus dari list
                    list.removeAt(position)
                    notifyItemRemoved(position)

                    // Simpan perubahan
                    NotesStorage.savePrivateNotes(context, list)
                }
                .setNegativeButton("No", null)
                .show()

            true
        }
    }

    override fun getItemCount(): Int = list.size
}
