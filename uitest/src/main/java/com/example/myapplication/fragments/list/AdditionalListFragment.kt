package com.example.myapplication.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.fragments.adapter.AdditionalAdapter
import com.example.myapplication.fragments.view.AdditionalViewModel
import kotlinx.android.synthetic.main.fragment_additional_list.view.*


class AdditionalListFragment : Fragment() {

    private lateinit var mAdditionalViewModel: AdditionalViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_additional_list, container, false)

        val adapter = AdditionalAdapter()
        val recyclerView = view.additional_recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mAdditionalViewModel = ViewModelProvider(this).get(AdditionalViewModel::class.java)
        mAdditionalViewModel.readAllData.observe(viewLifecycleOwner, { towers ->
            adapter.setData(towers)
        })

        view.addAdditionalFloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_additionalListFragment_to_addAdditionalFragment)
        }

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete){
            deleteAllAdditionals()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllAdditionals() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setPositiveButton("Yes"){  _, _ ->
            mAdditionalViewModel.deleteAllAdditionals()
            Toast.makeText(
                    requireContext(),
                    "Successfully removed all additionals",
                    Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("No"){  _, _ -> }
        builder.setTitle("Delete all additionals?")
        builder.setMessage("Are you sure you want to delete all additionals?")
        builder.create().show()
    }

}