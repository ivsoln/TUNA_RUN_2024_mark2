package com.inventive.tunarun

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.inventive.tunarun.FishClient.Companion.REQUEST_SHIFT
import com.inventive.tunarun.FishClient.Companion.showUser
import com.inventive.tunarun.Instant.Companion.afterKeyEntered
import com.inventive.tunarun.Instant.Companion.afterTextChanged
import com.inventive.tunarun.Instant.Companion.focusThenSelectionAll
import com.inventive.tunarun.Instant.Companion.toIntOrDefault
import java.text.SimpleDateFormat

class SkipjackCookMainActivity : AppCompatActivity() {

    private var steamNo: Int = 0
    private var rackCount: Int = 0

    private lateinit var textSteamNo: EditText
    private lateinit var textRackCount: EditText

    private lateinit var gotoContinue: TextView
    private lateinit var gotoBatchCook: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_cook_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        init()
        textSteamNo = findViewById(R.id.text_steamNo)
        textSteamNo.afterTextChanged {
            steamNo = textSteamNo.text.toString().toIntOrDefault(0)
            prompt()
        }

        textRackCount = findViewById(R.id.text_rackCount)
        textRackCount.afterTextChanged {
            rackCount = textRackCount.text.toString().toIntOrDefault(0)
            prompt()
        }


        gotoContinue = findViewById(R.id.goto_continue)
        gotoContinue.isEnabled = false
        gotoContinue.setOnClickListener {
            next()
        }

        gotoBatchCook = findViewById(R.id.goto_batchCook)
        gotoBatchCook.setOnClickListener {
            val intent = Intent(this, SkipjackCookListActivity::class.java)
            startActivity(intent)
        }

        textSteamNo.requestFocus()
        textSteamNo.afterKeyEntered {
            textRackCount.focusThenSelectionAll()
        }

        textRackCount.afterKeyEntered {
            prompt().also {
                if (it) {
                    next()
                }
            }
        }
    }

    private fun init() {
        findViewById<TextView>(R.id.text_user).showUser()
        var viewDate: TextView = findViewById(R.id.view_date)
        var viewShift: TextView = findViewById(R.id.view_shift)

        viewDate.text =
            SimpleDateFormat(Instant.dateFormat).format(FishClient.Companion.Skipjack.Shift.Date)
        viewShift.text = FishClient.Companion.Skipjack.Shift.WorkShift?.ShiftCode.toString()

        if (FishClient.Companion.Skipjack.Shift.Shift == 1) {
            viewShift.setBackgroundColor(resources.getColor(R.color.Blue_Gray_050))
        }
        if (FishClient.Companion.Skipjack.Shift.Shift == 2) {
            viewShift.setBackgroundColor(resources.getColor(R.color.Amber_A200))
        }

        viewDate.setOnClickListener {
            Intent(this, SkipjackShiftActivity::class.java).also {
                startActivityForResult(it, REQUEST_SHIFT, null)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_SHIFT) {
                init()
            }
        }
    }

    private fun next() {
        val intent = Intent(this, SkipjackCookPostActivity::class.java)
        intent.putExtra("STEAM_NO", steamNo)
        intent.putExtra("RACK_COUNT", rackCount)
        startActivity(intent)
    }

    private fun prompt(): Boolean {
        if (steamNo <= 0) {
            gotoContinue.isEnabled = false
            return false
        }
        if (rackCount <= 0) {
            gotoContinue.isEnabled = false
            return false
        }

        gotoContinue.isEnabled = true
        return true
    }

}