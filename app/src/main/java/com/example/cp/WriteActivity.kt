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

        // terima data dari popup
        val initialTitle = intent.getStringExtra("title") ?: ""
        val hide = intent.getBooleanExtra("hide", false)

        binding.etTitle.setText(initialTitle)

        binding.btnBack.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val content = binding.etContent.text.toString().trim()

            val result = Intent().apply {
                putExtra("title", title)
                putExtra("content", content)
                putExtra("hide", hide)
            }

            setResult(Activity.RESULT_OK, result)
            finish()
        }
    }
}
