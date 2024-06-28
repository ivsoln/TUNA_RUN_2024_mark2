package com.inventive.tunarun

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SkipjackSteamOffActivity : AppCompatActivity() {

    lateinit var textBarcode: EditText
    lateinit var textGroup: EditText
    lateinit var textBatch: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_steam_off)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textBarcode = findViewById(R.id.text_barcode)
        textGroup = findViewById(R.id.text_group)
        textBatch = findViewById(R.id.text_batch)
    }
}