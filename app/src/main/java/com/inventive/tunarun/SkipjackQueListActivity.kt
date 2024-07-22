package com.inventive.tunarun

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inventive.tunarun.FishClient.Companion.showShift
import com.inventive.tunarun.FishClient.Companion.showUser
import com.inventive.tunarun.Instant.Companion.showResult
import java.text.SimpleDateFormat

class SkipjackQueListActivity : AppCompatActivity(), QueListAdapter.ItemClickListener {

    private var adapter: QueListAdapter? = null

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

        var context: SkipjackQueListActivity = this
        val skipjack = FishClient.SkipjackClient(context)

        val callback = object : ActionRequest.Callback {

            override fun <T> onSuccess(result: T) {
                progressBar.visibility = View.GONE
                overlay.visibility = View.GONE

                val queues = result as Fish.Objects.HashSetClient<Fish.Skipjack.Queue>
                if (queues.Items.isEmpty()) {
                    viewResult.text = ""
                    viewResult.isVisible = true
                } else {
                    val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
                    recyclerView.setLayoutManager(LinearLayoutManager(context))
                    adapter = QueListAdapter(context, queues.Items)
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
        skipjack.getQueuesAsCompact(callback)
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

class QueListAdapter internal constructor(
    context: Context?,
    private val mData: List<Fish.Skipjack.Queue>,
) : RecyclerView.Adapter<QueListAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null
    private val itemViewList: ArrayList<View> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            mInflater.inflate(R.layout.custom_skipjack_que_item, parent, false)
        val viewHolder = ViewHolder(itemView)
        itemViewList.add(itemView) //to add all the 'list row item' views

        itemView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                itemViewList.forEach { it.setBackgroundColor(Color.TRANSPARENT) }
                v.setBackgroundColor(Color.parseColor("#FFFFE57F"))
            }

            false
        }
        return viewHolder
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val que = mData[position]
        holder.viewQue.text = que.queue_no.toString()
        holder.viewBatchNo.text = que.batch_no
        holder.viewLotNo.text = que.lot_no
        if (que.batch_group_text.isNotEmpty()) {
            holder.viewItemNo.text =
                que.getItemText() + " /" + que.customer_species_text + "/" + que.batch_group_text
        } else {
            holder.viewItemNo.text = que.getItemText()
        }
        holder.viewWeight.text = que.getNetWeightText()
    }

    // total number of rows
    override fun getItemCount(): Int {
        return mData.size

    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var viewQue: TextView = itemView.findViewById(R.id.view_que)
        var viewBatchNo: TextView = itemView.findViewById(R.id.view_batchNo)
        var viewLotNo: TextView = itemView.findViewById(R.id.view_lotNo)
        var viewItemNo: TextView = itemView.findViewById(R.id.view_itemNo)
        var viewWeight: TextView = itemView.findViewById(R.id.view_weight)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            Log.i("TUNA RUN > VIEW_HOLDER QUEUE_ITEM CLICK", viewQue.text.toString())
            if (mClickListener != null) mClickListener!!.onItemClick(view, getAdapterPosition())
        }
    }

    // convenience method for getting data at click position
    fun getItem(id: Int): Fish.Skipjack.Queue {
        return mData[id]
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
}

