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
import com.inventive.tunarun.FishClient.Companion.dialogPreRackList
import com.inventive.tunarun.FishClient.Companion.dialogQueueList
import com.inventive.tunarun.FishClient.Companion.dialogSpeciesList
import com.inventive.tunarun.FishClient.Companion.dialogSpeciesOriginList
import com.inventive.tunarun.FishClient.Companion.dialogSpeciesSizeList
import com.inventive.tunarun.FishClient.Companion.showShift
import com.inventive.tunarun.FishClient.Companion.showUser
import com.inventive.tunarun.Instant.Companion.afterKeyEntered
import com.inventive.tunarun.Instant.Companion.afterTextChanged
import com.inventive.tunarun.Instant.Companion.cancel
import com.inventive.tunarun.Instant.Companion.clear
import com.inventive.tunarun.Instant.Companion.clearResult
import com.inventive.tunarun.Instant.Companion.done
import com.inventive.tunarun.Instant.Companion.errorResult
import com.inventive.tunarun.Instant.Companion.focusThenSelectionAll
import com.inventive.tunarun.Instant.Companion.focusThenSelectionEnd
import com.inventive.tunarun.Instant.Companion.showResult
import com.inventive.tunarun.Instant.Companion.showVCColor
import com.inventive.tunarun.Instant.Companion.stringShortTime
import com.inventive.tunarun.Instant.Companion.toIntOrDefault
import com.inventive.tunarun.Instant.Companion.typing
import com.inventive.tunarun.Instant.Companion.warningResult
import org.w3c.dom.Text

class SkipjackWipTagActivity : AppCompatActivity() {

    private var _queue: Fish.Skipjack.Queue = Fish.Skipjack.Queue()
    private var _tag: Fish.Skipjack.Tag = Fish.Skipjack.Tag()
    private var _species: Fish.Skipjack.Masters.Species = Fish.Skipjack.Masters.Species()
    private var _size: Fish.Skipjack.Masters.SpeciesSize = Fish.Skipjack.Masters.SpeciesSize()

    private var _tagColor: Fish.Skipjack.Masters.TagColor = Fish.Skipjack.Masters.TagColor();

    private var _rack = Fish.Skipjack.Rack()
    private var _racks = Fish.Objects.HashSetClient<Fish.Skipjack.Rack>()

    private var _trayCount: Int = 0
    private var _eachCount: Int = 0

    private lateinit var textQueue: EditText
    private lateinit var viewColor: TextView

    private lateinit var viewSpecy: TextView

    private lateinit var viewOrigin: TextView
    private lateinit var textSize: EditText
    private lateinit var viewColor1: TextView
    private lateinit var viewColor2: TextView
    private lateinit var viewColor3: TextView
    private lateinit var textRack: EditText
    private lateinit var textTray: EditText
    private lateinit var textEach: EditText
    private lateinit var btnSave: TextView
    private lateinit var btnNew: TextView
    private lateinit var gotoEdit: TextView
    private lateinit var textResult: TextView
    private var REQUEST_COLOR = 0
    private var REQUEST_RACK = 30

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

        textQueue = findViewById(R.id.text_queueNo)
        viewColor = findViewById(R.id.view_tag_color)

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
        btnSave.isEnabled = false

        btnNew = findViewById(R.id.btn_new)
        btnNew.setOnClickListener {
            _tag = Fish.Skipjack.Tag()
            Fish.Skipjack.Queue().also { bind(it) }
            Fish.Skipjack.Rack().also { bind(it) }
            textQueue.focusThenSelectionAll()
        }

        textResult = findViewById(R.id.text_result)

        textQueue.afterTextChanged {
            textQueue.typing()
            clearRack()
            clearQueue()
        }
        textQueue.afterKeyEntered {
            textResult.clearResult()
            textQueue.text.toString().toIntOrDefault(0).also {
                if (it > 0) {
                    val skipjack = FishClient.SkipjackClient(applicationContext)
                    val callback = object : ActionRequest.Callback {
                        override fun <T> onSuccess(result: T) {
                            val obj = result as Fish.Skipjack.Queue
                            bind(obj)
                        }

                        override fun onError(result: String) {
                            Log.e("TUNA RUN > GET_BIN > ERROR", result)
                            textResult.showResult(Fish.Objects.EntityState.ERROR, result)
                        }
                    }
                    skipjack.getQueue(it, callback)
                }
            }
        }
        textQueue.setOnLongClickListener {
            selectQueue()
            true
        }

        viewColor.setOnClickListener {
            val intent = Intent(this, SkipjackColorActivity::class.java)
            startActivityForResult(intent, REQUEST_COLOR)
        }

