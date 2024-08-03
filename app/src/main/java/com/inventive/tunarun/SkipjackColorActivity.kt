package com.inventive.tunarun

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SkipjackColorActivity : AppCompatActivity(), ColorAdapter.OnColorClickListener {

    companion object {
        const val REQUEST_COLOR = 1
        const val EXTRA_SELECTED_COLOR = "TAG_COLOR_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skipjack_color)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_colors)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        val adapter = ColorAdapter(this, FishClient.Companion.Master.TagColor.Items)
        recyclerView.adapter = adapter
    }


    override fun onColorClick(color: Fish.Skipjack.Masters.TagColor) {
        val intent = Intent()
        intent.putExtra(EXTRA_SELECTED_COLOR, color.Id)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
