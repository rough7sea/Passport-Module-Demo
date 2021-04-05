package com.example.myapplication.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.entity.Tower
import com.example.myapplication.fragments.holder.MyViewHolder
import kotlinx.android.synthetic.main.tower_layout.view.*
import java.util.Collections.emptyList

class TowerAdapter : RecyclerView.Adapter<MyViewHolder>()  {

    private var towerList = emptyList<Tower>()

    private val dataManager: AppDatabase by lazy { App.getDatabaseManager() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tower_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = towerList[position]
        holder.itemView.id_textView.text = currentItem.tower_id.toString()
        holder.itemView.idtf_textView.text = currentItem.idtf

        currentItem.coord_id?.let {
            val coordinate = dataManager.coordinateDao().getById(it)
            if (coordinate != null) {
                holder.itemView.longitude_textView.text = coordinate.longitude.toString()
                holder.itemView.latitude_textView.text = coordinate.latitude.toString()
            }
        }
    }

    fun setData(tower: List<Tower>){
        this.towerList = tower
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = towerList.size
}