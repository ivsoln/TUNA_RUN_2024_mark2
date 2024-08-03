package com.inventive.tunarun

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inventive.tunarun.ColorAdapter.ColorViewHolder

class ColorAdapter(
    private val listener: OnColorClickListener,
    private val colors: List<Fish.Skipjack.Masters.TagColor>
) :
    RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    interface OnColorClickListener {
        fun onColorClick(color: Fish.Skipjack.Masters.TagColor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = colors[position]
        holder.colorName.text = color.color_type

        holder.colorName.setBackgroundColor(Color.parseColor(color.color_hex))

        holder.itemView.setOnClickListener {
            listener.onColorClick(color)
        }
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val colorName: TextView = itemView.findViewById(R.id.color_name)
    }
}

