package com.example.myapplication.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.fragments.adapter.TowerAdapter
import com.example.myapplication.fragments.view.TowerViewModel
import kotlinx.android.synthetic.main.fragment_tower_list.view.*


class TowerListFragment : Fragment() {

    private lateinit var mTowerViewModel: TowerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tower_list, container, false)

        val adapter = TowerAdapter()
        val recyclerView = view.tower_recycleView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mTowerViewModel = ViewModelProvider(this).get(TowerViewModel::class.java)
        mTowerViewModel.readAllData.observe(viewLifecycleOwner, { towers ->
            adapter.setData(towers)
        })

        view.addTowerFloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_towerListFragment_to_addTowerFragment)
        }

        setHasOptionsMenu(true)

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete){
            deleteAllTowers()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllTowers() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setPositiveButton("Yes"){  _, _ ->
            mTowerViewModel.deleteAllTowers()
            Toast.makeText(
                requireContext(),
                "Successfully removed all towers",
                Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("No"){  _, _ -> }
        builder.setTitle("Delete all towers?")
        builder.setMessage("Are you sure you want to delete all towers?")
        builder.create().show()
    }

}