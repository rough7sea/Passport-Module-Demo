package com.example.myapplication.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.datamanager.database.entity.Coordinate
import com.example.datamanager.database.entity.Tower
import com.example.datamanager.database.repository.impl.CoordinateRepository
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.fragments.holder.MyViewHolder
import com.example.myapplication.fragments.list.TowerListFragmentDirections
import kotlinx.android.synthetic.main.tower_layout.view.*
import java.util.Collections.emptyList

class TowerAdapter : RecyclerView.Adapter<MyViewHolder>()  {

    private var towerList = emptyList<Tower>()
    private val coordinateRepository: CoordinateRepository by lazy {
        App.getDataManager().getRepository(Coordinate::class.java) as CoordinateRepository
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tower_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = towerList[position]
        holder.itemView.towerNumber_textView.text = currentItem.number
        holder.itemView.idtf_textView.text = currentItem.idtf

        currentItem.coord_id?.let {
            val coordinate = coordinateRepository.getById(it)
            if (coordinate != null) {
                holder.itemView.longitude_textView.text = coordinate.longitude.toString()
                holder.itemView.latitude_textView.text = coordinate.latitude.toString()
            }
        }
        holder.itemView.tower_layout.setOnClickListener {
            val action = TowerListFragmentDirections.actionTowerListFragmentToTowerUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(tower: List<Tower>){
        this.towerList = tower
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = towerList.size
}