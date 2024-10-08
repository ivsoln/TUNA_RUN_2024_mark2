package com.inventive.tunarun

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.RecyclerView
import com.inventive.tunarun.Instant.Companion.afterKeyEntered
import com.inventive.tunarun.Instant.Companion.clearResult
import com.inventive.tunarun.Instant.Companion.showResult
import com.inventive.tunarun.Fish.Objects.EntityState
import com.inventive.tunarun.FishClient.Companion.REQUEST_BLIND_RECEIVE
import com.inventive.tunarun.FishClient.Companion.showShift
import com.inventive.tunarun.FishClient.Companion.showUser
import com.inventive.tunarun.Instant.Companion.errorResult


class SkipjackBinActivity : AppCompatActivity() {


    private var bin: Fish.Skipjack.Bin = Fish.Skipjack.Bin()
    private  var que: Fish.Skipjack.Queue = Fish.Skipjack.Queue()

    private var queType: Fish.Skipjack.Masters.QueueType = Fish.Skipjack.Masters.QueueType()
    private var queRange: Fish.Skipjack.Masters.QueueRange = Fish.Skipjack.Masters.QueueRange()

    private lateinit var gotoBlindReceive: TextView
    private lateinit var textBarcode: EditText
    private lateinit var textOrigin: EditText
    private lateinit var textSpecy: EditText
    private lateinit var textSpecyDesc: EditText
    private lateinit var textSloc: EditText
    private lateinit var textBatchNo: EditText
    private lateinit var textLotNo: EditText
    private lateinit var textWeight: EditText
    private lateinit var textQueue: TextView
    private lateinit var textQueueType: TextView
    private lateinit var viewRun: TextView
    private lateinit var viewList: TextView
    private lateinit var viewNew: TextView
    private lateinit var textResult: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_bin)

        findViewById<TextView>(R.id.text_user).showUser()
        findViewById<TextView>(R.id.view_shift).showShift()

        textBarcode = findViewById(R.id.text_barcode)
        textOrigin = findViewById(R.id.text_origin)
        textSpecy = findViewById(R.id.text_specy)
        textSpecyDesc = findViewById(R.id.text_specyDesc)
        textSloc = findViewById(R.id.text_sloc)
        textBatchNo = findViewById(R.id.text_batchNo)
        textLotNo = findViewById(R.id.text_lotNo)
        textWeight = findViewById(R.id.text_weight)
        textQueue = findViewById(R.id.text_queue)
        textQueueType = findViewById(R.id.text_queueType)

        textResult = findViewById(R.id.text_result)
        textResult.setOnClickListener {
            textResult.clearResult()
        }

        viewRun = findViewById(R.id.view_run)
        viewList = findViewById(R.id.view_list)
        viewNew = findViewById(R.id.view_new)

        gotoBlindReceive = findViewById(R.id.goto_blindReceive)
        gotoBlindReceive.setOnClickListener {
            openBlind()
        }

        textBarcode.afterKeyEntered {
            val barcode = textBarcode.text.toString()
            Log.i("TUNA RUN > GET_BIN", barcode)
            getBin(barcode)
        }

        viewRun.setOnClickListener {

            textResult.clearResult()
            bin.date = FishClient.Companion.Skipjack.Shift.Date
            bin.shift = FishClient.Companion.Skipjack.Shift.Shift

            val skipjack = FishClient.SkipjackClient(applicationContext)
            val callback = object : ActionRequest.Callback {
                override fun <T> onSuccess(result: T) {
                    var obj = result as Fish.Skipjack.Queue
                    bind(obj)
                    textResult.showResult(que)
                }

                override fun onError(result: String) {
                    Log.e("TUNA RUN > GET_BIN > ERROR", result)
                    textResult.errorResult(result)
                }
            }
            skipjack.addQueue(bin, callback)
        }

        viewList.setOnClickListener {
            Intent(this, SkipjackQueListActivity::class.java).also {
                startActivityForResult(it, 0)
            }
        }

        viewNew.setOnClickListener {
            clrscr()
        }


        openBlind()
    }

    private fun openBlind() {
        Intent(this, BlindReceiveActivity::class.java).also {
            it.putExtra("REQUEST_CODE", REQUEST_BLIND_RECEIVE)
            startActivityForResult(it, FishClient.REQUEST_BLIND_RECEIVE)
        }
    }

    private fun getBin(serialNo: String) {
        FishClient.SkipjackClient(applicationContext).also {
            val callback = object : ActionRequest.Callback {
                override fun <T> onSuccess(result: T) {
                    val obj = result as Fish.Skipjack.Bin
                    bind(obj)
                }
                override fun onError(result: String) {
                    textResult.errorResult(result)
                    Log.e("TUNA RUN > GET_BIN > ERROR", result)
                }
            }
            it.getBin(serialNo, callback)
        }
    }

    private fun bind(obj: Fish.Skipjack.Bin) {
        bin = obj

        textSpecy.setText(bin.species_code)
        textSpecyDesc.setText(bin.material_code)
        textOrigin.setText(bin.species_origin_code)
        textSloc.setText(bin.location_description)
        textBatchNo.setText(bin.batch_no)
        textLotNo.setText(bin.lot_no)
        textWeight.setText(bin.net_weight.toString())

        queRange = FishClient.Companion.Master.QueueRanges.Items.first()
        textQueue.text = queRange.queue_range_description

        queType = FishClient.Companion.Master.QueueTypes.Items.first()
        textQueueType.text = ""
    }

    private fun bind(obj: Fish.Skipjack.Queue) {
        que = obj
        if (que.Id > 0) {
            queType = que.QueueType
            queRange = que.QueueRange

            textQueue.text = que.queue_no.toString()
            textQueueType.text = queType.queue_type_code


        } else {
            textSpecy.setText("")
            textSpecyDesc.setText("")
            textOrigin.setText("")
            textSloc.setText("")
            textBatchNo.setText("")
            textLotNo.setText("")
            textWeight.setText("")

            textBarcode.setText("")
            textBarcode.requestFocus()
        }
    }

    private fun clrscr() {
        bin = Fish.Skipjack.Bin()
        bind(Fish.Skipjack.Queue())
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FishClient.REQUEST_BLIND_RECEIVE) {
                val serialNo = data?.getStringExtra("SERIAL_NO") as String
                if (serialNo.isNotEmpty()) {

                    getBin(serialNo)

                    textBarcode.setText(serialNo)
                    textBarcode.requestFocus()
                    textBarcode.selectAll()
                }
            }
        }
    }
}
