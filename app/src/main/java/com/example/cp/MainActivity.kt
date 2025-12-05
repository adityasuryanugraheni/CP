package com.example.cp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var lastSelectedTab = R.id.notesFragment   // ⭐ TAB TERAKHIR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navController = findNavController(R.id.fragmentContainerView)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // LISTENER NAVBAR (PRIVATE → PIN)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                // ⭐ Jika klik Private Notes → tampilkan PIN dialog
                R.id.privateFragment -> {
                    showPinDialog()
                    return@setOnItemSelectedListener true  // ⭐ AGAR TIDAK TERHIGHLIGHT
                }

                else -> {
                    lastSelectedTab = item.itemId     // ⭐ SIMPAN TAB TERAKHIR
                    navController.navigate(item.itemId)
                    return@setOnItemSelectedListener true
                }
            }
        }
    }

    // =====================================
    //               POPUP PIN
    // =====================================
    private fun showPinDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_pin, null)

        val et1 = dialogView.findViewById<EditText>(R.id.et1)
        val et2 = dialogView.findViewById<EditText>(R.id.et2)
        val et3 = dialogView.findViewById<EditText>(R.id.et3)
        val et4 = dialogView.findViewById<EditText>(R.id.et4)

        val inputs = listOf(et1, et2, et3, et4)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Auto move ke kotak berikutnya
        inputs.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && index < inputs.size - 1) {
                        inputs[index + 1].requestFocus()
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }

        // Cancel
        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()

            // ⭐ KEMBALIKAN TAB KE YANG TERAKHIR SEBELUM PRIVATE
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
            bottomNav.selectedItemId = lastSelectedTab
        }

        // Verification
        dialogView.findViewById<Button>(R.id.btnVerify).setOnClickListener {
            val pin = inputs.joinToString("") { it.text.toString() }

            val validPins = listOf("9999")

            if (pin in validPins) {
                findNavController(R.id.fragmentContainerView).navigate(R.id.privateFragment)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "PIN salah!", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()

        // AGAR DITENGAH
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.CENTER)

        // supaya rapi
        inputs.forEach { edit ->
            edit.setBackgroundResource(R.drawable.pin_box_selector)
            edit.setTextColor(android.graphics.Color.BLACK)
            edit.textSize = 24f
        }
    }
}