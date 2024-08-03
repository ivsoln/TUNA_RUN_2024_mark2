package com.inventive.tunarun

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
import com.inventive.tunarun.Fish.Objects
import com.inventive.tunarun.Fish.Objects.EntityState
import com.inventive.tunarun.Fish.Skipjack.Tag
import com.inventive.tunarun.Instant.Companion.afterKeyEntered
import com.inventive.tunarun.Instant.Companion.clear
import com.inventive.tunarun.Instant.Companion.focusThenSelectionAll
import com.inventive.tunarun.Instant.Companion.showResult
import com.inventive.tunarun.SkipjackCookPostActivity.Companion.Cook

class SkipjackCookPostTagActivity : AppCompatActivity() {

    private lateinit var adapter: FishAdapter.TagAdapter
    private var tags: MutableList<Tag> = mutableListOf()
    private lateinit var recyclerView: RecyclerView

    private lateinit var textBarcode: EditText
    private lateinit var textGroup: EditText
    private lateinit var textCount: TextView

    private lateinit var gotoNext: TextView
    private lateinit var gotoClr: TextView

    private lateinit var textResult: TextView

    private var requestCode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_cook_post_tag)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        if (intent != null) {
            if (intent.extras != null) {
                requestCode = intent.extras!!.getInt("REQUEST_CODE")
            }
        }

        adapter = FishAdapter.TagAdapter(this, tags)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.adapter = adapter

        textResult = findViewById(R.id.text_result)
        textBarcode = findViewById(R.id.text_barcode)
        textGroup = findViewById(R.id.text_group)
        textCount = findViewById(R.id.text_count)

        gotoNext = findViewById(R.id.goto_next)
        gotoNext.setOnClickListener {
            if (tags.size > 0) {

                Cook.tags!!.Items.addAll(0, tags)

                val intent = Intent()
                intent.putExtra("TAG_COUNT", tags.size)
                setResult(RESULT_OK, intent)
                finish()

            }
        }
        gotoClr = findViewById(R.id.goto_clr)
        gotoClr.setOnClickListener {
            tags.clear()
            recyclerView?.adapter?.notifyDataSetChanged()
            textBarcode.focusThenSelectionAll()
        }

        textBarcode.afterKeyEntered {
            var text: String = textBarcode.text.toString()
            val skipjack = FishClient.SkipjackClient(applicationContext)
            val callback = object : ActionRequest.Callback {
                override fun <T> onSuccess(result: T) {
                    val obj = result as Fish.Skipjack.Tag
                    addTag(obj)
                }

                override fun onError(result: String) {
                    Log.e("TUNA RUN > CREATE BLIND_RECEIVE", result)
                    textResult.showResult(EntityState.WARNING, result)
                }
            }
            skipjack.getTag(text, callback)

        }
    }


    private fun addTag(obj: Fish.Skipjack.Tag) {
        tags.add(0, obj)
        adapter.notifyItemInserted(0)
        recyclerView.scrollToPosition(0)
        textResult.showResult(obj)
    }
}