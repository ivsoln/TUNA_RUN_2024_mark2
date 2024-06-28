package com.inventive.tunarun

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SkipjackWipCookRackActivity : AppCompatActivity() {
    lateinit var textRackNo: EditText
    lateinit var textStartTime: EditText
    lateinit var textMessage: TextView
    lateinit var textCancelRack: TextView
    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_wip_cook_rack)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        textRackNo = findViewById(R.id.text_rackNo)
        textStartTime = findViewById(R.id.text_startTime)
        textMessage = findViewById(R.id.text_message)
        textCancelRack = findViewById(R.id.text_cancel_rack)
    }
}