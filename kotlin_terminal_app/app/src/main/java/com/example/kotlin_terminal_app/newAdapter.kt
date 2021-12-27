package com.example.kotlin_terminal_app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class newAdapter(val titles: ArrayList<String>, val quantity: ArrayList<Int>): RecyclerView.Adapter<myViewHolder>()
{
    private lateinit var listen:onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val inflator =  LayoutInflater.from(parent.context)
        var view = inflator.inflate(R.layout.single_product_view,parent,false)
        return myViewHolder(view,listen)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.title.text =  titles[position]
        holder.quantity.text = quantity[position].toString()
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    fun setOnItemClickListener(listener:onItemClickListener){
        listen = listener
    }


}