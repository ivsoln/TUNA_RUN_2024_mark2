package com.inventive.tunarun

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.inventive.tunarun.Instant.Companion.afterKeyEntered

class SkipjackWipTagActivity : AppCompatActivity() {

    var queue: Fish.Skipjack.Queue = Fish.Skipjack.Queue()
    lateinit var textQueueNo: EditText
    lateinit var viewQueColor: TextView
    lateinit var viewColor: TextView

    lateinit var viewSpecy: TextView

    lateinit var viewOrigin: TextView
    lateinit var viewSize: TextView
    lateinit var viewColor1: TextView
    lateinit var viewColor2: TextView
    lateinit var viewColor3: TextView
    lateinit var textRackNo: EditText
    lateinit var textTray: EditText
    lateinit var textFrac: EditText
    lateinit var btnSave: TextView
    lateinit var btnNew: TextView

    private lateinit var gotoEdit: TextView

    private val REQUEST_COLOR = 0
    private val REQUEST_COLOR1 = 1
    private val REQUEST_COLOR2 = 2
    private val REQUEST_COLOR3 = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_wip_tag)


        gotoEdit = findViewById(R.id.goto_edit)
        gotoEdit.setOnClickListener {
            val intent = Intent(this, SkipjackWipTagEditActivity::class.java)
            startActivity(intent)
        }

        textQueueNo = findViewById(R.id.text_queueNo)
        viewQueColor = findViewById(R.id.view_QueColor)
        viewColor = findViewById(R.id.view_col)

        viewSpecy = findViewById(R.id.view_specy)

        viewOrigin = findViewById(R.id.view_origin)
        viewSize = findViewById(R.id.view_size)
        viewColor1 = findViewById(R.id.view_color1)
        viewColor2 = findViewById(R.id.view_color2)
        viewColor3 = findViewById(R.id.view_color3)
        textRackNo = findViewById(R.id.text_rackNo)
        textTray = findViewById(R.id.text_tray)
        textFrac = findViewById(R.id.text_frac)
        btnSave = findViewById(R.id.btn_save)
        btnNew = findViewById(R.id.btn_new)


        textQueueNo.afterKeyEntered {

            var txtQue = textQueueNo.text.toString()
            if (txtQue.isNotEmpty()) {
                var date = FishClient.Companion.Skipjack.Shift.Date
                var shift = FishClient.Companion.Skipjack.Shift.Shift
                var queNo = txtQue.toInt()
                val skipjack = FishClient.SkipjackClient(applicationContext)
                val callback = object : ActionRequest.Callback {
                    override fun <T> onSuccess(result: T) {
                        val obj = result as Fish.Skipjack.Queue
                        bind(obj)
                    }

                    override fun onError(result: String) {
                        Log.e("TUNA RUN > GET_BIN > ERROR", result)
                    }
                }
                skipjack.getQueue(date, shift, queNo, callback)
            } else {

                //ALERT
            }

        }


        val popupSpeciesList = object : ListItem.Callback(this, "CHOOSE SPECIES") {
            override fun onItemSelected(result: ListItem) {
                viewSpecy.setText(result.caption)
                viewSpecy.setBackgroundColor(resources.getColor(R.color.Light_Green))
            }

            override fun searchTextChanged(listView: ListView, text: String) {
                items = listOf()
                for (o: Fish.Skipjack.Masters.Species in FishClient.Companion.Master.Species.Items) {
                    val w = ListItem()
                    w.id = o.Id
                    if (o.material_code.isNotEmpty()) {
                        w.caption =
                                //o.material_code.toString() + " (" + o.species_code.toString() + ")"
                            o.material_code.toString()
                    } else {
                        w.caption = o.species_code.toString()
                    }
                    w.description = o.species_description.toString()
                    items = items + w
                }
                listView.adapter =
                    ListItem.Adapter(
                        activity,
                        R.layout.activity_search_item_desc,
                        items
                    )
            }
        }

        val popupOriginList = object : ListItem.Callback(this, "CHOOSE ORIGIN") {
            override fun onItemSelected(result: ListItem) {
                viewOrigin.setText(result.description)
                viewOrigin.setBackgroundColor(resources.getColor(R.color.Light_Green))
            }

            override fun searchTextChanged(listView: ListView, text: String) {
                items = listOf()
                for (o: Fish.Skipjack.Masters.SpeciesOrigin in FishClient.Companion.Master.SpeciesOrigin.Items) {
                    val w = ListItem()
                    w.id = o.Id
                    w.caption = o.species_origin_code.toString()
                    w.description = o.species_origin_description.toString()
                    items = items + w
                }
                listView.adapter =
                    ListItem.Adapter(
                        activity,
                        R.layout.activity_search_item_desc,
                        items
                    )
            }
        }

        val popupSizeList = object : ListItem.Callback(this, "CHOOSE ORIGIN") {
            override fun onItemSelected(result: ListItem) {
                viewSize.setText(result.caption)
                viewSize.setBackgroundColor(resources.getColor(R.color.Light_Green))
            }

            override fun searchTextChanged(listView: ListView, text: String) {
                items = listOf()
                for (o: Fish.Skipjack.Masters.SpeciesSize in FishClient.Companion.Master.SpeciesSize.Items) {
                    val w = ListItem()
                    w.id = o.Id
                    w.caption = o.species_size_code.toString()
                    w.description = o.species_size_description.toString()
                    items = items + w
                }
                listView.adapter =
                    ListItem.Adapter(
                        activity,
                        R.layout.activity_search_item_desc,
                        items
                    )
            }
        }

        viewSpecy.setOnLongClickListener {
            Instant.selectionDialog(popupSpeciesList)
            true
        }

        viewOrigin.setOnLongClickListener {
            Instant.selectionDialog(popupOriginList)
            true
        }

        viewSize.setOnLongClickListener {
            Instant.selectionDialog(popupSizeList)
            true
        }
        viewColor.setOnClickListener {
            val intent = Intent(this, SkipjackTagColorActivity::class.java)
            startActivityForResult(intent, REQUEST_COLOR)
        }
        viewColor1.setOnClickListener {
            val intent = Intent(this, SkipjackColorActivity::class.java)
            startActivityForResult(intent, REQUEST_COLOR1)
        }
        viewColor2.setOnClickListener {
            val intent = Intent(this, SkipjackColorActivity::class.java)
            startActivityForResult(intent, REQUEST_COLOR2)
        }
        viewColor3.setOnClickListener {
            val intent = Intent(this, SkipjackColorActivity::class.java)
            startActivityForResult(intent, REQUEST_COLOR3)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val selectedColorId = data?.getIntExtra("color_ID", 0)
            selectedColorId?.let {
                when (requestCode) {
                    REQUEST_COLOR -> {
                        val selectedColor = getTagColorCodeById(it)
                        viewColor.setBackgroundColor(Color.parseColor(selectedColor.color_hex))
                        viewColor.text = selectedColor.color_description
                        viewColor.setTextColor(Color.BLACK)
                    }

                    REQUEST_COLOR1 -> {
                        val selectedColor = getColorCodeById(it)
                        viewColor1.setBackgroundColor(Color.parseColor(selectedColor.color_hex))
                        viewColor1.text = selectedColor.color_description
                        viewColor1.setTextColor(Color.BLACK)
                    }

                    REQUEST_COLOR2 -> {
                        val selectedColor = getColorCodeById(it)
                        viewColor2.setBackgroundColor(Color.parseColor(selectedColor.color_hex))
                        viewColor2.text = selectedColor.color_description
                        viewColor2.setTextColor(Color.BLACK)
                    }

                    REQUEST_COLOR3 -> {
                        val selectedColor = getColorCodeById(it)
                        viewColor3.setBackgroundColor(Color.parseColor(selectedColor.color_hex))
                        viewColor3.text = selectedColor.color_description
                        viewColor3.setTextColor(Color.BLACK)
                    }

                    else -> throw IllegalArgumentException("Unknown requestCode: $requestCode")
                }
            }
        }
    }


    private fun getColorCodeById(colorId: Int): Fish.Skipjack.Masters.VCColor {
        return FishClient.Companion.Master.VCColor.Items.first { it.Id == colorId }
    }

    private fun getTagColorCodeById(colorId: Int): Fish.Skipjack.Masters.TagColor {
        return FishClient.Companion.Master.TagColor.Items.first { it.Id == colorId }
    }

    fun bind(obj: Fish.Skipjack.Queue) {
        queue = obj

        viewSpecy.text = queue.species_code
        //viewOrigin.text = queue.species_origin_code
        //viewSize.text =
    }
}