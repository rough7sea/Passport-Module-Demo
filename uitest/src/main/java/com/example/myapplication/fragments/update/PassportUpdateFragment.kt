package com.example.myapplication.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.datamanager.database.entity.Passport
import com.example.datamanager.database.repository.impl.PassportRepository
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.fragments.utils.Utils.validate
import kotlinx.android.synthetic.main.fragment_add_passport.view.*
import kotlinx.android.synthetic.main.fragment_passport_update.*
import kotlinx.android.synthetic.main.fragment_passport_update.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class PassportUpdateFragment : Fragment() {

    private val args by navArgs<PassportUpdateFragmentArgs>()

    private val passportRepository: PassportRepository by lazy {
        App.getDataManager().getRepository(Passport::class.java) as PassportRepository
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_passport_update, container, false)

        val passport = args.currentPassport
        view.updateSection.setText(passport.sectionName)
        view.updateSiteId.setText(passport.siteId.toString())
        view.updateSectionId.setText(passport.sectionId)
        view.updateEch.setText(passport.echName)
        view.updateEchk.setText(passport.echkName)

        view.updatePassportButton.setOnClickListener {
            updatePassport()
        }

        setHasOptionsMenu(true)

        return view
    }

    private fun updatePassport() {
        val section = updateSection.text.toString()
        val siteId = updateSiteId.text.toString()
        val sectionId = updateSectionId.text.toString()
        val echName = updateEch.text.toString()
        val echkName = updateEchk.text.toString()

        if (validate(section, siteId, sectionId, echName, echkName)){
            CoroutineScope(Dispatchers.IO).launch {
                val passport = Passport(args.currentPassport.passport_id, Date(), section,
                    siteId.toLong(), sectionId, echName, echkName)
                passportRepository.update(passport)
                requireActivity().runOnUiThread {
                    Log.i("FRAGMENT_TEST", "Object updated: $passport")
                    Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Fill out all fields", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete){
            deletePassport()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deletePassport() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){  _, _ ->
            passportRepository.delete(args.currentPassport)
            Toast.makeText(
                requireContext(),
                "Passport successfully removed",
                Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){  _, _ ->

        }
        builder.setTitle("Delete current Passport?")
        builder.setMessage("Are you sure you want to delete current Passport?")
        builder.create().show()
    }

}