package com.example.cp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cp.databinding.FragmentPrivateBinding

class PrivateFragment : Fragment() {

    private var _binding: FragmentPrivateBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PrivateAdapter
    private var privateNotes = mutableListOf<Note>()

    private val privateWriteActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {

                val data = result.data
                val title = data?.getStringExtra("title") ?: ""
                val content = data?.getStringExtra("content") ?: ""
                val position = data?.getIntExtra("position", -1) ?: -1

                val updatedNote = Note(title, content)

                if (position != -1) {
                    privateNotes[position] = updatedNote
                    NotesStorage.savePrivateNotes(requireContext(), privateNotes)
                    adapter.notifyItemChanged(position)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrivateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        privateNotes = NotesStorage.loadPrivateNotes(requireContext())
        setupRecycler()
    }

    private fun setupRecycler() {
        adapter = PrivateAdapter(privateNotes, requireContext()) { note, position ->

            val intent = Intent(requireContext(), WriteActivity::class.java)
            intent.putExtra("title", note.title)
            intent.putExtra("content", note.content)
            intent.putExtra("position", position)
            intent.putExtra("hide", true)

            privateWriteActivityResult.launch(intent)
        }

        binding.rvPrivate.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPrivate.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        val savedPrivate = NotesStorage.loadPrivateNotes(requireContext())

        if (savedPrivate.size != privateNotes.size) {
            privateNotes.clear()
            privateNotes.addAll(savedPrivate)
            adapter.notifyDataSetChanged()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
