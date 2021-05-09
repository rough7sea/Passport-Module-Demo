package com.example.myapplication.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datamanager.database.entity.Coordinate
import com.example.datamanager.database.entity.Passport
import com.example.datamanager.database.entity.Tower
import com.example.datamanager.database.repository.impl.CoordinateRepository
import com.example.datamanager.database.repository.impl.PassportRepository
import com.example.datamanager.database.repository.impl.TowerRepository
import com.example.myapplication.App
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_add_tower.view.*
import kotlinx.android.synthetic.main.fragment_add_tower.view.editAssertNum
import kotlinx.android.synthetic.main.fragment_add_tower.view.editIDTF
import kotlinx.android.synthetic.main.fragment_add_tower.view.editLatitude
import kotlinx.android.synthetic.main.fragment_add_tower.view.editLongitude
import kotlinx.android.synthetic.main.fragment_add_tower.view.editNumber
import kotlinx.android.synthetic.main.fragment_tower_update.*
import kotlinx.android.synthetic.main.fragment_tower_update.view.*
import kotlinx.android.synthetic.main.tower_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class TowerUpdateFragment : Fragment() {

    private val args by navArgs<TowerUpdateFragmentArgs>()

    private val towerRepository: TowerRepository by lazy {
        App.getDataManager().getRepository(Tower::class.java) as TowerRepository
    }
    private val coordinateRepository: CoordinateRepository by lazy {
        App.getDataManager().getRepository(Coordinate::class.java) as CoordinateRepository
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tower_update, container, false)
        val tower = args.currentTower

        view.editIDTF.setText(tower.idtf)
        view.editAssertNum.setText(tower.assetNum)
        view.editNumber.setText(tower.number)

        tower.coord_id?.let {
            CoroutineScope(Dispatchers.IO).launch {
                val coord = coordinateRepository.getById(it)
                if (coord != null){
                    view.editLongitude.setText(coord.longitude.toString())
                    view.editLatitude.setText(coord.latitude.toString())
                }
            }
        }

        view.updateTowerButton.setOnClickListener {
            updateTower()
        }

        setHasOptionsMenu(true)

        return view
    }

    private fun updateTower() {
        val editIDTF = editIDTF.text.toString()
        val assertNum = editAssertNum.text.toString()
        val number = editNumber.text.toString()
        val longitude = editLongitude.text.toString()
        val latitude = editLatitude.text.toString()

        if (validate(editIDTF, assertNum, number)){
            var coord_id : Long? = null
            if (validate(longitude, latitude)){
                coord_id = coordinateRepository.add(
                    Coordinate(0, Date(), latitude.toDouble(), longitude.toDouble()))
                if (coord_id == -1L){
                    coord_id = coordinateRepository
                        .getCoordinateByLongitudeAndLatitude(latitude.toDouble(), longitude.toDouble())!!.coord_id
                }
            } else{
                Toast.makeText(requireContext(), "Must be fill latitude & longitude to update coordinate",
                    Toast.LENGTH_SHORT).show()
            }
            towerRepository.update(Tower(args.currentTower.tower_id, args.currentTower.passport_id,
                coord_id, Date(), editIDTF, assertNum, number = number))
            Toast.makeText(requireContext(), "Tower update", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Fill out all fields", Toast.LENGTH_SHORT).show()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete){
            deleteTower()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteTower() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){  _, _ ->
            towerRepository.delete(args.currentTower)
            Toast.makeText(
                requireContext(),
                "Tower successfully removed",
                Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){  _, _ ->

        }
        builder.setTitle("Delete current Tower?")
        builder.setMessage("Are you sure you want to delete current Tower?")
        builder.create().show()
    }

}