package com.example.cp.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cp.Note
import com.example.cp.NotesStorage
import com.example.cp.R
import com.example.cp.WriteActivity

class NotesAdapter(
    private val notes: MutableList<Note>,
    private val context: Context,
    private val onNoteClick: (Note, Int) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>()
 {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvContent: TextView = itemView.findViewById(R.id.tvDesc)

        fun bind(note: Note) {
            tvTitle.text = note.title
            tvContent.text = note.content

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {

                    AlertDialog.Builder(context)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes") { _, _ ->

                            notes.removeAt(position)
                            notifyItemRemoved(position)

                            NotesStorage.saveNotes(context, notes)
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
                true
            }
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onNoteClick(notes[position], position)
                }
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
