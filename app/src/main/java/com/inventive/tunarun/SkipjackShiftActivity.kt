package com.inventive.tunarun

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.inventive.tunarun.Instant.Companion.popupText
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SkipjackShiftActivity : AppCompatActivity() {

    private lateinit var textDatePicker: TextView
    private lateinit var textDS: TextView
    private lateinit var textNS: TextView
    private var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_shift)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textDatePicker = findViewById(R.id.text_date_picker)
        textDatePicker.setOnClickListener {
            showDatePicker()
        }

        FishClient.Companion.Skipjack.Shift.Date?.let {
            calendar.time = it
            viewDate()
        }

        textDS = findViewById(R.id.text_ds)
        textNS = findViewById(R.id.text_ns)

        textDS.setOnClickListener {
            popupText(calendar.time.toString())
            getShift(calendar.time, 1)
        }
        textNS.setOnClickListener {
            popupText(calendar.time.toString())
            getShift(calendar.time, 2)
        }
    }

    private fun getShift(date: Date, shift: Int) {
        FishClient.SkipjackClient(this).also {
            var callback = object : ActionRequest.Callback {
                override fun <T> onSuccess(result: T) {
                    FishClient.Companion.Skipjack.Shift = result as Fish.Skipjack.DateShift
                    Log.i("TUNA RUN > GET_SHIFT > SUCCESS", "OK")
                    setResult(RESULT_OK)
                    finish()
                }

                override fun onError(result: String) {
                    Log.e("TUNA RUN > GET_SHIFT > ERROR", result)
                }
            }
            it.getShift(date, shift, callback)
        }
    }

    private fun viewDate() {
        textDatePicker.text = SimpleDateFormat(Instant.dateFormat).format(calendar.time)
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this, { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0)
                viewDate()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).also {
            it.show()
        }
    }
}