package com.inventive.tunarun

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.inventive.tunarun.FishClient.Companion.showShift
import com.inventive.tunarun.FishClient.Companion.showUser
import com.inventive.tunarun.Instant.Companion.popupText
import java.text.SimpleDateFormat

class SkipjackWipMainActivity : AppCompatActivity() {

    lateinit var viewDate: TextView
    lateinit var viewShift: TextView

    lateinit var gotoScanBin: TextView
    lateinit var gotoPrepareRack: TextView

    lateinit var gotoQueue: TextView
    lateinit var gotoTag: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_wip_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TextView>(R.id.text_user).showUser()

        viewDate = findViewById(R.id.view_date)
        viewShift = findViewById(R.id.view_shift)

        gotoScanBin = findViewById(R.id.goto_scanBin)
        gotoPrepareRack = findViewById(R.id.goto_prepareRack)

        gotoQueue = findViewById(R.id.goto_queue_list)
        gotoQueue.setOnClickListener {
            Intent(this, SkipjackQueListActivity::class.java).also {
                startActivityForResult(it, 0, null)
            }
        }
        gotoTag = findViewById(R.id.goto_tag)

        viewShift(FishClient.Companion.Skipjack.Shift)

        gotoScanBin.setOnClickListener {
            val intent = Intent(this, SkipjackBinActivity::class.java)
            startActivity(intent)
        }

        gotoPrepareRack.setOnClickListener {
            val intent = Intent(this, SkipjackWipCookRackActivity::class.java)
            startActivity(intent)
        }

        gotoTag.setOnClickListener {
            val intent = Intent(this, SkipjackWipTagActivity::class.java)
            startActivity(intent)
        }
    }

    //1 : Date-Shift Changed
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                viewShift(FishClient.Companion.Skipjack.Shift)
            }
        }
    }

    private fun viewShift(shift: Fish.Skipjack.DateShift) {
        viewDate.text =
            SimpleDateFormat(Instant.dateFormat).format(shift.Date)
        viewShift.text = shift.WorkShift?.ShiftCode.toString()

        if (shift.Shift == 1) {
            viewShift.setBackgroundColor(resources.getColor(R.color.Blue_Gray_050))
        }
        if (shift.Shift == 2) {
            viewShift.setBackgroundColor(resources.getColor(R.color.Amber_A200))
        }

        viewDate.setOnClickListener {
            Intent(this, SkipjackShiftActivity::class.java).also {
                startActivityForResult(it, 1, null)
            }
        }
    }
}