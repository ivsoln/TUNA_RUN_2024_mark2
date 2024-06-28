package com.inventive.tunarun

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class SkipjackCookPostActivity : AppCompatActivity() {

    lateinit var gotoScan: TextView
    lateinit var gotoReset: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_cook_post)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        gotoScan = findViewById(R.id.goto_scan)
        gotoReset = findViewById(R.id.goto_reset)

        gotoScan.setOnClickListener {
            val intent = Intent(this, SkipjackCookPostTagActivity::class.java)
            startActivity(intent)
        }
    }
}