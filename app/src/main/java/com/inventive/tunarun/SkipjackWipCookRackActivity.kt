package com.inventive.tunarun

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.inventive.tunarun.FishClient.Companion.dialogPreRackList
import com.inventive.tunarun.FishClient.Companion.showShift
import com.inventive.tunarun.FishClient.Companion.showUser
import com.inventive.tunarun.Instant.Companion.afterKeyEntered
import com.inventive.tunarun.Instant.Companion.afterTextChanged
import com.inventive.tunarun.Instant.Companion.clearResult
import com.inventive.tunarun.Instant.Companion.done
import com.inventive.tunarun.Instant.Companion.errorResult
import com.inventive.tunarun.Instant.Companion.focusThenSelectionAll
import com.inventive.tunarun.Instant.Companion.focusThenSelectionEnd
import com.inventive.tunarun.Instant.Companion.showResult
import com.inventive.tunarun.Instant.Companion.showShortTime

class SkipjackWipCookRackActivity : AppCompatActivity() {


    var rack: Fish.Skipjack.Rack = Fish.Skipjack.Rack()

    lateinit var textRackNo: EditText
    lateinit var textStartTime: EditText
    lateinit var textResult: TextView
    lateinit var textCancelRack: TextView

    private var requestCode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_wip_cook_rack)

        findViewById<TextView>(R.id.text_user).showUser()
        findViewById<TextView>(R.id.view_shift).showShift()

        textRackNo = findViewById(R.id.text_rackNo)
        textStartTime = findViewById(R.id.text_startTime)
        textResult = findViewById(R.id.text_result)
        textCancelRack = findViewById(R.id.text_cancel_rack)

        textCancelRack.isVisible = false

        textRackNo.afterKeyEntered {
            textResult.clearResult()
            textCancelRack.setOnClickListener { }
            var date = FishClient.Companion.Skipjack.Shift.Date
            var shift = FishClient.Companion.Skipjack.Shift.Shift
            var rackNo = textRackNo.text.toString()
            val skipjack = FishClient.SkipjackClient(applicationContext)
            val callback = object : ActionRequest.Callback {
                override fun <T> onSuccess(result: T) {
                    val obj = result as Fish.Skipjack.Rack
                    bind(obj)
                }

                override fun onError(result: String) {
                    Log.e("TUNA RUN > PRE-PARE_RACK > ERROR", result)
                }
            }
            skipjack.preRack(date, shift, rackNo, callback)
        }
        textRackNo.afterTextChanged {
            textResult.clearResult()
            textStartTime.setText("")
            textCancelRack.isVisible = false
        }


        textRackNo.setOnLongClickListener {
            val callback = object : ActionRequest.Callback {
                override fun <T> onSuccess(result: T) {
                    var rack = result as Fish.Skipjack.Rack
                    bind(rack)
                }

                override fun onError(result: String) {
                    textResult.showResult(Fish.Objects.EntityState.WARNING, result)
                }
            }

            this.dialogPreRackList(callback)
            true
        }

        textRackNo.setOnTouchListener { v, event ->
            v.onTouchEvent(event)
            val imm = v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
            true
        }

        if (intent != null) {
            if (intent.extras != null) {
                requestCode = intent.extras!!.getInt("REQUEST_CODE")
                val rackNo = intent.extras!!.getString("RACK_NO")
                if (rackNo != null) {
                    if (rackNo.isNotBlank()) {
                        textRackNo.setText(rackNo)
                    }
                }
            }
        }

        textStartTime.afterKeyEntered {
            if (rack != null) {
                if ((rack.Id > 0) && (requestCode == 30)) {
                    val intent = Intent()
                    intent.putExtra("RACK_NO", rack.rack_no)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }

        textRackNo.focusThenSelectionEnd()
    }

    fun bind(obj: Fish.Skipjack.Rack) {
        textCancelRack.isVisible = false
        rack = obj
        textRackNo.setText(rack.rack_no)
        textStartTime.showShortTime(rack.rack_arrange_start)

        if (rack.state == Fish.Objects.EntityState.ERROR) {
            textResult.errorResult(rack.entityMessage)
        } else {
            textResult.showResult(
                rack.state,
                "${rack.entityMessage}\r\n${rack.rack_arrange_start}"
            )
            if (rack.state == Fish.Objects.EntityState.SUCCESS) {
                textRackNo.done()
            } else {
                textCancelRack.isVisible = true
                textCancelRack.setOnClickListener { cancelRack() }
            }
        }
        if (requestCode == 30) {
            textStartTime.focusThenSelectionEnd()
        } else {
            textRackNo.focusThenSelectionAll()
        }
    }

    private fun cancelRack() {
        textResult.clearResult()
        if (rack.Id > 0) {
            var date = FishClient.Companion.Skipjack.Shift.Date
            var shift = FishClient.Companion.Skipjack.Shift.Shift
            var rackNo = textRackNo.text.toString()
            val skipjack = FishClient.SkipjackClient(applicationContext)
            val callback = object : ActionRequest.Callback {
                override fun <T> onSuccess(result: T) {
                    val obj = result as Fish.Skipjack.Rack
                    textResult.showResult(
                        Fish.Objects.EntityState.WARNING,
                        "${obj.entityMessage}"
                    )
                    textStartTime.setText("")
                    textCancelRack.isVisible = false
                    textRackNo.focusThenSelectionAll()
                }

                override fun onError(result: String) {
                    Log.e("TUNA RUN > CANCEL_RACK > ERROR", result)
                }
            }
            skipjack.deleteRack(date, shift, rackNo, callback)
        }
    }
}