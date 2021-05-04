package com.example.myapplication.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.datamanager.database.entity.Coordinate
import com.example.datamanager.database.entity.Passport
import com.example.datamanager.database.entity.Tower
import com.example.datamanager.database.repository.impl.CoordinateRepository
import com.example.datamanager.database.repository.impl.PassportRepository
import com.example.datamanager.database.repository.impl.TowerRepository
import com.example.myapplication.App
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_add_tower.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTowerFragment : Fragment() {

    private val towerRepository: TowerRepository by lazy {
        App.getDataManager().getRepository(Tower::class.java) as TowerRepository
    }
    private val coordinateRepository: CoordinateRepository by lazy {
        App.getDataManager().getRepository(Coordinate::class.java) as CoordinateRepository
    }
    private val passportRepository: PassportRepository by lazy {
        App.getDataManager().getRepository(Passport::class.java) as PassportRepository
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_tower, container, false)

        view.addTowerButton.setOnClickListener {
            insertDataToDataBase(view)
        }

        return view
    }

    private fun insertDataToDataBase(view: View) {

        val editIDTF = view.editIDTF.text.toString()
        val assertNum = view.editAssertNum.text.toString()
        val number = view.editNumber.text.toString()
        val longitude = view.editLongitude.text.toString()
        val latitude = view.editLatitude.text.toString()


        if (inputCheck(editIDTF, assertNum, number)){
            CoroutineScope(Dispatchers.IO).launch {
                val passports = passportRepository.findAll()
                if (passports.isEmpty()){
                    Log.i("FRAGMENT_TEST", "There are no any passport in system")
                    return@launch
                }
                var insert: Long? = null
                if (longitude.isNotEmpty() && latitude.isNotEmpty()){
                    insert = coordinateRepository.add(Coordinate(longitude = longitude.toDouble(), latitude = latitude.toDouble()))
                    if (insert == -1L){
                        insert = coordinateRepository.getCoordinateByLongitudeAndLatitude(longitude.toDouble(), latitude.toDouble())!!.coord_id
                    }
                }
                val tower = Tower(0, passports[(passports.indices).random()].passport_id, insert,
                    idtf = editIDTF, assetNum = assertNum, number = number)
                towerRepository.add(tower)
                requireActivity().runOnUiThread {
                    Log.i("FRAGMENT_TEST", "Next object add to DataBase: $tower")
                    Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
                }
            }
            findNavController().navigate(R.id.action_addTowerFragment_to_towerListFragment)
        } else {
            Toast.makeText(requireContext(), "Fill out all fields", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(editIDTF: String, assertNum: String, number: String): Boolean {
        return !TextUtils.isEmpty(editIDTF) && !TextUtils.isEmpty(assertNum) && !TextUtils.isEmpty(number)
    }

}