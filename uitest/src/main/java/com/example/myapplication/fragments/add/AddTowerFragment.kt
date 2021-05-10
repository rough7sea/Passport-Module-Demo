package com.example.myapplication.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
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
import java.util.*

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

    private var _binding: View? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        val list = passportRepository.findAll().map { it.passport_id.toString() }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, list)
        binding.addTowerAutoCompleteTextView.setAdapter(arrayAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_tower, container, false)
        _binding = view

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
        val passport_id = view.addTowerAutoCompleteTextView.text.toString()


        if (validate(editIDTF, assertNum, number, passport_id)){
            CoroutineScope(Dispatchers.IO).launch {
                val passport = passportRepository.getById(passport_id.toLong())
                if (passport == null){
                    Log.i("FRAGMENT_TEST", "There are no passport with id[$passport_id] in system")
                    return@launch
                }
                var coord_id: Long? = null
                if (longitude.isNotEmpty() && latitude.isNotEmpty()){
                    coord_id = coordinateRepository.add(
                        Coordinate(0, Date(), latitude.toDouble(), longitude.toDouble()))
                    if (coord_id == -1L){
                        coord_id = coordinateRepository
                            .getCoordinateByLongitudeAndLatitude(latitude.toDouble(), longitude.toDouble())!!.coord_id
                    }
                }
                val tower = Tower(0, passport_id.toLong(), coord_id,
                    idtf = editIDTF, assetNum = assertNum, number = number)
                towerRepository.add(tower)
                requireActivity().runOnUiThread {
                    Log.i("FRAGMENT_TEST", "Next object add to DataBase: $tower")
                    Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Fill out all fields", Toast.LENGTH_LONG).show()
        }
    }

    private fun validate(vararg args: String): Boolean {
        for (a in args){
            if (TextUtils.isEmpty(a)){
                return false
            }
        }
        return true
    }
}