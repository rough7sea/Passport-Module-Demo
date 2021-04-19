package com.example.myapplication.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.entity.Additional
import com.example.myapplication.database.entity.Tower
import com.example.myapplication.fragments.holder.MyViewHolder
import kotlinx.android.synthetic.main.additional_layout.view.*
import kotlinx.android.synthetic.main.tower_layout.view.*
import kotlinx.android.synthetic.main.tower_layout.view.id_textView
import kotlinx.android.synthetic.main.tower_layout.view.latitude_textView
import kotlinx.android.synthetic.main.tower_layout.view.longitude_textView
import java.util.Collections.emptyList

class AdditionalAdapter : RecyclerView.Adapter<MyViewHolder>()  {

    private var additionalList = emptyList<Additional>()
    private val dataManager: AppDatabase by lazy { App.getDatabaseManager() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.additional_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = additionalList[position]
        holder.itemView.id_textView.text = currentItem.add_id.toString()
        holder.itemView.number_textView.text = currentItem.number

        currentItem.coord_id?.let {
            val coordinate = dataManager.coordinateDao().getById(it)
            if (coordinate != null) {
                holder.itemView.longitude_textView.text = coordinate.longitude.toString()
                holder.itemView.latitude_textView.text = coordinate.latitude.toString()
            }
        }
    }

    fun setData(additional: List<Additional>){
        this.additionalList = additional
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = additionalList.size
}