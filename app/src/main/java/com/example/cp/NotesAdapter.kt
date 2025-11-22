package com.example.cp.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cp.Note
import com.example.cp.NotesStorage
import com.example.cp.R

class NotesAdapter(private val notes: MutableList<Note>, private val context: Context) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvContent: TextView = itemView.findViewById(R.id.tvDesc)

        fun bind(note: Note) {
            tvTitle.text = note.title
            tvContent.text = note.content

            // Long click untuk hapus
            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Konfirmasi hapus
                    AlertDialog.Builder(context)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes") { _, _ ->
                            // Hapus dari list
                            notes.removeAt(position)
                            notifyItemRemoved(position)

                            // Simpan perubahan ke SharedPreferences
                            NotesStorage.saveNotes(context, notes)
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notes, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int = notes.size
}
