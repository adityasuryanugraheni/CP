package com.example.cp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile,
            container,
            false)

        val sharedPref = requireContext().getSharedPreferences("UserSession", 0)
        val username = sharedPref.getString("USERNAME", "")
        val password = sharedPref.getString("PASSWORD", "")

        val edtName = view.findViewById<EditText>(R.id.edtName)
        val edtPassword = view.findViewById<EditText>(R.id.edtPassword)

        edtName.setText(username)
        edtPassword.setText(password)

        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {

            sharedPref.edit().clear().apply()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }
}
