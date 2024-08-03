package com.inventive.tunarun

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inventive.tunarun.Fish.Objects.HashSetClient
import com.inventive.tunarun.FishClient.Companion.REQUEST_SHIFT
import com.inventive.tunarun.FishClient.Companion.REQUEST_TAG_SCAN
import org.w3c.dom.Text

class SkipjackCookPostActivity : AppCompatActivity() {

    private lateinit var adapter: FishAdapter.TagAdapter

    private lateinit var textSteamNo: TextView
    private lateinit var textRackCount: TextView
    private lateinit var textRackScanned: TextView

    private lateinit var gotoPickCount: TextView
    private lateinit var gotoScan: TextView
    private lateinit var gotoReset: TextView

    private lateinit var recyclerView: RecyclerView

    companion object {
        var Cook: Fish.Skipjack.Cook = Fish.Skipjack.Cook()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_cook_post)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setLayoutManager(LinearLayoutManager(this))


        Cook = Fish.Skipjack.Cook()
        adapter = FishAdapter.TagAdapter(this, Cook.tags!!.Items)
        recyclerView.adapter = adapter

        textSteamNo = findViewById(R.id.text_steamNo)
        textRackCount = findViewById(R.id.text_rackCount)
        textRackScanned = findViewById(R.id.text_rackScanned)

        gotoPickCount = findViewById(R.id.goto_pickCount)
        gotoScan = findViewById(R.id.goto_scan)
        gotoReset = findViewById(R.id.goto_reset)

        gotoScan.setOnClickListener {
            gotoScan()
        }
        if (intent != null) {
            if (intent.extras != null) {
                Cook.line_no = intent.extras!!.getInt("STEAM_NO")
                Cook.rack_count = intent.extras!!.getInt("RACK_COUNT")
                textSteamNo.text = Cook.line_no.toString()
                textRackCount.text = Cook.rack_count.toString()
            }
        }
        textRackScanned.text = Cook.tags?.count.toString()
        gotoScan()
    }

    private fun gotoScan() {
        Intent(this, SkipjackCookPostTagActivity::class.java).also {
            startActivityForResult(it, REQUEST_TAG_SCAN, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAG_SCAN) {
                data?.getIntExtra("TAG_COUNT", 0)!!.also {
                    if (it > 0) {
                        recyclerView?.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}