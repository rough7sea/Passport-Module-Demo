package com.example.myapplication.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.datamanager.database.entity.Additional
import com.example.datamanager.database.entity.Coordinate
import com.example.datamanager.database.repository.impl.CoordinateRepository
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.fragments.holder.MyViewHolder
import com.example.myapplication.fragments.list.AdditionalListFragmentDirections
import kotlinx.android.synthetic.main.additional_layout.view.*
import kotlinx.android.synthetic.main.tower_layout.view.latitude_textView
import kotlinx.android.synthetic.main.tower_layout.view.longitude_textView
import java.util.Collections.emptyList

class AdditionalAdapter : RecyclerView.Adapter<MyViewHolder>()  {

    private var additionalList = emptyList<Additional>()
    private val coordinateRepository: CoordinateRepository by lazy {
        App.getDataManager().getRepository(Coordinate::class.java) as CoordinateRepository
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.additional_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = additionalList[position]
        holder.itemView.id_textView.text = currentItem.add_id.toString()
        holder.itemView.number_textView.text = currentItem.number

        currentItem.coord_id?.let {
            val coordinate = coordinateRepository.getById(it)
            if (coordinate != null) {
                holder.itemView.longitude_textView.text = coordinate.longitude.toString()
                holder.itemView.latitude_textView.text = coordinate.latitude.toString()
            }
        }

        holder.itemView.additional_layout.setOnClickListener {
            val action = AdditionalListFragmentDirections.actionAdditionalListFragmentToAdditionalUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(additional: List<Additional>){
        this.additionalList = additional
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = additionalList.size
}