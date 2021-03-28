package com.example.myapplication.fragments.selection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.fragments.adapter.PassportAdapter
import com.example.myapplication.fragments.view.PassportViewModel
import kotlinx.android.synthetic.main.fragment_passport_list.view.*

class PassportSelectionListFragment : Fragment() {

    private lateinit var mPassportViewModel: PassportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_passport_selection_list, container, false)

        val adapter = PassportAdapter()
        val recyclerView = view.passport_recycleView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mPassportViewModel = ViewModelProvider(this).get(PassportViewModel::class.java)
        mPassportViewModel.readAllData.observe(viewLifecycleOwner, { passports ->
            adapter.setData(passports)
        })


        setHasOptionsMenu(true)

        return view
    }

}