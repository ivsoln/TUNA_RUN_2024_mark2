package com.inventive.tunarun

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inventive.tunarun.FishAdapter.RecyclerViewAdapter
import com.inventive.tunarun.FishClient.Companion.showShift
import com.inventive.tunarun.FishClient.Companion.showUser
import com.inventive.tunarun.Instant.Companion.clearResult
import com.inventive.tunarun.Instant.Companion.showResult

class SkipjackQueGroupListActivity : AppCompatActivity() , RecyclerViewAdapter.ItemClickListener{
    private var adapter: FishAdapter.QueGroupAdapter? = null

    private lateinit var progressBar: ProgressBar
    private lateinit var overlay: View
    private lateinit var viewResult: TextView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.custom_list_view_item)

        findViewById<TextView>(R.id.view_shift).showShift()
        findViewById<TextView>(R.id.text_user).showUser()

        progressBar = findViewById(R.id.progressBar)
        overlay = findViewById(R.id.overlay)
        viewResult = findViewById(R.id.view_result)
        viewResult.isVisible = false

        progressBar.visibility = View.VISIBLE
        overlay.visibility = View.VISIBLE

        var context: SkipjackQueGroupListActivity = this
        val skipjack = FishClient.SkipjackClient(context)

        val callback = object : ActionRequest.Callback {

            override fun <T> onSuccess(result: T) {
                progressBar.visibility = View.GONE
                overlay.visibility = View.GONE

                val queues = result as Fish.Objects.HashSetClient<Fish.Skipjack.Queue>
                if (queues.Items.isEmpty()) {
                    viewResult.clearResult()
                    viewResult.isVisible = true
                } else {
                    val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
                    recyclerView.setLayoutManager(LinearLayoutManager(context))
                    adapter = FishAdapter.QueGroupAdapter(context, queues.Items)
                    adapter!!.setClickListener(itemClickListener = context)
                    recyclerView.setAdapter(adapter)
                }
            }

            override fun onError(result: String) {
                Log.e("TUNA RUN > LIST_QUEUE > ERROR", result)
                progressBar.visibility = View.GONE
                overlay.visibility = View.GONE
                viewResult.showResult(Fish.Objects.EntityState.ERROR, result)
            }
        }
        skipjack.listQueuesAsGroup(callback)
    }

    override fun onItemClick(view: View?, position: Int) {
//        var que = adapter!!.getItem(position)
//        Log.i("TUNA RUN > QUEUE CLICK", que.Id.toString())
//        val intent = Intent()
//        intent.putExtra("QUE_ID", que.Id.toString())
//        setResult(RESULT_OK, intent)
//        finish()
    }
}


