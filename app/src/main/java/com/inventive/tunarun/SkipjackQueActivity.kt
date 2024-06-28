package com.inventive.tunarun

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
class SkipjackQueActivity : AppCompatActivity() {

    private val tabs = arrayOf(
        "PROPERTY",
        "TIME",
        "PRODUCT"
    )

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    lateinit var textBarcode: EditText
    lateinit var viewSpecies: TextView
    lateinit var viewOrigin: TextView
    lateinit var viewBatchNo: TextView
    lateinit var btnSave: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_que)

        textBarcode = findViewById(R.id.text_barcode)
        viewSpecies = findViewById(R.id.view_species)
        viewOrigin = findViewById(R.id.view_origin)
        viewBatchNo = findViewById(R.id.view_batchNo)
        btnSave = findViewById(R.id.btn_save)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager2)

        val adapter = SkipjackQueAdapter(supportFragmentManager, lifecycle,this)
        adapter.number = 89 //pass data object to adapter to view in fragment element
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }
}

