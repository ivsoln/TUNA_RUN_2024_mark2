package com.inventive.tunarun

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SkipjackWipTagEditActivity : AppCompatActivity() {

    lateinit var textBarcode: EditText
    lateinit var textQueue: EditText
    lateinit var viewColor: TextView
    lateinit var viewColorNew: TextView
    lateinit var viewSpecy: TextView
    lateinit var textSpecy: EditText
    lateinit var viewOrigin: TextView
    lateinit var textOrigin: EditText
    lateinit var viewSize: TextView
    lateinit var textSize: EditText
    lateinit var viewRack: TextView
    lateinit var textRack: EditText
    lateinit var viewTray: TextView
    lateinit var textTray: EditText
    lateinit var viewEach: TextView
    lateinit var textEach: EditText
    lateinit var goToSave: TextView
    lateinit var goToDel: TextView
    lateinit var goToUndo: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_wip_tag_edit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textBarcode = findViewById(R.id.text_barcode)
        textBarcode.setOnLongClickListener {



            true
        }

        textQueue = findViewById(R.id.text_queue)

        viewColor = findViewById(R.id.view_color)
        viewColorNew = findViewById(R.id.view_colorNew)

        viewSpecy = findViewById(R.id.view_specy)
        textSpecy = findViewById(R.id.text_specy)

        viewOrigin = findViewById(R.id.view_origin)
        textOrigin = findViewById(R.id.text_origin)

        viewSize = findViewById(R.id.view_size)
        textSize = findViewById(R.id.text_size)

        viewRack = findViewById(R.id.view_rack)
        textRack = findViewById(R.id.text_rack)

        viewTray = findViewById(R.id.view_tray)
        textTray = findViewById(R.id.text_tray)

        viewEach = findViewById(R.id.view_each)
        textEach = findViewById(R.id.text_each)

        goToSave = findViewById(R.id.goto_save)
        goToDel = findViewById(R.id.goto_delete)
        goToUndo = findViewById(R.id.goto_undo)
    }
}