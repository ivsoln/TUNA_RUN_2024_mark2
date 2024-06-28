package com.inventive.tunarun

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SkipjackCleanMainActivity : AppCompatActivity() {

    private lateinit var gotoCleaning: TextView
    private lateinit var gotoTmptrRec: TextView
    private lateinit var gotoBatchCook: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_clean_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        gotoCleaning = findViewById(R.id.goto_cleaning)
        gotoCleaning.setOnClickListener {
            val intent = Intent(this, SkipjackCleanTagActivity::class.java)
            startActivity(intent)
        }


        gotoTmptrRec = findViewById(R.id.goto_tempRec)
        gotoTmptrRec.setOnClickListener {
            val intent = Intent(this, SkipjackCookTmptrActivity::class.java)
            startActivity(intent)
        }



        gotoBatchCook = findViewById(R.id.goto_batchCook)
        gotoBatchCook.setOnClickListener {
            val intent = Intent(this, SkipjackCookListActivity::class.java)
            startActivity(intent)
        }

    }
}