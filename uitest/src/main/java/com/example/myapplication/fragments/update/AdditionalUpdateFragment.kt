package com.example.myapplication.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.datamanager.database.entity.Additional
import com.example.datamanager.database.entity.Coordinate
import com.example.datamanager.database.entity.Tower
import com.example.datamanager.database.repository.impl.AdditionalRepository
import com.example.datamanager.database.repository.impl.CoordinateRepository
import com.example.datamanager.database.repository.impl.TowerRepository
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.fragments.utils.Utils
import kotlinx.android.synthetic.main.fragment_add_additional.view.*
import kotlinx.android.synthetic.main.fragment_additional_update.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AdditionalUpdateFragment : Fragment() {

    private val args by navArgs<AdditionalUpdateFragmentArgs>()

    private val towerRepository: TowerRepository by lazy {
        App.getDataManager().getRepository(Tower::class.java) as TowerRepository
    }
    private val coordinateRepository: CoordinateRepository by lazy {
        App.getDataManager().getRepository(Coordinate::class.java) as CoordinateRepository
    }
    private val additionalRepository: AdditionalRepository by lazy {
        App.getDataManager().getRepository(Additional::class.java) as AdditionalRepository
    }

    override fun onResume() {
        super.onResume()
        binding.updateTowerForAdditionalTextView
            .setAdapter(
                ArrayAdapter(requireContext(), R.layout.dropdown_item,
                    towerRepository.findAll().map { it.tower_id.toString() })
            )

    }

    private var _binding: View? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_additional_update, container, false)
        _binding = view

        val additional = args.currentAdditional

        view.updateAdditionalType.setText(additional.type)
        view.updateAdditionalNumber.setText(additional.number)
        view.updateTowerForAdditionalTextView.setText(additional.tower_id.toString())

        additional.coord_id?.let {
            CoroutineScope(Dispatchers.IO).launch {
                val coord = coordinateRepository.getById(it)
                if (coord != null){
                    binding.updateAdditionalLongitude.setText(coord.longitude.toString())
                    binding.updateAdditionalLatitude.setText(coord.latitude.toString())
                }
            }
        }

        view.updateAdditionalButton.setOnClickListener {
            updateAdditional()
        }

        setHasOptionsMenu(true)

        return view
    }

    private fun updateAdditional() {
        val type = binding.updateAdditionalType.text.toString()
        val number = binding.updateAdditionalNumber.text.toString()
        val longitude = binding.updateAdditionalLongitude.text.toString()
        val latitude = binding.updateAdditionalLatitude.text.toString()
        val tower_id = binding.updateTowerForAdditionalTextView.text.toString()

        if (Utils.validate(number, tower_id)){
            CoroutineScope(Dispatchers.IO).launch {
                val tower = towerRepository.getById(tower_id.toLong())
                if (tower == null){
                    Log.i("FRAGMENT_TEST", "There are no tower with id[$tower_id] in system")
                    return@launch
                }
                var coord_id: Long? = null
                if (longitude.isNotEmpty() && latitude.isNotEmpty()){
                    coord_id = coordinateRepository.getOrCreateCoordinateId(latitude.toDouble(), longitude.toDouble())
                } else {
                    Toast.makeText(requireContext(), "Must be fill latitude & longitude to update coordinate",
                        Toast.LENGTH_SHORT).show()
                }
                val additional = Additional(args.currentAdditional.add_id, tower_id.toLong(), coord_id, Date(), type, number)
                additionalRepository.update(additional)
                requireActivity().runOnUiThread {
                    Log.i("FRAGMENT_TEST", "Additional update: $additional")
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
            deleteAdditional()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAdditional() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){  _, _ ->
            additionalRepository.delete(args.currentAdditional)
            Toast.makeText(
                requireContext(),
                "Additional successfully removed",
                Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){  _, _ -> }

        builder.setTitle("Delete current Additional?")
        builder.setMessage("Are you sure you want to delete current Additional?")
        builder.create().show()
    }
}