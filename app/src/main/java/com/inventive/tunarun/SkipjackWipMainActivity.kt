package com.inventive.tunarun

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.inventive.tunarun.FishClient.Companion.showShift
import com.inventive.tunarun.FishClient.Companion.showUser
import com.inventive.tunarun.Instant.Companion.afterKeyEntered
import com.inventive.tunarun.Instant.Companion.afterTextChanged
import com.inventive.tunarun.Instant.Companion.popupText
import com.inventive.tunarun.Instant.Companion.showResult
import com.inventive.tunarun.Instant.Companion.toIntOrDefault
import java.text.SimpleDateFormat

class SkipjackWipMainActivity : AppCompatActivity() {

    private lateinit var viewDate: TextView
    private lateinit var viewShift: TextView

    private lateinit var gotoScanBin: TextView
    private lateinit var gotoCreateQueue: TextView
    private lateinit var gotoPrepareRack: TextView

    private lateinit var gotoQueueList: TextView
    private lateinit var gotoGroupList: TextView
    private lateinit var gotoTag: TextView

    lateinit var textQueueNo: EditText

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
        gotoCreateQueue = findViewById(R.id.goto_create_queue)
        gotoPrepareRack = findViewById(R.id.goto_prepareRack)

        gotoQueueList = findViewById(R.id.goto_queue_list)
        gotoQueueList.setOnClickListener {
            Intent(this, SkipjackQueListActivity::class.java).also {
                startActivityForResult(it, 0, null)
            }
        }
        gotoGroupList = findViewById(R.id.goto_group_list)
        gotoGroupList.setOnClickListener {
            Intent(this, SkipjackQueGroupListActivity::class.java).also {
                startActivityForResult(it, 0, null)
            }
        }

        gotoTag = findViewById(R.id.goto_tag)

        viewShift(FishClient.Companion.Skipjack.Shift)

        gotoScanBin.setOnClickListener {
            val intent = Intent(this, SkipjackBinActivity::class.java)
            startActivity(intent)
        }

        //gotoCreateQueue.isEnabled = false
        gotoCreateQueue.setOnClickListener {
            val intent = Intent(this, SkipjackQueCreateActivity::class.java)
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

        textQueueNo = findViewById(R.id.text_queueNo)
        textQueueNo.afterKeyEntered {
            textQueueNo.text.toString().also {
                val intent = Intent(applicationContext, SkipjackQueActivity::class.java)
                intent.putExtra("QUEUE_NO", it)
                startActivity(intent)
            }
        }
        textQueueNo.setOnTouchListener { v, event ->
            v.onTouchEvent(event)
            val imm = v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
            true
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