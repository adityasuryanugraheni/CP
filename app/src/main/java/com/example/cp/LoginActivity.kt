package com.example.cp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private val validUsers = mapOf(
        "Yumna" to "111"
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

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this,
                    "Name or password is not filled in",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (validUsers[username] == password) {

                val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
                sharedPref.edit().apply {
                    putString("USERNAME", username)
                    putString("PASSWORD", password)
                    apply()
                }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                Toast.makeText(this, "login successful", Toast.LENGTH_SHORT).show()
                finish()

            } else {
                Toast.makeText(this,
                    "Incorrect username or password",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}
