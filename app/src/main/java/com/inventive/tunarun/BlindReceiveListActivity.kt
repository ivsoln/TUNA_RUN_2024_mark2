package com.inventive.tunarun

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class BlindReceiveListActivity : AppCompatActivity(), MyRecyclerViewAdapter.ItemClickListener
{
    private var adapter: MyRecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_blind_receive_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // data to populate the RecyclerView with
        // data to populate the RecyclerView with
        val animalNames = ArrayList<String>()
        animalNames.add("Horse")
        animalNames.add("Cow")
        animalNames.add("Camel")
        animalNames.add("Sheep")
        animalNames.add("Goat")
        animalNames.add("Horse")
        animalNames.add("Cow")
        animalNames.add("Camel")
        animalNames.add("Sheep")
        animalNames.add("Goat")
        animalNames.add("Horse")
        animalNames.add("Cow")
        animalNames.add("Camel")
        animalNames.add("Sheep")
        animalNames.add("Goat")
        animalNames.add("Horse")
        animalNames.add("Cow")
        animalNames.add("Camel")
        animalNames.add("Sheep")
        animalNames.add("Goat")
        animalNames.add("Horse")
        animalNames.add("Cow")
        animalNames.add("Camel")
        animalNames.add("Sheep")
        animalNames.add("Goat")
        animalNames.add("Horse")
        animalNames.add("Cow")
        animalNames.add("Camel")
        animalNames.add("Sheep")
        animalNames.add("Goat")

        // set up the RecyclerView
        // set up the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvAnimals)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        adapter = MyRecyclerViewAdapter(this, animalNames)
        adapter!!.setClickListener(itemClickListener = this)

        recyclerView.setAdapter(adapter)
    }

    override fun onItemClick(view: View?, position: Int) {
        //Toast.makeText(this, "You clicked " + adapter!!.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT ).show()

        var animal = adapter!!.getItem(position)
        val intent = Intent()
        intent.putExtra("Animal", animal)
        setResult(RESULT_OK, intent)
        finish()
    }
}


class MyRecyclerViewAdapter internal constructor(context: Context?, private val mData: List<String>) : RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater
    private var mClickListener: ItemClickListener? = null

    // data is passed into the constructor
    init {
        mInflater = LayoutInflater.from(context)
    }

    //instance variable
    private val itemViewList: ArrayList<View> = ArrayList()
    // inflates the row layout from xml when needed


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {



        val itemView: View = mInflater.inflate(R.layout.fragment_skipjack_blind_receive_item, parent, false)
        val myViewHolder = ViewHolder(itemView)
        itemViewList.add(itemView) //to add all the 'list row item' views

        itemView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                v.setBackgroundColor(Color.parseColor("#FFE57F"))
            }
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                v.setBackgroundColor(Color.TRANSPARENT)
            }
            false
        }

        //Set on click listener for each item view
//        itemView.setOnClickListener {
//            for (tempItemView in itemViewList) {
//                /** navigate through all the itemViews and change color
//                 * of selected view to colorSelected and rest of the views to colorDefault  */
//                /** navigate through all the itemViews and change color
//                 * of selected view to colorSelected and rest of the views to colorDefault  */
//                if (itemViewList.get(myViewHolder.getAdapterPosition()) === tempItemView) {
//                    tempItemView.setBackgroundResource(R.color.Amber_A100)
//                } else {
//                    tempItemView.setBackgroundResource(R.color.Blue_Gray_050)
//                }
//            }
//        }
        return myViewHolder
    }


    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animal = mData[position]
        holder.myTextView.text = animal
    }

    // total number of rows
    override fun getItemCount(): Int {
        return mData.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var myTextView: TextView

        init {
            myTextView = itemView.findViewById<TextView>(R.id.tvAnimalName)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, getAdapterPosition())
        }
    }

    // convenience method for getting data at click position
    fun getItem(id: Int): String {
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
