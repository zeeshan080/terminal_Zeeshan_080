package com.example.kotlin_terminal_app

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_terminal_app.databinding.ActivityMainBinding

class myViewHolder(itemView: View, listener:newAdapter.onItemClickListener): RecyclerView.ViewHolder(itemView) {

    var title = itemView.findViewById<TextView>(R.id.title)
    var quantity = itemView.findViewById<TextView>(R.id.quantity)
    var btn = itemView.findViewById<Button>(R.id.buyButton)

    init {
        btn.setOnClickListener(){
            listener.onItemClick(adapterPosition)
        }
    }
}
