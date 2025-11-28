package com.example.cp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    // Daftar user dan password yang diperbolehkan
    private val validUsers = mapOf(
        "aditya" to "123",
        "yumna" to "111",
        "raka" to "abc",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etName = findViewById<EditText>(R.id.etName)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {

            val username = etName.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Cek kosong
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nama atau password belum diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cek apakah username ada dan password benar
            if (validUsers[username] == password) {

                // SIMPAN DATA LOGIN KE SHAREDPREFERENCES
                val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
                sharedPref.edit().apply {
                    putString("USERNAME", username)
                    putString("PASSWORD", password)
                    apply()
                }

                // Pindah ke MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                finish()

            } else {
                Toast.makeText(this, "Username atau password salah", Toast.LENGTH_SHORT).show()
            }
        }   // ⬅️ ini penutup btnLogin.setOnClickListener
    }       // ⬅️ ini penutup onCreate
}           // ⬅️ ini penutup class
