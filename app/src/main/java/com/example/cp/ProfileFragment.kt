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

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // =============================
        // 1. AMBIL DATA DARI SHAREDPREF
        // =============================
        val sharedPref = requireContext().getSharedPreferences("UserSession", 0)
        val username = sharedPref.getString("USERNAME", "")
        val password = sharedPref.getString("PASSWORD", "")

        // =============================
        // 2. TEMUKAN EDITTEXT DI XML
        // =============================
        val edtName = view.findViewById<EditText>(R.id.edtName)
        val edtPassword = view.findViewById<EditText>(R.id.edtPassword)

        // =============================
        // 3. MASUKKAN DATA KE EDITTEXT
        // =============================
        edtName.setText(username)
        edtPassword.setText(password)

        // =============================
        // 4. LOGOUT
        // =============================
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {

            // HAPUS SESSION LOGIN
            sharedPref.edit().clear().apply()

            // Pindah ke LoginActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }
}
