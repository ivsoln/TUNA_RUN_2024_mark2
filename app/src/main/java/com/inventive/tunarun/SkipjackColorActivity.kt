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
        const val EXTRA_SELECTED_COLOR = "color_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skipjack_color)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_colors)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        val adapter =
            ColorAdapter(this@SkipjackColorActivity, FishClient.Companion.Master.VCColor.Items)
        recyclerView.adapter = adapter
    }


    override fun onColorClick(color: Fish.Skipjack.Masters.VCColor) {
        val intent = Intent()
        intent.putExtra(EXTRA_SELECTED_COLOR, color.Id)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
