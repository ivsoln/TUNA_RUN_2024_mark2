package com.inventive.tunarun

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.inventive.tunarun.Instant.Companion.afterKeyEntered
import com.inventive.tunarun.Instant.Companion.focusThenSelectionAll
import com.inventive.tunarun.Instant.Companion.focusThenSelectionEnd
import com.inventive.tunarun.Instant.Companion.toIntOrDefault


class SkipjackQueActivity : AppCompatActivity() {

    private val tabs = arrayOf(
        "PROPERTY",
        "TIME",
        "PRODUCT"
    )

    var queue: Fish.Skipjack.Queue = Fish.Skipjack.Queue()

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    lateinit var textQueueNo: EditText
    lateinit var viewItemNo: TextView
    lateinit var viewOrigin: TextView
    lateinit var viewBatchNo: TextView
    lateinit var viewWeight: TextView
    lateinit var btnSave: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_skipjack_que)

        textQueueNo = findViewById(R.id.text_queueNo)
        viewItemNo = findViewById(R.id.view_species)
        viewOrigin = findViewById(R.id.view_origin)
        viewBatchNo = findViewById(R.id.view_batchNo)
        viewWeight = findViewById(R.id.view_weight)
        btnSave = findViewById(R.id.btn_save)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager2)


        textQueueNo.afterKeyEntered {
            textQueueNo.text.toString().also {
                getQueue(it)
            }
        }

        textQueueNo.setOnTouchListener { v, event ->
            v.onTouchEvent(event)
            val imm = v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
            true
        }

        if ((intent != null) && (intent.extras != null)) {
            val queueNo = intent.extras!!.getString("QUEUE_NO")
            if (queueNo != null) {
                Log.i("TUNA RUN > EXTRA_QUEUE_NO", queueNo)
                getQueue(queueNo)
            }
        }
    }


    private fun getQueue(queueNo: String) {
        queueNo.toIntOrDefault(0).also {
            if (it > 0) {
                val skipjack = FishClient.SkipjackClient(applicationContext)
                val callback = object : ActionRequest.Callback {
                    override fun <T> onSuccess(result: T) {
                        val obj = result as Fish.Skipjack.Queue
                        Log.i("TUNA RUN > GET_QUEUE", obj.entityMessage)
                        bind(obj)
                    }

                    override fun onError(result: String) {
                        Log.e("TUNA RUN > GET_QUEUE > ERROR", result)
                    }
                }
                skipjack.getQueue(it, callback)
            }
        }
    }


    fun bind(obj: Fish.Skipjack.Queue) {
        queue = obj

        textQueueNo.setText(queue.queue_no.toString())
        viewBatchNo.text = queue.batch_no
        viewOrigin.text = queue.species_origin_code
        viewItemNo.text = queue.getItemText()
        viewWeight.text = queue.getNetWeightText()

        Log.i("TUNA RUN > BINDING_ADAPTER", queue.Id.toString())
        val adapter = SkipjackQueAdapter(supportFragmentManager, lifecycle, this, queue)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
        textQueueNo.focusThenSelectionAll()
    }
}

