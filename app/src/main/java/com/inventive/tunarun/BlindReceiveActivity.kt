package com.inventive.tunarun

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.inventive.tunarun.Instant.Companion.afterKeyEntered
import com.inventive.tunarun.Instant.Companion.afterTextChanged
import com.inventive.tunarun.Instant.Companion.clearResult
import com.inventive.tunarun.Instant.Companion.showResult
import com.inventive.tunarun.Fish.Objects.EntityState
import com.inventive.tunarun.FishClient.Companion.REQUEST_BLIND_RECEIVE
import com.inventive.tunarun.FishClient.Companion.showShift
import com.inventive.tunarun.FishClient.Companion.showUser
import com.inventive.tunarun.Instant.Companion.done
import com.inventive.tunarun.Instant.Companion.error
import com.inventive.tunarun.Instant.Companion.focusThenSelectionEnd
import com.inventive.tunarun.Instant.Companion.typing



class BlindReceiveActivity : AppCompatActivity() {


    private var br = Fish.Skipjack.Blind.BR()
    private lateinit var species: Fish.Skipjack.Masters.Species


    private lateinit var inputBarcode: EditText
    private lateinit var captBarcode: TextView

    private lateinit var inputSerialNo: EditText

    private lateinit var inputBatchNo: EditText
    private lateinit var inputLotNo: EditText
    private lateinit var inputSpecies: EditText

    private lateinit var inputWeight: EditText

    private lateinit var inputMaterial: EditText

    private lateinit var inputSloc: EditText

    private lateinit var actionSave: TextView


    private lateinit var textResult: TextView

