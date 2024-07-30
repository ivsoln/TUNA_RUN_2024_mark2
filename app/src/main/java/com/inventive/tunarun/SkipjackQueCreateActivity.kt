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
import com.inventive.tunarun.Fish.Objects.EntityState
import com.inventive.tunarun.FishClient.Companion.REQUEST_BLIND_RECEIVE
import com.inventive.tunarun.FishClient.Companion.showShift
import com.inventive.tunarun.FishClient.Companion.showUser
import com.inventive.tunarun.Instant.Companion.afterKeyEntered
import com.inventive.tunarun.Instant.Companion.afterKeyEnteredThenCloseKeyboard
import com.inventive.tunarun.Instant.Companion.clear
import com.inventive.tunarun.Instant.Companion.clearResult
import com.inventive.tunarun.Instant.Companion.done
import com.inventive.tunarun.Instant.Companion.errorResult
import com.inventive.tunarun.Instant.Companion.focusThenSelectionAll
import com.inventive.tunarun.Instant.Companion.getDecimalWeight
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
    private lateinit var checkMergeBatch: CheckBox
    private lateinit var textRun: TextView
    private lateinit var textResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_que_create)

        findViewById<TextView>(R.id.text_user).showUser()
        findViewById<TextView>(R.id.view_shift).showShift()

        inputBarcode = findViewById(R.id.input_barcode)
        inputBarcode.setText("1-0001|WETU I002322-310001000|20240621-01|IGFK001A|305kg|2202406210000")

        recyclerView = findViewById(R.id.recycler_view)
        textProduct = findViewById(R.id.text_customer)
        inputBatchGroup = findViewById(R.id.input_group)
        textProductType = findViewById(R.id.text_queueType)
        textProductRange = findViewById(R.id.text_queueRange)
        checkMergeBatch = findViewById(R.id.chk_merge_batch_species)
        checkMergeBatch.isChecked = false
        checkMergeBatch.isEnabled = false

        textRun = findViewById(R.id.view_run)
        textResult = findViewById(R.id.text_result)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        bins = mutableListOf()
        adapter = SkipjackBinAdapter(this, bins)
        recyclerView.adapter = adapter

        inputBarcode.afterKeyEnteredThenCloseKeyboard {

            var barcode: String = inputBarcode.text.toString()
            if ((barcode.length == 6) && (barcode.contains("-"))) {
                val skipjack = FishClient.SkipjackClient(applicationContext)
                val callback = object : ActionRequest.Callback {
                    override fun <T> onSuccess(result: T) {
                        val obj = result as Fish.Skipjack.Bin
                        if (obj.state == EntityState.FOUND) {
                            addBin(obj)
                            inputBarcode.clear()
                            inputBarcode.requestFocus()
                        }else{
                            textResult.showResult(obj)
                        }
                    }

                    override fun onError(result: String) {
                        Log.e("TUNA RUN > GET_BIN BY SLOC", result)
                        textResult.showResult(EntityState.WARNING, result)
                    }
                }
                skipjack.getBinBySloc(barcode, callback)
            } else if (barcode.contains("|")) {
                val strs: List<String> = barcode.uppercase().split("|")
                if (strs.size == 6) {
                    var sloc = strs[0]
                    var batch = strs[1]
                    var lot = strs[2]
                    var material = strs[3]
                    var weight = strs[4]
                    var serialNo = strs[5]

                    var br = Fish.Skipjack.Blind.BR()
                    br.shift_id = FishClient.Companion.Skipjack.Shift.Id
                    br.serial_no = serialNo
                    br.batch_no = batch
                    br.lot_no = lot
                    br.weight = weight.getDecimalWeight()
                    br.sloc = sloc
                    br.material_code = material

                    FishClient.Companion.Master.Species.Items.firstOrNull { it.material_code == material && it.species_code.isNotEmpty() }
                        .also {
                            if (it != null) {
                                br.species = it.species_code
                                textResult.clearResult()
                                val skipjack = FishClient.SkipjackClient(applicationContext)
                                val callback = object : ActionRequest.Callback {
                                    override fun <T> onSuccess(result: T) {
                                        val obj = result as Fish.Skipjack.Bin
                                        if (obj.state == EntityState.FOUND) {
                                            addBin(obj)
                                            inputBarcode.clear()
                                            inputBarcode.requestFocus()
                                        }else{
                                            textResult.showResult(obj)
                                            doBarcode(barcode)
                                        }
                                    }

                                    override fun onError(result: String) {
                                        Log.e("TUNA RUN > CREATE BLIND_RECEIVE", result)
                                        textResult.showResult(EntityState.WARNING, result)
                                    }
                                }
                                skipjack.createThenGetBin(br, callback)
                            } else {
                                Log.e("TUNA RUN > NOT_FOUND MATERIAL_SPECIES", material)
                                textResult.showResult(EntityState.ERROR, "NOT_FOUND MATERIAL_SPECIES: $material")
                            }
                        }
                }
                inputBarcode.focusThenSelectionAll()
            } else {
                doBarcode(barcode)
            }
        }

        textRun.setOnClickListener {
            textRun.isEnabled = false
            FishClient.SkipjackClient(applicationContext).also {
                val callback = object : ActionRequest.Callback {
                    override fun <T> onSuccess(result: T) {
                        val obj = result as Fish.Objects.HashSetClient<Fish.Skipjack.Queue>
                        if (obj.state != Fish.Objects.EntityState.SUCCESS) {
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
                var mergeBatch = checkMergeBatch.isChecked;

                var que = Fish.Skipjack.Queue()
                que.date = FishClient.Companion.Skipjack.Shift.Date;
                que.shift = FishClient.Companion.Skipjack.Shift.Shift;
                que.DateShift = FishClient.Companion.Skipjack.Shift;

                que.batch_group_text = groupText
                que.check_merge_batch_species = mergeBatch

                que.Bins.Items = bins
                it.createQueue(que, callback)
            }
        }

        inputBarcode.requestFocus()
    }

    private fun doBarcode(barcode: String) {
        var intent = Intent(this, BlindReceiveActivity::class.java)
        intent.putExtra("REQUEST_CODE", REQUEST_BLIND_RECEIVE)
        intent.putExtra("BARCODE_TEXT", barcode)
        startActivityForResult(intent, REQUEST_BLIND_RECEIVE)
    }


    fun addBin(obj: Fish.Skipjack.Bin) {
        bins.add(0, obj)
        adapter.notifyItemInserted(0)
        recyclerView.scrollToPosition(0)
        textResult.showResult(obj)
    }

    private fun getBin(serialNo: String) {
        FishClient.SkipjackClient(applicationContext).also {
            val callback = object : ActionRequest.Callback {
                override fun <T> onSuccess(result: T) {
                    val obj = result as Fish.Skipjack.Bin
                    addBin(obj)
                    inputBarcode.clear()
                    inputBarcode.requestFocus()
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
                }
            }
        }
    }
}