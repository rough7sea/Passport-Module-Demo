package com.example.myapplication.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TowerListFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_tower_list, container, false)

        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_addFragment)
        }

        return view
    }

}