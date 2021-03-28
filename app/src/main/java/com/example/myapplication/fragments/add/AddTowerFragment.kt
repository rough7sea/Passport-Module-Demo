package com.example.myapplication.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.database.entity.Tower
import com.example.myapplication.fragments.view.CoordinateViewModel
import com.example.myapplication.fragments.view.PassportViewModel
import com.example.myapplication.fragments.view.TowerViewModel
import kotlinx.android.synthetic.main.fragment_add_tower.view.*

class AddTowerFragment : Fragment() {

    private lateinit var towerViewModel: TowerViewModel
    private lateinit var mPassportViewModel: PassportViewModel
    private lateinit var mCoordinateViewModel: CoordinateViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_tower, container, false)

        towerViewModel = ViewModelProvider(this).get(TowerViewModel::class.java)
        mPassportViewModel = ViewModelProvider(this).get(PassportViewModel::class.java)
        mCoordinateViewModel = ViewModelProvider(this).get(CoordinateViewModel::class.java)


        view.addButton.setOnClickListener {
            insertDataToDataBase(view)
        }

        view.editPassportField.setOnClickListener {
            val action = AddTowerFragmentDirections.actionAddTowerFragmentToPassportSelectionListFragment()
            view.findNavController().navigate(action)
        }

        return view
    }

    private fun insertDataToDataBase(view: View) {

        val editIDTF = view.editIDTF.text.toString()

        if (inputCheck(editIDTF)){

            val passport = mPassportViewModel.readAllData.value!![0]
            val coordinate = mCoordinateViewModel.readAllData.value!![0]

            val tower = Tower(0, coordinate.coord_id, passport.passport_id, idtf =  editIDTF)

            towerViewModel.addTower(tower)

            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()

            findNavController().navigate(R.id.action_addTowerFragment_to_towerListFragment)

        } else {
            Toast.makeText(requireContext(), "Fill out all fields", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(editIDTF: String): Boolean {
        return !TextUtils.isEmpty(editIDTF)
    }

}