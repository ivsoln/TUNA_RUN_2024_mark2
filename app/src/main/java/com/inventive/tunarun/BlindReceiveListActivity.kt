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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inventive.tunarun.FishAdapter.RecyclerViewAdapter
import com.inventive.tunarun.FishClient.Companion.showShift
import com.inventive.tunarun.FishClient.Companion.showUser
import com.inventive.tunarun.Instant.Companion.showResult


class BlindReceiveListActivity : AppCompatActivity(), RecyclerViewAdapter.ItemClickListener {

    private var adapter: BlindReceiveListAdapter? = null

    private lateinit var progressBar: ProgressBar
    private lateinit var overlay: View
    private lateinit var viewResult: TextView

    private  var requestCode: Int = -1

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

        var context: BlindReceiveListActivity = this
        FishClient.SkipjackClient(context).also {
            val callback = object : ActionRequest.Callback {
                override fun <T> onSuccess(result: T) {
                    progressBar.visibility = View.GONE
                    overlay.visibility = View.GONE

                    val br = result as Fish.Objects.HashSetClient<Fish.Skipjack.Blind.BR>
                    if (br.Items.isEmpty()) {
                        viewResult.text = ""
                        viewResult.isVisible = true
                    } else {
                        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
                        recyclerView.setLayoutManager(LinearLayoutManager(context))
                        adapter = BlindReceiveListAdapter(context, br.Items)
                        adapter!!.setClickListener(itemClickListener = context)
                        recyclerView.setAdapter(adapter)
                    }
                }

                override fun onError(result: String) {
                    Log.e("TUNA RUN > LIST_BLIND_RECEIVE > ERROR", result)
                    progressBar.visibility = View.GONE
                    overlay.visibility = View.GONE
                    viewResult.showResult(Fish.Objects.EntityState.ERROR, result)
                }
            }
            it.listBlindReceive(callback)
        }

        if (intent != null) {
            if (intent.extras != null) {
                requestCode = intent.extras!!.getInt("REQUEST_CODE")
            }
        }
    }

    override fun onItemClick(view: View?, position: Int) {
        if (requestCode == FishClient.REQUEST_BLIND_RECEIVE) {
            var br = adapter!!.getItem(position)
            val intent = Intent()
            intent.putExtra("SERIAL_NO", br.serial_no)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}

class BlindReceiveListAdapter internal constructor(
    context: Context?,
    private val mData: List<Fish.Skipjack.Blind.BR>
) : RecyclerView.Adapter<BlindReceiveListAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: RecyclerViewAdapter.ItemClickListener? = null

    //instance variable
    private val itemViewList: ArrayList<View> = ArrayList()
    // inflates the row layout from xml when needed


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            mInflater.inflate(R.layout.custom_skipjack_blind_recieve_item, parent, false)
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
        val br = mData[position]
        holder.viewSerialNo.text = br.serial_no.toString()
        holder.viewBatchNo.text = br.batch_no
        holder.viewLotNo.text = br.lot_no
        holder.viewSloc.text = br.sloc

        var itemNo = ""
        if (br.material_code != null) {
            itemNo += br.material_code
        }

        if (br.species != null) {
            if (itemNo.isNotEmpty()) {
                itemNo += " /"
            }
            itemNo += br.species
        }

        holder.viewItemNo.text = itemNo
        holder.viewWeight.text = "${br.weight} KG."
    }

    // total number of rows
    override fun getItemCount(): Int {
        return mData.size

    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var viewSerialNo: TextView = itemView.findViewById(R.id.view_serial_no)
        var viewBatchNo: TextView = itemView.findViewById(R.id.view_batchNo)
        var viewLotNo: TextView = itemView.findViewById(R.id.view_lotNo)
        var viewItemNo: TextView = itemView.findViewById(R.id.view_itemNo)
        var viewWeight: TextView = itemView.findViewById(R.id.view_weight)
        var viewSloc: TextView = itemView.findViewById(R.id.view_sloc)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, getAdapterPosition())
        }
    }


    // convenience method for getting data at click position
    fun getItem(id: Int): Fish.Skipjack.Blind.BR {
        return mData[id]
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: RecyclerViewAdapter.ItemClickListener?) {
        mClickListener = itemClickListener
    }

}
