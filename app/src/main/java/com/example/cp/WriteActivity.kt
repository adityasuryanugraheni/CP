package com.example.cp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cp.databinding.ActivityWriteBinding

class WriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val initialTitle = intent.getStringExtra("title") ?: ""
        val initialContent = intent.getStringExtra("content") ?: ""
        val hide = intent.getBooleanExtra("hide", false)
        val position = intent.getIntExtra("position", -1)

        binding.etTitle.setText(initialTitle)
        binding.etContent.setText(initialContent)

        binding.btnBack.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val content = binding.etContent.text.toString().trim()

            if (title.isEmpty()) {
                binding.etTitle.error = "Title cannot be empty"
                return@setOnClickListener
            }

            val result = Intent().apply {
                putExtra("title", title)
                putExtra("content", content)
                putExtra("hide", hide)

                if (position != -1) {
                    putExtra("position", position)
                }
            }

            setResult(Activity.RESULT_OK, result)
            finish()
        }
    }
}
