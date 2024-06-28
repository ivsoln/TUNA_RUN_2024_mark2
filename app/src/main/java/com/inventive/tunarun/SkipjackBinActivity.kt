package com.inventive.tunarun

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import com.inventive.tunarun.Instant.Companion.afterKeyEntered
import com.inventive.tunarun.Instant.Companion.clearResult
import com.inventive.tunarun.Instant.Companion.showResult
import com.inventive.tunarun.Fish.Objects.EntityState
import com.inventive.tunarun.FishClient.Companion.showShift


class SkipjackBinActivity : AppCompatActivity() {


    var bin: Fish.Skipjack.Bin = Fish.Skipjack.Bin()

    lateinit var gotoBlindReceive: TextView
    lateinit var textBarcode: EditText
    lateinit var textOrigin: EditText
    lateinit var textSpecy: EditText
    lateinit var textSpecyDesc: EditText
    lateinit var textSloc: EditText
    lateinit var textBatchNo: EditText
    lateinit var textLotNo: EditText
    lateinit var textWeight: EditText
    lateinit var textQueue: EditText
    lateinit var labelQueue: TextView
    lateinit var viewRun: TextView
    lateinit var viewList: TextView
    lateinit var viewNew: TextView
    lateinit var textResult: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_bin)

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
        labelQueue = findViewById(R.id.labelQueue)

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
                    val que = result as Fish.Skipjack.Queue

                    textResult.showResult(
                        EntityState.WARNING,
                        "${que.entityMessage}\r\n${que.time_stamp}"
                    )


                }

                override fun onError(result: String) {
                    Log.e("TUNA RUN > GET_BIN > ERROR", result)
                }
            }
            skipjack.addQueue(bin, callback)
        }

        viewList.setOnClickListener{
            Intent(this, SkipjackQueListActivity::class.java).also {
                startActivityForResult(it, 0, null)
            }
        }

        openBlind()
    }

    private fun openBlind(){
        Intent(this, BlindReceiveActivity::class.java).also {
            startActivityForResult(it, 10, null)
        }
    }
    private fun getBin(serialNo: String){
        val skipjack = FishClient.SkipjackClient(applicationContext)
        val callback = object : ActionRequest.Callback {
            override fun <T> onSuccess(result: T) {
                val obj = result as Fish.Skipjack.Bin
                bind(obj)

            }

            override fun onError(result: String) {
                Log.e("TUNA RUN > GET_BIN > ERROR", result)
            }
        }
        skipjack.getBin(serialNo, callback)
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 10) {
                val serialNo = data?.getSerializableExtra("SERIAL_NO") as String
                if (serialNo.isNotEmpty()){

                    getBin(serialNo)

                    textBarcode.setText(serialNo)
                    textBarcode.requestFocus()
                    textBarcode.selectAll()
                }
            }
        }
    }
}