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
import com.inventive.tunarun.FishClient.Companion.showShift


class BlindReceiveActivity : AppCompatActivity() {


    private var br = Fish.Skipjack.Blind.BR()
    private lateinit var species: Fish.Skipjack.Masters.Species

    private lateinit var gotoListAll: TextView

    private lateinit var inputBarcode: EditText
    private lateinit var captBarcode: TextView


    private lateinit var inputBatchNo: EditText
    private lateinit var inputLotNo: EditText
    private lateinit var inputOrigin: EditText
    private lateinit var inputSpecies: EditText

    private lateinit var inputWeight: EditText

    private lateinit var inputMaterial: EditText

    private lateinit var inputSloc: EditText
    private lateinit var inputStatus: EditText
    private lateinit var inputRemarkDesc: EditText

    private lateinit var actionSave: TextView
    private lateinit var actionUse: TextView


    private lateinit var textResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_blind_receive)

        findViewById<TextView>(R.id.view_shift).showShift()

        gotoListAll = findViewById(R.id.goto_listAll)
        gotoListAll.setOnClickListener {
            val intent = Intent(this, BlindReceiveListActivity::class.java).also {
                startActivityForResult(it, 0, null)
            }
        }

        inputBarcode = findViewById(R.id.input_barcode)
        captBarcode = findViewById(R.id.capt_barcode)
        inputBatchNo = findViewById(R.id.text_batchNo)

        inputLotNo = findViewById(R.id.text_lotNo)
        inputOrigin = findViewById(R.id.text_origin)
        inputSpecies = findViewById(R.id.text_species)

        inputWeight = findViewById(R.id.text_weight)
        inputMaterial = findViewById(R.id.text_material)

        inputSloc = findViewById(R.id.text_sloc)
        inputStatus = findViewById(R.id.text_status)
        inputRemarkDesc = findViewById(R.id.text_remarkDesc)

        actionSave = findViewById(R.id.action_save)
        actionUse = findViewById(R.id.action_use)

        actionSave.isEnabled = false
        actionUse.isEnabled = false

        textResult = findViewById(R.id.text_result)
        textResult.setOnClickListener {
            textResult.clearResult()
        }

        inputBarcode.afterTextChanged {
            inputBarcode.setBackgroundColor(resources.getColor(R.color.Cyan_A200))
        }
        inputBarcode.afterKeyEntered {
            actionSave.isEnabled = false
            actionUse.isEnabled = false
            textResult.clearResult()
            inputBarcode.isEnabled = false
            val barcode = inputBarcode.text.toString().uppercase()
            Log.i("TUNA RUN > READ_BARCODE", barcode)

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
                    inputBarcode.setBackgroundColor(resources.getColor(R.color.Red_200))
                }
                inputBarcode.selectAll()

            }
            inputBarcode.isEnabled = true
            inputBarcode.requestFocus()
        }


        actionSave.setOnClickListener {

            textResult.clearResult()

            br = Fish.Skipjack.Blind.BR()
            br.shift_id = FishClient.Companion.Skipjack.Shift.Id

            br.serial_no = inputBarcode.text.toString()
            br.batch_no = inputBatchNo.text.toString()
            br.lot_no = inputLotNo.text.toString()

            br.origin = inputOrigin.text.toString()
            br.species = inputSpecies.text.toString()

            var wTxt = inputWeight.text.toString()
            if (wTxt.isNotEmpty()) {
                br.weight = wTxt.toDouble()
            }
            br.sloc = inputSloc.text.toString()
            br.material_code = inputMaterial.text.toString()
            br.status = inputStatus.text.toString()
            br.remark = inputRemarkDesc.text.toString()

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
                    Log.e("TUNA RUN > SEARCH ERROR", result)
                    textResult.showResult(EntityState.WARNING, result)

                }
            }
            skipjack.changeBlindReceive(br, callback)
        }
        actionUse.setOnClickListener {

            if (br!!.Id > 0) {
                val intent = Intent()
                intent.putExtra("SERIAL_NO", br.serial_no)
                setResult(RESULT_OK, intent)
                finish()
            }
        }


        val popupStatusList = object : ListItem.Callback(this, "CHOOSE STATUS") {
            override fun onItemSelected(result: ListItem) {
                inputStatus.setText(result.caption)
                inputStatus.setBackgroundColor(resources.getColor(R.color.Light_Green))
            }

            override fun searchTextChanged(listView: ListView, text: String) {

                Log.i("TUNA RUN > SEARCH", text)
                val skipjack = FishClient.SkipjackClient(applicationContext)
                val callback = object : ActionRequest.Callback {
                    override fun <T> onSuccess(result: T) {

                        val list = result as Fish.Objects.HashSetClient<Fish.Skipjack.Blind.Status>

                        items = listOf()
                        //for (i in 1..20) {
                        for (o: Fish.Skipjack.Blind.Status in list.Items) {
                            val w = ListItem()
                            w.id = o.Id
                            w.caption = o.statusText.toString()
                            w.description = ""
                            items = items + w
                        }
                        //}

                        var o0 = list.EntityMessages[0]
                        Toast.makeText(
                            applicationContext,
                            o0.TimeStamp.toString(),
                            Toast.LENGTH_LONG
                        ).show()

                        listView.adapter =
                            ListItem.Adapter(
                                activity,
                                R.layout.activity_search_item_desc,
                                items
                            )
                    }

                    override fun onError(result: String) {
                        Log.e("TUNA RUN > SEARCH ERROR", result)
                        textResult.showResult(EntityState.WARNING, result)

                    }
                }
                skipjack.listBlindStatus(callback)
            }
        }
        inputStatus.setOnLongClickListener {
            Instant.selectionDialog(popupStatusList)
            true
        }


        inputBarcode.setOnTouchListener { v, event ->
            v.onTouchEvent(event)
            val imm = v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
            true
        }
        captBarcode.setOnTouchListener { v, event ->
            v.onTouchEvent(event)
            val imm = v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.showSoftInput(inputBarcode, InputMethodManager.SHOW_IMPLICIT)
            true
        }


        inputBarcode.requestFocus()

    }

    private fun prompt() {
        if (inputBatchNo.text.isNotEmpty() && inputWeight.text.isNotEmpty() && inputMaterial.text.isNotEmpty() && inputSloc.text.isNotEmpty()) {
            actionSave.isEnabled = true
        } else {
            actionSave.isEnabled = false
        }

        if (br.Id > 0) {
            actionUse.isEnabled = true
        } else {
            actionUse.isEnabled = false
        }
    }

    private fun bind(obj: Fish.Skipjack.Blind.BR) {
        br = obj

        inputBarcode.setText(br.serial_no)

        setBatch(br.batch_no)
        setLot(br.lot_no)
        setSloc(br.sloc)
        setMaterial(br.material_code)
        setWeight(br.weight.toString())

        inputSpecies.setText(br.species)
        inputOrigin.setText(br.origin)
        inputStatus.setText(br.status)
        inputRemarkDesc.setText(br.remark)

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

    private fun setLot(barcode: String) {
        inputLotNo.setText(barcode)
        inputLotNo.setBackgroundColor(resources.getColor(R.color.Light_Green))
    }

    private fun setSloc(barcode: String) {
        inputSloc.setText(barcode)
        inputSloc.setBackgroundColor(resources.getColor(R.color.Light_Green))
    }

    private fun setBatch(barcode: String) {
        inputBatchNo.setText(barcode)
        inputBatchNo.setBackgroundColor(resources.getColor(R.color.Light_Green))
    }

    private fun setWeight(barcode: String) {
        var wgt = barcode.uppercase().replace(" ", "")
            .replace("K", "")
            .replace("G", "")
        inputWeight.setText(wgt)
        inputWeight.setBackgroundColor(resources.getColor(R.color.Light_Green))
    }

    private fun setMaterial(barcode: String) {
        inputMaterial.setText(barcode)
        inputMaterial.setBackgroundColor(resources.getColor(R.color.Light_Green))

        FishClient.Companion.Master.Species.Items.firstOrNull { it.material_code == barcode && it.species_code.isNotEmpty() }
            .also {
                if (it != null) {
                    species = it
                    inputSpecies.setText(species.species_code)
                    inputSpecies.setBackgroundColor(resources.getColor(R.color.Light_Green))
                } else {
                    Log.e("TUNA RUN > NOT_FOUND MATERIAL_SPECIES", barcode)
                    inputSpecies.setText("")
                    inputSpecies.setBackgroundColor(resources.getColor(R.color.Red_200))
                }
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // If you have multiple activities returning results then you should include unique request codes for each
        //if (requestCode == <pick a code>) {

        // The result code from the activity started using startActivityForResults
        if (resultCode == Activity.RESULT_OK) {
            val animal = data?.getSerializableExtra("Animal") as String
            Toast.makeText(this, "You clicked $animal", Toast.LENGTH_SHORT).show()
        }
        //}
    }
}