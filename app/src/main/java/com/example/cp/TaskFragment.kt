package com.example.cp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cp.databinding.FragmentTaskBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskFragment : Fragment() {

    private lateinit var binding: FragmentTaskBinding
    private lateinit var adapter: TaskAdapter
    private var taskList: MutableList<TaskModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTaskBinding.inflate(inflater, container, false)

        taskList = loadTasks()

        adapter = TaskAdapter(taskList) { updatedList ->
            saveTasks(updatedList)
        }

        binding.recyclerNotes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerNotes.adapter = adapter

        binding.etInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                val text = binding.etInput.text.toString().trim()

                if (text.isNotEmpty()) {
                    adapter.addTask(text)
                    binding.etInput.text.clear()
                }

                true
            } else false
        }

        return binding.root
    }

    private fun saveTasks(list: MutableList<TaskModel>) {
        val prefs = requireContext().getSharedPreferences("task_prefs", Context.MODE_PRIVATE)
        val json = Gson().toJson(list)
        prefs.edit().putString("tasks", json).apply()
    }

    private fun loadTasks(): MutableList<TaskModel> {
        val prefs = requireContext().getSharedPreferences("task_prefs", Context.MODE_PRIVATE)
        val json = prefs.getString("tasks", null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<TaskModel>>() {}.type
        return Gson().fromJson(json, type)
    }
}
