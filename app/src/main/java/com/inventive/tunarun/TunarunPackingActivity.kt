package com.inventive.tunarun

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class TunarunPackingActivity : AppCompatActivity() {

    private lateinit var adapter: TunarunPackingAdapterActivity
    private lateinit var itemList: MutableList<String>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var addCode: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tunarun_packing)

        itemList = mutableListOf(
            "Item 1", "Item 2", "Item 3", "Item 4", "Item 5",
            "Item 6", "Item 7", "Item 8", "Item 9", "Item 10",
            "Item 11", "Item 12", "Item 13", "Item 14", "Item 15",
            "Item 16", "Item 17", "Item 18", "Item 19", "Item 20"
        )

        adapter = TunarunPackingAdapterActivity(itemList)

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {
            refreshItems()
        }

        val recyclerView: RecyclerView = findViewById(R.id.packing_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        addCode = findViewById(R.id.text_code)
        addCode.setOnEditorActionListener { _, actionId, event ->
            if (actionId == KeyEvent.ACTION_DOWN || event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val newItem = addCode.text.toString().trim()
                if (newItem.isNotEmpty()) {
                    itemList.add(0, newItem)
                    adapter.notifyItemInserted(0)
                    addCode.text.clear()
                    recyclerView.scrollToPosition(0)

                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(addCode.windowToken, 0)
                }
                true
            } else {
                false
            }
        }

        //define ItemTouchHelper.SimpleCallback for move left
        val itemTouchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                //onSwiped run when move to left then AlertDialog for deleated confirm
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    AlertDialog.Builder(this@TunarunPackingActivity)
                        .setMessage("ต้องการลบรายการนี้ใช่หรือไม่?")
                        .setPositiveButton("ใช่") { dialog, which ->
                            adapter.removeItem(position)
                        }.setNegativeButton("ไม่") { dialog, which ->
                            adapter.notifyItemChanged(position)
                            dialog.dismiss()
                        }.show()
                }

                //onChildDraw create some bin
                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    super.onChildDraw(
                        c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                    )
                    val itemView = viewHolder.itemView

                    if (dX < 0) {
                        val icon = ContextCompat.getDrawable(
                            this@TunarunPackingActivity, R.drawable.ic_minus
                        )!!
                        val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
                        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                        val iconBottom = iconTop + icon.intrinsicHeight
                        val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                        val iconRight = itemView.right - iconMargin
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        icon.draw(c)
                    }
                }
            })

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun refreshItems() {
        // some process
        itemList.clear()
        itemList.addAll(
            listOf(
                "Item 21", "Item 22", "Item 23", "Item 24", "Item 25",
                "Item 26", "Item 27", "Item 28", "Item 29", "Item 30",
                "Item 31", "Item 32", "Item 33", "Item 34", "Item 35",
                "Item 36", "Item 37", "Item 38", "Item 39", "Item 40"
            )
        )

        // change to adapter
        adapter.notifyDataSetChanged()


        // closed progress of SwipeRefreshLayout
        swipeRefreshLayout.isRefreshing = false
    }
}