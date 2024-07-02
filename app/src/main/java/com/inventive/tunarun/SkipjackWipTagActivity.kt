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
import com.inventive.tunarun.FishClient.Companion.showShift
import com.inventive.tunarun.FishClient.Companion.showUser
import com.inventive.tunarun.Instant.Companion.afterKeyEntered
import com.inventive.tunarun.Instant.Companion.afterTextChanged
import com.inventive.tunarun.Instant.Companion.showVCColor
import com.inventive.tunarun.Instant.Companion.stringShortTime
import org.w3c.dom.Text

class SkipjackWipTagActivity : AppCompatActivity() {

    var _queue: Fish.Skipjack.Queue = Fish.Skipjack.Queue()
    var _tag: Fish.Skipjack.Tag = Fish.Skipjack.Tag()
    var _species: Fish.Skipjack.Masters.Species = Fish.Skipjack.Masters.Species()
    var _origin: Fish.Skipjack.Masters.SpeciesOrigin = Fish.Skipjack.Masters.SpeciesOrigin()
    var _size: Fish.Skipjack.Masters.SpeciesSize? = Fish.Skipjack.Masters.SpeciesSize()

    var _tagColor: Fish.Skipjack.Masters.TagColor = Fish.Skipjack.Masters.TagColor();

    var _rack = Fish.Skipjack.Rack()
    var _racks = Fish.Objects.HashSetClient<Fish.Skipjack.Rack>()

    lateinit var textQueueNo: EditText
    lateinit var viewQueColor: TextView
    lateinit var viewColor: TextView

    lateinit var viewSpecy: TextView

    lateinit var viewOrigin: TextView
    lateinit var textSize: EditText
    lateinit var viewColor1: TextView
    lateinit var viewColor2: TextView
    lateinit var viewColor3: TextView
    lateinit var textRack: EditText
    lateinit var textTray: EditText
    lateinit var textEach: EditText
    lateinit var btnSave: TextView
    lateinit var btnNew: TextView

    private lateinit var gotoEdit: TextView

    private val REQUEST_COLOR = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_wip_tag)

        findViewById<TextView>(R.id.text_user).showUser()
        findViewById<TextView>(R.id.view_shift).showShift()

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
        textSize = findViewById(R.id.text_size)
        viewColor1 = findViewById(R.id.view_color1)
        viewColor2 = findViewById(R.id.view_color2)
        viewColor3 = findViewById(R.id.view_color3)
        textRack = findViewById(R.id.text_rackNo)
        textTray = findViewById(R.id.text_tray)
        textEach = findViewById(R.id.text_each)
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
                _species = FishClient.Companion.Master.Species.Items.first { it.Id == result.id }
                viewSpecy.text = _species.species_code
                viewSpecy.setBackgroundColor(resources.getColor(R.color.Light_Green))
            }

            override fun searchTextChanged(listView: ListView, text: String) {
                items = listOf()
                for (o: Fish.Skipjack.Masters.Species in FishClient.Companion.Master.Species.Items) {
                    val w = ListItem()
                    w.id = o.Id
                    w.caption = o.species_code
                    if (o.material_code.isNotEmpty()) {
                        w.caption = "${o.species_code} /${o.material_code}"
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
                _origin =
                    FishClient.Companion.Master.SpeciesOrigin.Items.first { it.Id == result.id }
                viewOrigin.text = _origin.species_origin_code
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

        val popupSizeList = object : ListItem.Callback(this, "CHOOSE SIZE") {
            override fun onItemSelected(result: ListItem) {
                textSize.setText(result.caption)
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
                        R.layout.activity_search_item,
                        items
                    )
            }
        }

        val popupRackList = object : ListItem.Callback(this, "CHOOSE PRE-RACK") {
            override fun onItemSelected(result: ListItem) {
                _rack = _racks.Items.first { it.Id == result.id }
                textRack.setText(_rack.rack_no)
                textRack.setBackgroundColor(resources.getColor(R.color.Light_Green))
            }

            override fun searchTextChanged(listView: ListView, text: String) {
                var date = FishClient.Companion.Skipjack.Shift.Date
                var shift = FishClient.Companion.Skipjack.Shift.Shift
                val skipjack = FishClient.SkipjackClient(applicationContext)
                val callback = object : ActionRequest.Callback {
                    override fun <T> onSuccess(result: T) {
                        _racks = result as Fish.Objects.HashSetClient<Fish.Skipjack.Rack>
                        items = listOf()
                        for (o in _racks.Items) {
                            val w = ListItem()
                            w.id = o.Id
                            w.caption = o.rack_no.toString()
                            w.description = o.rack_arrange_start.stringShortTime()
                            items = items + w
                        }
                        listView.adapter =
                            ListItem.Adapter(
                                activity,
                                R.layout.activity_search_item_desc,
                                items
                            )
                    }

                    override fun onError(result: String) {
                        Log.e("TUNA RUN > GET_PRE_RACKS > ERROR", result)
                    }
                }
                skipjack.getPreRacks(date, shift, callback)
            }
        }

        textRack.setOnLongClickListener {
            Instant.selectionDialog(popupRackList)
            true
        }



        textSize.afterTextChanged { s ->
            FishClient.Companion.Master.SpeciesSize.Items.firstOrNull { it.species_size_code == s }
                .also {
                    _size = it
                    if (_size != null) {
                        textSize.setBackgroundColor(resources.getColor(R.color.Light_Green))
                    } else {
                        textSize.setBackgroundColor(resources.getColor(R.color.Red_A200))
                    }
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

        textSize.setOnLongClickListener {
            Instant.selectionDialog(popupSizeList)
            true
        }
        viewColor.setOnClickListener {
            val intent = Intent(this, SkipjackTagColorActivity::class.java)
            startActivityForResult(intent, REQUEST_COLOR)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val selectedColorId = data?.getIntExtra("color_ID", 0)
            selectedColorId?.let {
                when (requestCode) {
                    REQUEST_COLOR -> {
                        _tagColor = getTagColorCodeById(it)
                        viewColor.setBackgroundColor(Color.parseColor(_tagColor.color_hex))
                        viewColor.text = _tagColor.color_description
                        viewColor.setTextColor(Color.BLACK)
                    }

                    else -> throw IllegalArgumentException("Unknown requestCode: $requestCode")
                }
            }
        }
    }

    private fun getTagColorCodeById(colorId: Int): Fish.Skipjack.Masters.TagColor {
        return FishClient.Companion.Master.TagColor.Items.first { it.Id == colorId }
    }

    fun bind(obj: Fish.Skipjack.Queue) {
        _queue = obj
        _species = _queue.Species
        _origin = _queue.SpeciesOrigin

        viewSpecy.text = _species.species_code
        viewOrigin.text = _origin.species_origin_code

        if (_queue.VCColors.Items.size > 0) {
            _queue.VCColors.Items.forEachIndexed { index, element ->
                when (index) {
                    0 -> {
                        viewColor1.showVCColor(element)
                    }

                    1 -> {
                        viewColor2.showVCColor(element)
                    }

                    2 -> {
                        viewColor3.showVCColor(element)
                    }
                }
            }
        }
    }
}