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

    // menerima hasil dari WriteActivity
    private val WriteActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val data = result.data
                val title = data?.getStringExtra("title") ?: ""
                val content = data?.getStringExtra("content") ?: ""
                val hide = data?.getBooleanExtra("hide", false) ?: false

                val finalTitle = if (hide) "Hidden Note" else title
                val finalContent = if (hide) "******" else content

                // Tambahkan ke list
                notes.add(0, Note(finalTitle, finalContent))

                // Simpan ke SharedPreferences
                NotesStorage.saveNotes(requireContext(), notes)

                adapter.notifyItemInserted(0)
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

        // Load sekali saja
        notes = NotesStorage.loadNotes(requireContext())

        setupRecycler()
        setupFab()
    }


    private fun setupRecycler() {
        adapter = NotesAdapter(notes, requireContext())
        binding.rvNotes.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvNotes.adapter = adapter
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            showAddPopup()
        }
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

    override fun onResume() {
        super.onResume()

        // Update tampilan setelah kembali dari WriteActivity
        val savedNotes = NotesStorage.loadNotes(requireContext())
        if (savedNotes.size != notes.size) {
            notes.clear()
            notes.addAll(savedNotes)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
