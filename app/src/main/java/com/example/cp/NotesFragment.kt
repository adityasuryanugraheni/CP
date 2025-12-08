package com.example.cp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cp.adapter.NotesAdapter
import com.example.cp.databinding.FragmentNotesBinding

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NotesAdapter
    private var notes = mutableListOf<Note>()

    private val WriteActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val data = result.data
                val title = data?.getStringExtra("title") ?: ""
                val content = data?.getStringExtra("content") ?: ""
                val hide = data?.getBooleanExtra("hide", false) ?: false
                val position = data?.getIntExtra("position", -1) ?: -1

                val note = Note(title, content)

                if (position != -1) {
                    notes[position] = note
                    NotesStorage.saveNotes(requireContext(), notes)
                    adapter.notifyItemChanged(position)
                    return@registerForActivityResult
                }

                if (hide) {
                    val privateNotes = NotesStorage.loadPrivateNotes(requireContext())
                    privateNotes.add(0, note)
                    NotesStorage.savePrivateNotes(requireContext(), privateNotes)
                } else {
                    notes.add(0, note)
                    NotesStorage.saveNotes(requireContext(), notes)
                    adapter.notifyItemInserted(0)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notes = NotesStorage.loadNotes(requireContext())
        setupRecycler()
        setupFab()
    }

    private fun setupRecycler() {
        adapter = NotesAdapter(notes, requireContext()) { note, position ->
            val intent = Intent(requireContext(), WriteActivity::class.java)
            intent.putExtra("title", note.title)
            intent.putExtra("content", note.content)
            intent.putExtra("position", position)
            WriteActivityResult.launch(intent)
        }

        binding.rvNotes.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvNotes.adapter = adapter
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener { showAddPopup() }
    }

    private fun showAddPopup() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_notes, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val etTitle = dialogView.findViewById<EditText>(R.id.etPopupTitle)
        val cbHide = dialogView.findViewById<CheckBox>(R.id.cbHide)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(requireContext(), WriteActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("hide", cbHide.isChecked)
            WriteActivityResult.launch(intent)

            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
