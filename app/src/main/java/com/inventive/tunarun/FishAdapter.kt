package com.inventive.tunarun

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FishAdapter {


    class ListItem {
        override fun toString(): String {
            return caption.toString()
        }

        var id: Int = 0
        var caption: String = ""
        var description: String = ""


    }
//
//    class ListViewAdapter(
//        context: Activity,
//        var resources: Int,
//        private var items: List<ListItem>,
//    ) :
//        ArrayAdapter<ListItem>(context, resources, items) {
//
//        abstract class Callback<T>(activity: Activity, title: String) {
//            var title: String = title
//            val activity: Activity = activity
//            var items: List<T> = listOf()
//            open fun onItemSelected(result: T) {}
//            open fun searchTextChanged(listView: ListView, text: String) {}
//        }
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//
//            val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
//            val view: View = layoutInflater.inflate(resources, null)
//            val textCaption: TextView = view.findViewById(R.id.text_caption)
//            val textDescription: TextView = view.findViewById(R.id.text_description)
//            var item: ListItem = items[position]
//
//
//            textCaption.text = item.caption;
//            textDescription.text = item.description;
//            return view
//        }
//    }

    class RecyclerViewAdapter internal constructor(
        context: Context?,
        callback: Callback<ListItem>?,
        var resources: Int,
        private val mData: List<ListItem>,
    ) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        abstract class Callback<T>(activity: Activity, title: String) {
            var title: String = title
            val activity: Activity = activity
            var items: List<T> = listOf()
            private var mClickListener: ItemClickListener? = null
            open fun onItemSelected(result: T) {}
            open fun searchTextChanged(listView: RecyclerView, text: String) {}

            fun onItemClicked(view: View?, position: Int) {
                if (mClickListener != null) mClickListener!!.onItemClick(view, position)
            }

            fun setClickListener(itemClickListener: ItemClickListener?) {
                mClickListener = itemClickListener
            }
        }

        private var _callback: RecyclerViewAdapter.Callback<ListItem>? = null
        private val mInflater: LayoutInflater = LayoutInflater.from(context)
        private var mClickListener: ItemClickListener? = null
        private val itemViewList: ArrayList<View> = ArrayList()

        constructor(context: Context?, resources: Int, mData: List<ListItem>) : this(
            context,
            null,
            resources,
            mData
        )

        init {
            _callback = callback
            setClickListener(object : ItemClickListener {
                override fun onItemClick(view: View?, position: Int) {
                    Log.i(
                        "TUNA RUN > ADAPTER_CLICK",
                        position.toString()
                    )
                    _callback?.onItemClicked(view, position)
                }
            })
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView: View =
                mInflater.inflate(resources, parent, false)
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
            val obj = mData[position]
            holder.textCaption.text = obj.caption
            holder.textDescription.text = obj.description
        }

        // total number of rows
        override fun getItemCount(): Int {
            return mData.size

        }

        // stores and recycles views as they are scrolled off screen
        inner class ViewHolder internal constructor(itemView: View) :
            RecyclerView.ViewHolder(itemView),
            View.OnClickListener {

            val textCaption: TextView = itemView.findViewById(R.id.text_caption)
            val textDescription: TextView = itemView.findViewById(R.id.text_description)

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(view: View) {
                Log.i("TUNA RUN > VIEW_HOLDER TAG_ITEM CLICK", textCaption.text.toString())
                if (mClickListener != null) mClickListener!!.onItemClick(
                    view,
                    getAdapterPosition()
                )
            }
        }

        // convenience method for getting data at click position
        fun getItem(id: Int): ListItem {
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

    class QueAdapter internal constructor(
        context: Context?,
        private val mData: List<Fish.Skipjack.Queue>,
    ) : RecyclerView.Adapter<QueAdapter.ViewHolder>() {
        private val mInflater: LayoutInflater = LayoutInflater.from(context)
        private var mClickListener: RecyclerViewAdapter.ItemClickListener? = null
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
        inner class ViewHolder internal constructor(itemView: View) :
            RecyclerView.ViewHolder(itemView),
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
        fun setClickListener(itemClickListener: RecyclerViewAdapter.ItemClickListener?) {
            mClickListener = itemClickListener
        }

    }

    class QueGroupAdapter internal constructor(
        context: Context?,
        private val mData: List<Fish.Skipjack.Queue>,
    ) : RecyclerView.Adapter<QueGroupAdapter.ViewHolder>() {
        private val mInflater: LayoutInflater = LayoutInflater.from(context)
        private var mClickListener: RecyclerViewAdapter.ItemClickListener? = null
        private val itemViewList: ArrayList<View> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView: View =
                mInflater.inflate(R.layout.custom_skipjack_que_grp_item, parent, false)
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
            holder.viewGrpText.text = que.batch_group_text
            holder.viewBatchNo.text = que.batch_no
            holder.viewLot.text = que.lot_date_text
            holder.viewSize.text = que.species_size_code

            holder.viewWeight.text = que.weight_bins_text
            holder.viewTotal.text = "=" + que.weight_bins_total_text
        }

        // total number of rows
        override fun getItemCount(): Int {
            return mData.size
        }

        // stores and recycles views as they are scrolled off screen
        inner class ViewHolder internal constructor(itemView: View) :
            RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
            var viewGrpText: TextView = itemView.findViewById(R.id.view_group_text)
            var viewSize: TextView = itemView.findViewById(R.id.view_size)
            var viewBatchNo: TextView = itemView.findViewById(R.id.view_batchNo)
            var viewLot: TextView = itemView.findViewById(R.id.view_lot)
            var viewWeight: TextView = itemView.findViewById(R.id.view_weight)
            var viewTotal: TextView = itemView.findViewById(R.id.view_total)


            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(view: View) {
                Log.i("TUNA RUN > VIEW_HOLDER QUEUE_ITEM CLICK", viewGrpText.text.toString())
                if (mClickListener != null) mClickListener!!.onItemClick(view, getAdapterPosition())
            }
        }

        // convenience method for getting data at click position
        fun getItem(id: Int): Fish.Skipjack.Queue {
            return mData[id]
        }

        // allows clicks events to be caught
        fun setClickListener(itemClickListener: RecyclerViewAdapter.ItemClickListener?) {
            mClickListener = itemClickListener
        }

    }


    class BinAdapter internal constructor(
        context: Context?,
        private val mData: List<Fish.Skipjack.Bin>,
    ) : RecyclerView.Adapter<BinAdapter.ViewHolder>() {
        private val mInflater: LayoutInflater = LayoutInflater.from(context)
        private var mClickListener: RecyclerViewAdapter.ItemClickListener? = null
        private val itemViewList: ArrayList<View> = ArrayList()


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView: View =
                mInflater.inflate(R.layout.custom_skipjack_bin_item, parent, false)
            val viewHolder = ViewHolder(itemView)
            itemViewList.add(itemView) //to add all the 'list row item' views

            itemView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    //v.setBackgroundColor(Color.parseColor("#000000"))
                }
                if (event.action == MotionEvent.ACTION_UP) {
                    v.setBackgroundColor(Color.TRANSPARENT)
                }
                false
            }
            return viewHolder
        }


        // binds the data to the TextView in each row
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val bin = mData[position]
            holder.viewSerialNo.text = bin.serial_no.toString()
            holder.viewBatchNo.text = bin.batch_no
            holder.viewLotNo.text = bin.lot_no
            holder.viewSloc.text = bin.location_description

            var itemNo = ""
            if (bin.material_code != null) {
                itemNo += bin.material_code
            }

            if (bin.species_code != null) {
                if (itemNo.isNotEmpty()) {
                    itemNo += " /"
                }
                itemNo += bin.species_code
            }

            holder.viewItemNo.text = itemNo
            holder.viewWeight.text = "${bin.net_weight} KG."
        }

        // total number of rows
        override fun getItemCount(): Int {
            return mData.size

        }

        // stores and recycles views as they are scrolled off screen
        inner class ViewHolder internal constructor(itemView: View) :
            RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
            var viewSerialNo: TextView = itemView.findViewById(R.id.view_que)
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
        fun getItem(id: Int): Fish.Skipjack.Bin {
            return mData[id]
        }

        // allows clicks events to be caught
        fun setClickListener(itemClickListener: RecyclerViewAdapter.ItemClickListener?) {
            mClickListener = itemClickListener
        }


    }


    class TagAdapter internal constructor(
        context: Context?,
        private val mData: List<Fish.Skipjack.Tag>,
    ) : RecyclerView.Adapter<TagAdapter.ViewHolder>() {
        private val mInflater: LayoutInflater = LayoutInflater.from(context)
        private var mClickListener: RecyclerViewAdapter.ItemClickListener? = null
        private val itemViewList: ArrayList<View> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView: View =
                mInflater.inflate(R.layout.custom_skipjack_wip_tag_item, parent, false)
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
            val obj = mData[position]
            holder.viewQue.text = obj.queue_no.toString()
            holder.viewBatchNo.text = obj.batch_no
        }

        // total number of rows
        override fun getItemCount(): Int {
            return mData.size

        }

        // stores and recycles views as they are scrolled off screen
        inner class ViewHolder internal constructor(itemView: View) :
            RecyclerView.ViewHolder(itemView),
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
                Log.i("TUNA RUN > VIEW_HOLDER TAG_ITEM CLICK", viewQue.text.toString())
                if (mClickListener != null) mClickListener!!.onItemClick(view, getAdapterPosition())
            }
        }

        // convenience method for getting data at click position
        fun getItem(id: Int): Fish.Skipjack.Tag {
            return mData[id]
        }

        // allows clicks events to be caught
        fun setClickListener(itemClickListener: RecyclerViewAdapter.ItemClickListener?) {
            mClickListener = itemClickListener
        }

    }
}