    private var requestCode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_blind_receive)
        findViewById<TextView>(R.id.text_user).showUser()
        findViewById<TextView>(R.id.view_shift).showShift()

        inputBarcode = findViewById(R.id.input_barcode)
        inputBarcode.setOnLongClickListener {
            var intent = Intent(this, BlindReceiveListActivity::class.java)
            intent.putExtra("REQUEST_CODE", REQUEST_BLIND_RECEIVE)
            startActivityForResult(intent, REQUEST_BLIND_RECEIVE, null)
            true
        }

        inputSerialNo = findViewById(R.id.text_serialNo)
        captBarcode = findViewById(R.id.capt_barcode)
        inputBatchNo = findViewById(R.id.text_batchNo)

        inputLotNo = findViewById(R.id.text_lotNo)
        inputSpecies = findViewById(R.id.text_species)

        inputWeight = findViewById(R.id.text_weight)
        inputMaterial = findViewById(R.id.text_material)

        inputSloc = findViewById(R.id.text_sloc)

        actionSave = findViewById(R.id.action_save)

        actionSave.isEnabled = false

        textResult = findViewById(R.id.text_result)
        textResult.setOnClickListener {
            textResult.clearResult()
        }

        inputBarcode.afterTextChanged {
            inputBarcode.typing()
        }
        inputBarcode.afterKeyEntered {
            actionSave.isEnabled = false
            textResult.clearResult()
            inputBarcode.isEnabled = false
            val barcode = inputBarcode.text.toString().uppercase()
            Log.i("TUNA RUN > READ_BARCODE", barcode)

            doBarcode(barcode)
        }


        actionSave.setOnClickListener {

            textResult.clearResult()

            br = Fish.Skipjack.Blind.BR()
            br.shift_id = FishClient.Companion.Skipjack.Shift.Id

            br.serial_no = inputSerialNo.text.toString()
            br.batch_no = inputBatchNo.text.toString()
            br.lot_no = inputLotNo.text.toString()

            br.species = inputSpecies.text.toString()

            var wTxt = inputWeight.text.toString()
            if (wTxt.isNotEmpty()) {
                br.weight = wTxt.toDouble()
            }
            br.sloc = inputSloc.text.toString()
            br.material_code = inputMaterial.text.toString()

            val skipjack = FishClient.SkipjackClient(applicationContext)
            val callback = object : ActionRequest.Callback {
                override fun <T> onSuccess(result: T) {
                    val obj = result as Fish.Skipjack.Blind.BR
                    if (obj.state == EntityState.OK) {

                        bind(obj)

                        if (requestCode == REQUEST_BLIND_RECEIVE) {
                            if (br!!.Id > 0) {
                                val intent = Intent()
                                intent.putExtra("SERIAL_NO", br.serial_no)
                                setResult(RESULT_OK, intent)
                                finish()
                            }
                        } else {
                            textResult.showResult(
                                EntityState.OK,
                                "${obj.entityMessage}\r\n${obj.time_stamp}"
                            )
                        }
                    } else {
                        textResult.showResult(EntityState.WARNING, obj.entityMessage)
                    }
                }

                override fun onError(result: String) {
                    Log.e("TUNA RUN > SEARCH ERROR", result)
                    textResult.showResult(EntityState.WARNING, result)

                }
            }
            skipjack.changeBlindReceive(br, callback)
        }


        if (intent != null) {
            if (intent.extras != null) {
                requestCode = intent.extras!!.getInt("REQUEST_CODE")
                val barcode = intent.extras!!.getString("BARCODE_TEXT")
                if (barcode != null) {
                    Log.v("TUNA RUN > EXTRA_BARCODE_TEXT", barcode)
                    doBarcode(barcode)
                }
            }
        }


        inputBarcode.setOnTouchListener { v, event ->
            v.onTouchEvent(event)
            val imm = v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
            true
        }
        inputBarcode.requestFocus()
    }

    private fun doBarcode(barcode: String) {
        if (barcode.isNotEmpty()) {
            if (barcode.contains("|")) {
                val strs: List<String> = barcode.uppercase().split("|")
                if (strs.size == 6) {
                    var sloc = strs[0]
                    var batch = strs[1]
                    var lot = strs[2]
                    var material = strs[3]
                    var weight = strs[4]
                    var serialNo = strs[5]

                    inputBarcode.setText(serialNo)

                    setSerialNo(serialNo)

                    setSloc(sloc)
                    setBatch(batch)
                    setLot(lot)
                    setWeight(weight)
                    setMaterial(material)

                    actionSave.isEnabled = true
                } else {
                    textResult.showResult(
                        EntityState.WARNING,
                        "WRONG BARCODE FORMAT\r\n$barcode"
                    )
                }
            } else if (barcode.uppercase().endsWith("KG")) {
                setWeight(barcode)
                inputBarcode.setText("")
                prompt()
            } else if (barcode.length == 6) {
                setSloc(barcode)
                inputBarcode.setText("")
                prompt()
            } else if (barcode.length == 7) {
                setMaterial(barcode)
                inputBarcode.setText("")
                prompt()
            } else if (barcode.length == 8) {
                setMaterial(barcode)
                inputBarcode.setText("")
                prompt()
            } else if ((barcode.length == 13) && (barcode.toBigDecimalOrNull() != null)) {
                getSerialNo(barcode)
            } else if (barcode.length >= 9) {
                setBatch(barcode)
                inputBarcode.setText("")
                prompt()
            } else {
                inputBarcode.error()
            }
            inputBarcode.selectAll()

        }
        inputBarcode.isEnabled = true
        inputBarcode.requestFocus()
    }

    private fun prompt() {
        if (inputBatchNo.text.isNotEmpty()
            && inputWeight.text.isNotEmpty()
            && inputMaterial.text.isNotEmpty()
            && inputSloc.text.isNotEmpty()
        ) {
            actionSave.isEnabled = true
        } else {
            actionSave.isEnabled = false
        }
    }

    private fun bind(obj: Fish.Skipjack.Blind.BR) {
        br = obj
        inputBarcode.setText(br.serial_no)
        setSerialNo(br.serial_no)
        setBatch(br.batch_no)
        setLot(br.lot_no)
        setSloc(br.sloc)
        setMaterial(br.material_code)
        setWeight(br.weight.toString())
        inputSpecies.setText(br.species)

        inputBarcode.focusThenSelectionEnd()
        prompt()

    }

    private fun getSerialNo(serialNo: String) {
        val skipjack = FishClient.SkipjackClient(applicationContext)
        val callback = object : ActionRequest.Callback {
            override fun <T> onSuccess(result: T) {
                val obj = result as Fish.Skipjack.Blind.BR
                if (obj.state == EntityState.OK) {
                    bind(obj)
                    textResult.showResult(
                        EntityState.OK,
                        "${obj.entityMessage}\r\n${obj.time_stamp}"
                    )
                } else {
                    textResult.showResult(EntityState.WARNING, obj.entityMessage)
                }
            }

            override fun onError(result: String) {
                Log.e("TUNA RUN > GET ERROR", result)
                textResult.showResult(EntityState.WARNING, result)
            }
        }
        skipjack.getBlindReceive(serialNo, callback)

    }

    private fun setSerialNo(serialNo: String) {
        inputSerialNo.done(serialNo)
    }

    private fun setLot(barcode: String) {
        inputLotNo.done(barcode)
    }

    private fun setSloc(barcode: String) {
        inputSloc.done(barcode)
    }

    private fun setBatch(barcode: String) {
        inputBatchNo.done(barcode)
    }

    private fun setWeight(barcode: String) {
        var wgt = barcode.uppercase().replace(" ", "")
            .replace("K", "")
            .replace("G", "")
        inputWeight.done(wgt)
    }

    private fun setMaterial(barcode: String) {
        inputMaterial.done(barcode)

        FishClient.Companion.Master.Species.Items.firstOrNull { it.material_code == barcode && it.species_code.isNotEmpty() }
            .also {
                if (it != null) {
                    species = it
                    inputSpecies.done(species.species_code)
                } else {
                    Log.e("TUNA RUN > NOT_FOUND MATERIAL_SPECIES", barcode)
                    inputSpecies.setText("")
                    inputSpecies.setBackgroundColor(resources.getColor(R.color.Red_200))
                }
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_BLIND_RECEIVE) {
                val serialNo = data?.getStringExtra("SERIAL_NO") as String
                if (serialNo.isNotEmpty()) {
                    getSerialNo(serialNo)
                }
            }

        }
    }
}