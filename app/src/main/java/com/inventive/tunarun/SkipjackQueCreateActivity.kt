package com.inventive.tunarun

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.inventive.tunarun.FishClient.Companion.REQUEST_BLIND_RECEIVE
import com.inventive.tunarun.FishClient.Companion.showShift
import com.inventive.tunarun.FishClient.Companion.showUser
import com.inventive.tunarun.Instant.Companion.afterKeyEntered
import com.inventive.tunarun.Instant.Companion.errorResult
import com.inventive.tunarun.Instant.Companion.showResult

class SkipjackQueCreateActivity : AppCompatActivity() {

    private lateinit var adapter: SkipjackBinAdapter
    private lateinit var bins: MutableList<Fish.Skipjack.Bin>

    private lateinit var inputBarcode: EditText
    private lateinit var recyclerView: RecyclerView

    private lateinit var textProduct: TextView
    private lateinit var inputBatchGroup: EditText
    private lateinit var textProductType: TextView
    private lateinit var textProductRange: TextView
    private lateinit var checkMerge: CheckBox
    private lateinit var textRun: TextView
    private lateinit var textResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_que_create)

        findViewById<TextView>(R.id.text_user).showUser()
        findViewById<TextView>(R.id.view_shift).showShift()

        inputBarcode = findViewById(R.id.input_barcode)
        recyclerView = findViewById(R.id.recycler_view)
        textProduct = findViewById(R.id.text_customer)
        inputBatchGroup = findViewById(R.id.input_group)
        textProductType = findViewById(R.id.text_queueType)
        textProductRange = findViewById(R.id.text_queueRange)
        checkMerge = findViewById(R.id.chk_merge_batch_species)
        checkMerge.isChecked = true
        checkMerge.isEnabled = false

        textRun = findViewById(R.id.view_run)
        textResult = findViewById(R.id.text_result)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        bins = mutableListOf()
        adapter = SkipjackBinAdapter(this, bins)
        recyclerView.adapter = adapter

        inputBarcode.afterKeyEntered {
            inputBarcode.text.toString().also {
                var intent = Intent(this, BlindReceiveActivity::class.java)
                intent.putExtra("REQUEST_CODE", REQUEST_BLIND_RECEIVE)
                intent.putExtra("BARCODE_TEXT", it)
                startActivityForResult(intent, REQUEST_BLIND_RECEIVE)
            }
        }

        textRun.setOnClickListener {
            textRun.isEnabled = false
            FishClient.SkipjackClient(applicationContext).also {
                val callback = object : ActionRequest.Callback {
                    override fun <T> onSuccess(result: T) {
                        val obj = result as Fish.Objects.HashSetClient<Fish.Skipjack.Queue>

                        if (obj.state != Fish.Objects.EntityState.SUCCESS){
                            textRun.isEnabled = true
                        }
                        textResult.showResult(obj)
                    }

                    override fun onError(result: String) {
                        textResult.errorResult(result)
                        Log.e("TUNA RUN > GET_BIN > ERROR", result)
                    }
                }

                var groupText = inputBatchGroup.text.toString()
                var que = Fish.Skipjack.Queue()
                que.date = FishClient.Companion.Skipjack.Shift.Date;
                que.shift = FishClient.Companion.Skipjack.Shift.Shift;
                que.DateShift = FishClient.Companion.Skipjack.Shift;

                que.batch_group_text = groupText

                que.Bins.Items = bins
                it.createQueue(que, callback)
            }
        }

        inputBarcode.requestFocus()
    }

    private fun getBin(serialNo: String) {
        FishClient.SkipjackClient(applicationContext).also {
            val callback = object : ActionRequest.Callback {
                override fun <T> onSuccess(result: T) {

                    val obj = result as Fish.Skipjack.Bin

                    bins.add(0, obj)
                    adapter.notifyItemInserted(0)
                    recyclerView.scrollToPosition(0)
                    textResult.showResult(obj)
                }

                override fun onError(result: String) {
                    textResult.errorResult(result)
                    Log.e("TUNA RUN > GET_BIN > ERROR", result)
                }
            }
            it.getBin(serialNo, callback)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_BLIND_RECEIVE) {
                val serialNo = data?.getStringExtra("SERIAL_NO") as String
                if (serialNo.isNotEmpty()) {
                    getBin(serialNo)
                    inputBarcode.setText("")
                    inputBarcode.requestFocus()
                }
            }
        }
    }
}