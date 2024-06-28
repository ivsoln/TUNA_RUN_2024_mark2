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
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        FishClient.initVCColors(this@SkipjackColorActivity, object : ActionRequest.Callback {
            override fun <T> onSuccess(result: T) {
                if (result is Fish.Objects.HashSetClient<*>) {
                    val hashSetClient =
                        result as Fish.Objects.HashSetClient<Fish.Skipjack.Masters.VCColor>
                    val items = hashSetClient.Items

                    Log.i("TUNA RUN", "Number of items retrieved: ${items.size}")

                    for (item in items) {
                        val colorHex = item.color_hex
                        Log.i("TUNA RUN", "Color Code: $colorHex")
                    }

                    val adapter = ColorAdapter(this@SkipjackColorActivity, items)
                    recyclerView.adapter = adapter
                } else {
                    Log.e(
                        "TUNA RUN",
                        "Result is not of type HashSetClient<Fish.Skipjack.Masters.VCColor>"
                    )
                }
            }

            override fun onError(result: String) {
                Log.e("TUNA RUN", "Error in FishClient.initVCColors: $result")
            }
        })


    }



    override fun onColorClick(color: Fish.Skipjack.Masters.VCColor) {
        val intent = Intent()
        intent.putExtra(EXTRA_SELECTED_COLOR, color.Id)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
