package com.example.myapplication.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.fragments.adapter.PassportAdapter
import com.example.myapplication.fragments.view.PassportViewModel
import kotlinx.android.synthetic.main.fragment_passport_list.view.*

class PassportListFragment : Fragment() {

    private lateinit var mPassportViewModel: PassportViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_passport_list, container, false)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete){
            deleteAllPassports()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllPassports() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){  _, _ ->
            mPassportViewModel.deleteAllPassports()
            Toast.makeText(
                    requireContext(),
                    "Successfully removed all passports",
                    Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){  _, _ -> }
        builder.setTitle("Delete all passports?")
        builder.setMessage("Are you sure you want to delete all passports?")
        builder.create().show()
    }

}