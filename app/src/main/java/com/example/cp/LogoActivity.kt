package com.example.cp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import android.view.animation.AccelerateDecelerateInterpolator

class LogoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)

        val logo = findViewById<ImageView>(R.id.logoImage)

        // Animasi logo sedikit naik + fade in
        logo.animate()
            .translationY(-120f)
            .alpha(1f)
            .setDuration(1200)
            .setInterpolator(AccelerateDecelerateInterpolator()) // lebih smooth
            .start()

        // Pindah halaman dengan shared element transition
        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this, LoginActivity::class.java)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                logo,
                "app_logo" // pastikan sama di LoginActivity
            )

            startActivity(intent, options.toBundle())
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
            finish()

        }, 1300)
    }
}
