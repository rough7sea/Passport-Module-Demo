package com.example.myapplication.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.datamanager.database.entity.Passport
import com.example.datamanager.database.repository.impl.PassportRepository
import com.example.myapplication.App
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_add_passport.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AddPassportFragment : Fragment() {

    private val passportRepository: PassportRepository by lazy {
        App.getDataManager().getRepository(Passport::class.java) as PassportRepository
    }

    private var _binding: View? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_passport, container, false)
        _binding = view

        view.addPassportButton.setOnClickListener {
            insertPassportToDB()
        }

        return view
    }

    private fun insertPassportToDB() {
        val section = binding.editSection.text.toString()
        val siteId = binding.editSiteId.text.toString()
        val sectionId = binding.editSectionId.text.toString()
        val echName = binding.editEch.text.toString()
        val echkName = binding.editEchk.text.toString()

        if (validate(section, siteId, sectionId, echName, echkName)){
            CoroutineScope(Dispatchers.IO).launch {
                val passport = Passport(0, Date(), section, siteId.toLong(), sectionId, echName, echkName)
                passportRepository.add(passport)
                requireActivity().runOnUiThread {
                    Log.i("FRAGMENT_TEST", "Next object add to DataBase: $passport")
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