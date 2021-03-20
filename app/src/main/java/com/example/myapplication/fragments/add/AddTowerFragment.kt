package com.example.myapplication.fragments.add

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.entity.Coordinate
import com.example.myapplication.view.TowerViewModel
import java.util.*
import kotlin.random.Random

/**
 * A simple [Fragment] subclass.
 * Use the [AddTowerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddTowerFragment : Fragment() {

    private lateinit var towerViewModel: TowerViewModel

    private lateinit var appDatabase: AppDatabase

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_add_tower, container, false)

        appDatabase = App.getDatabaseManager()

        towerViewModel = ViewModelProvider(this).get(TowerViewModel::class.java)

        view.findViewById<Button>(R.id.addButton).setOnClickListener {
            insertDataToDataBase(view)
        }
        return view
    }

    private fun insertDataToDataBase(view: View) {
//        val passportId = view.findViewById<EditText>(R.id.editPassportId).text
//        val editChangeDate = view.findViewById<EditText>(R.id.editChangeDate).text

        val editIDTF = view.findViewById<EditText>(R.id.editIDTF).text.toString()


        if (inputCheck(editIDTF)){

//            var passport = Passport(0, 123, 123)
//
//            appDatabase.passportDao().insert(passport)
//
//
//            passport = appDatabase.passportDao().getAll().get(0)
//            val coord = appDatabase.coordinateDao().getAll().get(0)
//
//            val tower = Tower(0, coord.coord_id,
//                    passport.passport_id, 123, "some info")
//
//            towerViewModel.addTower(tower)

            val rand = Random(23)

            val coordinate = Coordinate(
                    latitude =  rand.nextInt(),
                    longitude =  rand.nextInt())

            appDatabase.coordinateDao().insert(coordinate)


            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()

            findNavController().navigate(R.id.action_addFragment_to_FirstFragment)
        } else {

            Toast.makeText(requireContext(), "Fill out all fields", Toast.LENGTH_LONG).show()
        }

    }

    private fun inputCheck(editIDTF: String): Boolean{
//        return !(TextUtils.isEmpty(editIDTF) && editChangeDate.isEmpty())
        return !TextUtils.isEmpty(editIDTF)
    }


}