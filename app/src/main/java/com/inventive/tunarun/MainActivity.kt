package com.inventive.tunarun

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private lateinit var buttonLogin: TextView
    private lateinit var buttonRm: TextView
    private lateinit var buttonCook: TextView
    private lateinit var buttonSteam: TextView
    private lateinit var buttonClean: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        buttonLogin = findViewById(R.id.lnk_login)
        buttonLogin.isVisible = false
        buttonLogin.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivityForResult(it, 0, null)
            }
        }


        buttonRm = findViewById(R.id.lnk_rm)
        buttonRm.setOnClickListener {
            val intent = Intent(this, SkipjackWipMainActivity::class.java)
            startActivity(intent)
        }

        buttonCook = findViewById(R.id.lnk_cook)
        buttonCook.setOnClickListener {
            val intent = Intent(this, SkipjackCookMainActivity::class.java)
            startActivity(intent)
        }


        buttonSteam = findViewById(R.id.lnk_steam)
        buttonSteam.setOnClickListener {
            val intent = Intent(this, SkipjackSteamMainActivity::class.java)
            startActivity(intent)
        }

        buttonClean = findViewById(R.id.lnk_clean)
        buttonClean.setOnClickListener {
            val intent = Intent(this, SkipjackCleanMainActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.isVisible = false
        buttonRm.isVisible = false
        buttonCook.isVisible = false
        buttonSteam.isVisible = false
        buttonClean.isVisible = false

        FishClient.also {
            val callback = object : InitCallback {
                override var count: Int = 0
                override var total: Int = 4

                override fun refresh() {
                    if (count === total) {
                        success()
                    }
                }

                override fun success() {
                    buttonLogin.isVisible = true
                }

                override fun onError(error: String) {
                }
            }
            it.Init(this, callback)
        }
    }

    //0 : Check-Login
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                buttonLogin.isVisible = false
                when (FishClient.Companion.Skipjack.Identity.RoleId) {
                    2 -> {
                        buttonRm.isVisible = true
                        buttonCook.isVisible = true
                        buttonSteam.isVisible = true
                        buttonClean.isVisible = true
                    }
                    3 -> {
                        buttonRm.isVisible = true
                        val intent = Intent(this, SkipjackWipMainActivity::class.java)
                        startActivity(intent)
                    }
                    4 -> {
                        buttonCook.isVisible = true
                        val intent = Intent(this, SkipjackCookMainActivity::class.java)
                        startActivity(intent)
                    }
                    5 -> {
                        buttonSteam.isVisible = true
                        val intent = Intent(this, SkipjackSteamMainActivity::class.java)
                        startActivity(intent)
                    }
                    6 -> {
                        buttonClean.isVisible = true
                        val intent = Intent(this, SkipjackCleanMainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}