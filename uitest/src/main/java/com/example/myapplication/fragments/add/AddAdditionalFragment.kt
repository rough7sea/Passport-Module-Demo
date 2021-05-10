package com.example.myapplication.fragments.add

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.datamanager.database.entity.Additional
import com.example.datamanager.database.entity.Coordinate
import com.example.datamanager.database.entity.Tower
import com.example.datamanager.database.repository.impl.AdditionalRepository
import com.example.datamanager.database.repository.impl.CoordinateRepository
import com.example.datamanager.database.repository.impl.TowerRepository
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.fragments.utils.Utils.validate
import kotlinx.android.synthetic.main.fragment_add_additional.view.*
import kotlinx.android.synthetic.main.fragment_add_tower.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class AddAdditionalFragment : Fragment() {

    private val towerRepository: TowerRepository by lazy {
        App.getDataManager().getRepository(Tower::class.java) as TowerRepository
    }
    private val coordinateRepository: CoordinateRepository by lazy {
        App.getDataManager().getRepository(Coordinate::class.java) as CoordinateRepository
    }
    private val additionalRepository: AdditionalRepository by lazy {
        App.getDataManager().getRepository(Additional::class.java) as AdditionalRepository
    }

    private var _binding: View? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        binding.addTowerForAdditionalTextView
            .setAdapter(ArrayAdapter(requireContext(), R.layout.dropdown_item,
                towerRepository.findAll().map { it.tower_id.toString() }))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_additional, container, false)
        _binding = view

        view.addAdditionalButton.setOnClickListener {
            insertAdditionalIntoDataBase()
        }

        return view
    }

    private fun insertAdditionalIntoDataBase() {
        val type = binding.editAdditionalType.text.toString()
        val number = binding.editAdditionalNumber.text.toString()
        val longitude = binding.editAdditionalLongitude.text.toString()
        val latitude = binding.editAdditionalLatitude.text.toString()
        val tower_id = binding.addTowerForAdditionalTextView.text.toString()

        if (validate(number, tower_id)){
            CoroutineScope(Dispatchers.IO).launch {
                val tower = towerRepository.getById(tower_id.toLong())
                if (tower == null){
                    Log.i("FRAGMENT_TEST", "There are no tower with id[$tower_id] in system")
                    return@launch
                }
                var coord_id: Long? = null
                if (longitude.isNotEmpty() && latitude.isNotEmpty()){
                    coord_id = coordinateRepository.getOrCreateCoordinateId(latitude.toDouble(), longitude.toDouble())
                }
                val additional = Additional(0, tower_id.toLong(), coord_id, Date(), type, number)
                additionalRepository.add(additional)
                requireActivity().runOnUiThread {
                    Log.i("FRAGMENT_TEST", "Next object add to DataBase: $additional")
                    Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Fill out all fields", Toast.LENGTH_LONG).show()
        }
    }
}