        Log.i(
            "TUNA RUN > TAG_COLOR_COUNT:",
            FishClient.Companion.Master.TagColor.Items.size.toString()
        )
        Log.i("TUNA RUN > SHIFT:", FishClient.Companion.Skipjack.Shift.Shift.toString())

        getTagColorCodeById(FishClient.Companion.Skipjack.Shift.Shift).also { bind(it) }

        textRack.afterTextChanged {
            textRack.typing()
            textResult.clearResult()
        }
        textRack.afterKeyEntered {
            var rackNo = textRack.text.toString()
            if (rackNo.isNotEmpty()) {
                getRack(rackNo)
            }
        }

        textSize.afterTextChanged { x ->
            FishClient.Companion.Master.SpeciesSize.Items.firstOrNull { it.species_size_code == x }
                .also {
                    bind(it)
                }
        }
        textSize.afterKeyEntered {
            if ((_size != null) && (_size!!.Id > 0)) {
                textRack.focusThenSelectionAll()
            }
        }

        viewSpecy.setOnLongClickListener {

            val callback = object : ActionRequest.Callback {
                override fun <T> onSuccess(result: T) {
                    _species = result as Fish.Skipjack.Masters.Species
                    viewSpecy.text = _species.species_code
                    viewSpecy.setBackgroundColor(resources.getColor(R.color.Light_Green))
                }

                override fun onError(result: String) {
                    textResult.showResult(Fish.Objects.EntityState.WARNING, result)
                }
            }

            this.dialogSpeciesList(callback)
            true
        }
        textSize.setOnLongClickListener {
            val callback = object : ActionRequest.Callback {
                override fun <T> onSuccess(result: T) {
                    var size = result as Fish.Skipjack.Masters.SpeciesSize
                    bind(size)
                }

                override fun onError(result: String) {
                    textResult.showResult(Fish.Objects.EntityState.WARNING, result)
                }
            }

            this.dialogSpeciesSizeList(callback)
            true
        }
        textRack.setOnLongClickListener {
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

        textTray.afterTextChanged {
            _trayCount = textTray.text.toString().toIntOrDefault(0)
            textTray.typing()
            prompt()
        }
        textTray.afterKeyEntered {
            var text = textTray.text.toString()
            if (text.isNotBlank()) {
                var trayCount = text.toInt()
                textTray.done()
            } else {
                textTray.cancel()
            }
            textEach.focusThenSelectionAll()
        }
        textEach.afterTextChanged {
            _eachCount = textEach.text.toString().toIntOrDefault(0)
            textEach.typing()
            prompt()
        }
        textEach.afterKeyEntered {
            var text = textEach.text.toString()
            if (text.isNotBlank()) {
                var eachCount = text.toInt()
                textEach.done()
            } else {
                textEach.cancel()
            }
            btnSave.requestFocus()
        }

        btnSave.setOnClickListener {
            btnSave.isEnabled = false
            _tag = Fish.Skipjack.Tag().newInstance(_queue)
            _tag.cook_rack_id = _rack.Id
            _tag.species_id = _species.Id
            _tag.tag_color_code = _tagColor.color_code
            _tag.species_size_id = _size.Id

            _tag.quantity_tray = _trayCount
            _tag.quantity_each = _eachCount

            val skipjack = FishClient.SkipjackClient(applicationContext)
            val callback = object : ActionRequest.Callback {
                override fun <T> onSuccess(result: T) {
                    val obj = result as Fish.Skipjack.Tag
                    bind(obj)
                }

                override fun onError(result: String) {
                    Log.e("TUNA RUN > ADD_TAG > ERROR", result)
                    textResult.showResult(Fish.Objects.EntityState.ERROR, result)
                }
            }
            skipjack.addTag(_tag, callback)

        }

        textQueue.focusThenSelectionAll()

    }

    private fun selectQueue() {
        val callback = object : ActionRequest.Callback {
            override fun <T> onSuccess(result: T) {
                var queue = result as Fish.Skipjack.Queue
                bind(queue)
            }

            override fun onError(result: String) {
                textResult.showResult(Fish.Objects.EntityState.WARNING, result)
            }
        }

        this.dialogQueueList(callback)
    }

    private fun getRack(rackNo: String) {
        var date = FishClient.Companion.Skipjack.Shift.Date
        var shift = FishClient.Companion.Skipjack.Shift.Shift
        val skipjack = FishClient.SkipjackClient(applicationContext)


        val callback = object : ActionRequest.Callback {
            override fun <T> onSuccess(result: T) {
                var obj = result as Fish.Skipjack.Rack
                if (obj.Id > 0) {
                    bind(obj)
                } else {
                    textResult.warningResult(obj.entityMessage)
                    Intent(
                        applicationContext,
                        SkipjackWipCookRackActivity::class.java
                    ).also {
                        it.putExtra("REQUEST_CODE", REQUEST_RACK)
                        it.putExtra("RACK_NO", rackNo)
                        startActivityForResult(it, REQUEST_RACK)
                    }
                }
            }

            override fun onError(result: String) {
                Log.e("TUNA RUN > GET_PRE_RACK > ERROR", result)
            }
        }
        skipjack.getRack(date, shift, rackNo, callback)
    }

