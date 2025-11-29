package com.example.cp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cp.databinding.FragmentPrivateBinding

class PrivateFragment : Fragment() {
    private lateinit var binding: FragmentPrivateBinding
    private lateinit var adapter: PrivateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate layout
        binding = FragmentPrivateBinding.inflate(inflater, container, false)

        // ----------------------------
        // DATA PRIVATE (bisa kamu ganti)
        // ----------------------------
        val privateList = listOf(
            PrivateModel("Private Catatan 1", "Isi private note pertama."),
            PrivateModel("Private Catatan 2", "Ini adalah catatan rahasia."),
            PrivateModel("Private Catatan 3", "Konten private lainnya.")
        )

        // SETUP ADAPTER
        adapter = PrivateAdapter(privateList)

        binding.rvPrivate.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPrivate.adapter = adapter

        return binding.root
    }
}
