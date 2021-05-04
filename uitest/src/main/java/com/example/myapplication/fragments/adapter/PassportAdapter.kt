package com.example.myapplication.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.datamanager.database.entity.Passport
import com.example.myapplication.fragments.holder.MyViewHolder
import kotlinx.android.synthetic.main.passport_layout.view.*

class PassportAdapter : RecyclerView.Adapter<MyViewHolder>() {

    private var passportList = emptyList<Passport>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.passport_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = passportList[position]
        holder.itemView.passport_id_textView.text = currentItem.passport_id.toString()
        holder.itemView.passport_sectionName_editText.text = currentItem.sectionName
        holder.itemView.passport_changeDate_editText.text = currentItem.changeDate.toString()
    }

    override fun getItemCount(): Int = passportList.size

    fun setData(passports: List<Passport>){
        this.passportList = passports
        notifyDataSetChanged()
    }
}