    fun bind(obj: Fish.Skipjack.Masters.SpeciesSize?) {
        if ((obj != null) && (obj.Id > 0)) {
            _size = obj
            textSize.done()
        } else {
            _size = Fish.Skipjack.Masters.SpeciesSize()
            textSize.typing()
        }
        prompt()
    }

    private fun clearRack() {
        textRack.clear()
        textTray.clear()
        textEach.clear()
    }

    fun bind(obj: Fish.Skipjack.Rack) {
        if (obj.state == Fish.Objects.EntityState.NEW) {
            clearRack()
        } else if (obj.Id > 0) {
            _rack = obj
            textRack.setText(_rack.rack_no)
            textRack.done()
            textTray.focusThenSelectionAll()
        } else {
            _rack = Fish.Skipjack.Rack()
            textResult.warningResult(obj.entityMessage)
            textRack.typing()
            textRack.focusThenSelectionAll()
        }
        prompt()
    }

    private fun clearQueue() {
        //viewColor.clear()

        viewSpecy.clear()
        viewOrigin.clear()
        textSize.clear()

        viewColor1.clear()
        viewColor2.clear()
        viewColor3.clear()
    }

    fun bind(obj: Fish.Skipjack.Queue) {
        if (obj.Id > 0) {
            _queue = obj
            _species = _queue.Species

            textQueue.setText(_queue.queue_no.toString())
            viewSpecy.text = _species.species_code
            viewSpecy.done()
            viewOrigin.text = _queue.species_origin_code
            viewOrigin.done()
            if (_queue.VCColors.Items.size > 0) {
                _queue.VCColors.Items.forEachIndexed { index, element ->
                    when (index) {
                        0 -> viewColor1.showVCColor(element)
                        1 -> viewColor2.showVCColor(element)
                        2 -> viewColor3.showVCColor(element)
                    }
                }
            }
            textQueue.done()
            textSize.focusThenSelectionAll()
        } else if (obj.state == Fish.Objects.EntityState.NEW) {
            _queue = obj
            _size = Fish.Skipjack.Masters.SpeciesSize()

            textQueue.setText("")
            clearQueue()
        } else {
            _queue = Fish.Skipjack.Queue()
            textResult.showResult(obj.state, obj.entityMessage)
            textQueue.typing()
            viewSpecy.cancel()
            viewOrigin.cancel()
            textQueue.focusThenSelectionAll()
        }
        prompt()
    }

    fun bind(obj: Fish.Skipjack.Tag) {
        if (obj.Id > 0) {
            _tag = obj
            textResult.showResult(Fish.Objects.EntityState.SUCCESS, obj.entityMessage)
            btnSave.isEnabled = false
            btnNew.requestFocus()
        } else {
            textResult.errorResult(obj.entityMessage)
        }
        prompt()
    }


    private fun prompt() {
        btnSave.isEnabled = false
        if (_tag.Id > 0) return
        if (_queue.Id == 0) return
        if (_tagColor.Id == 0) return
        if (_size.Id == 0) return
        if (_rack.Id == 0) return

        if ((_trayCount + _eachCount) <= 0) return

        btnSave.isEnabled = true

    }

    private fun bind(obj: Fish.Skipjack.Masters.TagColor) {
        _tagColor = obj
        viewColor.setBackgroundColor(Color.parseColor(_tagColor.color_hex))
        viewColor.text = _tagColor.color_type
        viewColor.setTextColor(Color.BLACK)
        prompt()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_COLOR -> {
                    data?.getIntExtra("TAG_COLOR_ID", 0).also {
                        var obj = getTagColorCodeById(it)
                        bind(obj)
                    }
                }

                REQUEST_RACK -> {
                    data?.getStringExtra("RACK_NO").also {
                        if (it != null) {
                            if (it.isNotBlank()) {
                                getRack(it)
                            }
                        }
                    }
                }

                else -> throw IllegalArgumentException("TUNA RUN > UNKNOW_REQUEST_CODE: $requestCode")

            }
        }
    }

    private fun getTagColorCodeById(colorId: Int?): Fish.Skipjack.Masters.TagColor {
        return FishClient.Companion.Master.TagColor.Items.first { it.Id == colorId }
    }

}