package com.inventive.tunarun

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SkipjackTagColorActivity : AppCompatActivity(), SkipjackTagColorAdapter.OnColorClickListener {

    companion object {
        const val REQUEST_COLOR = 0
        const val EXTRA_SELECTED_COLOR = "TAG_COLOR_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skipjack_tag_color)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_tag_colors)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val items = FishClient.Companion.Master.TagColor.Items

        for (item in items) {
            val colorHex = item.color_hex
            Log.i("TUNA RUN", "COLOR_HEX: $colorHex")
        }

        val adapter = SkipjackTagColorAdapter(this, items)
        recyclerView.adapter = adapter


    }


    override fun onColorClick(color: Fish.Skipjack.Masters.TagColor) {
        val intent = Intent()
        intent.putExtra(EXTRA_SELECTED_COLOR, color.Id)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
