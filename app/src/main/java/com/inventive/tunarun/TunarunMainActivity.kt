package com.inventive.tunarun

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TunarunMainActivity : AppCompatActivity() {

    lateinit var viewDate: TextView
    lateinit var viewShift: TextView
    lateinit var goToRm: TextView
    lateinit var goToPack: TextView
    lateinit var goToSeamSeal: TextView
    lateinit var goToBasket: TextView
    lateinit var goToRetort: TextView
    lateinit var goToInspect: TextView
    lateinit var goToPallet: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tunarun_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewDate = findViewById(R.id.view_date)
        viewShift = findViewById(R.id.view_shift)
        goToRm = findViewById(R.id.goto_rm)
        goToPack = findViewById(R.id.goto_pack)
        goToSeamSeal = findViewById(R.id.goto_seam_seal)
        goToBasket = findViewById(R.id.goto_basket)
        goToRetort = findViewById(R.id.goto_retort)
        goToInspect = findViewById(R.id.goto_inspect)
        goToPallet = findViewById(R.id.goto_pallet)
    }
}