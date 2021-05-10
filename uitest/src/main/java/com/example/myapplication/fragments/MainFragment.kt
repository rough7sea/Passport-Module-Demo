package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.datamanager.DataManager
import com.example.datamanager.database.entity.Additional
import com.example.datamanager.database.entity.Coordinate
import com.example.datamanager.database.entity.Passport
import com.example.datamanager.database.entity.Tower
import com.example.datamanager.database.repository.impl.AdditionalRepository
import com.example.datamanager.database.repository.impl.CoordinateRepository
import com.example.datamanager.database.repository.impl.PassportRepository
import com.example.datamanager.database.repository.impl.TowerRepository
import com.example.datamanager.external.entities.WorkResult
import com.example.myapplication.App
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class MainFragment : Fragment() {

    private val dataManager: DataManager by lazy { App.getDataManager() }
    private val additionalRepository: AdditionalRepository by lazy {
        App.getDataManager().getRepository(Additional::class.java) as AdditionalRepository
    }
    private val towerRepository: TowerRepository by lazy {
        App.getDataManager().getRepository(Tower::class.java) as TowerRepository
    }
    private val coordinateRepository: CoordinateRepository by lazy {
        App.getDataManager().getRepository(Coordinate::class.java) as CoordinateRepository
    }
    private val passportRepository: PassportRepository by lazy {
        App.getDataManager().getRepository(Passport::class.java) as PassportRepository
    }

    private val filepath = "MyFileStorage"
    private var myExternalFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        view.tower_list_button.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_towerListFragment)
        }

        view.passport_list_button.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_passportListFragment)
        }

        view.additional_list_button.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_additionalListFragment)
        }

        view.import_button.setOnClickListener {
            import("0100101M.001.xml", view)
        }

        view.import_button2.setOnClickListener {
            import("fullTower.xml", view)
        }

        view.export_button.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
            val towers = towerRepository.findAll()
            if (towers.isNotEmpty()){
                dataManager.export(
                    towers[0],
                    File(requireActivity().getExternalFilesDir(filepath), "fullTower.xml")
                ).observe(viewLifecycleOwner,{
                    when (it){
                        is WorkResult.Completed -> {
                            Toast.makeText(activity, "Successfully exported data", Toast.LENGTH_SHORT).show()
                        }
                        is WorkResult.Loading -> {}
                        is WorkResult.Progress -> {}
                        is WorkResult.Canceled -> {}
                        is WorkResult.Error -> {
                            Toast.makeText(activity, "Unexpected error"  , Toast.LENGTH_SHORT).show()
                        }
                        else -> throw Exception("Invalid work result")
                    }
                })
                Log.i("HANDLER_TEST", "Set Tower ${towers[0].coord_id} & ${towers[0].number} to handler")
            } else {
                Log.w("HANDLER_TEST", "There are no Towers in database")
            }
//            }
        }

        view.handler_test_button.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_handlerTestFragment)
        }

        view.additional_handler_test_button.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_additionalHandlerTestFragment)
        }

        view.wipe_data_button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                coordinateRepository.deleteAll()
                additionalRepository.deleteAll()
                towerRepository.deleteAll()
                passportRepository.deleteAll()
                requireActivity().runOnUiThread {
                    Toast.makeText(activity, "Successfully wiped all data", Toast.LENGTH_SHORT).show()
                }
            }
        }

        view.init_data_button.setOnClickListener {
            initData()
        }

        view.search_test_button.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_searchTestFragment)
        }

        return view
    }

    private fun import(fileName: String, view: View) {
        myExternalFile = File(requireActivity().getExternalFilesDir(filepath), fileName)

        if (myExternalFile != null && myExternalFile!!.length() != 0L){
            dataManager.import(myExternalFile!!).observeForever {
                view.data_progress.text = "Progress: ${it.progress.toString()}"
            }
        } else {
            Log.w("FRAGMENT", "file $myExternalFile is empty")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    private fun initData(){
        CoroutineScope(Dispatchers.IO).launch {
            val passports = listOf(
                Passport(0, Date(), "test", 123, "test", "test", "test"),
            )
            val coordinates = listOf(
                Coordinate(0, Date(), 60.014443, 30.648889),
                Coordinate(0, Date(), 60.017926, 30.664085),
                Coordinate(0, Date(), 60.015179, 30.673251),
                Coordinate(0, Date(), 60.016507, 30.666208),
                Coordinate(0, Date(), 60.017678, 30.681093),
                Coordinate(0, Date(), 60.013649, 30.666173),
            )
            val towers = listOf(
                Tower(0, 1, 1, Date(), "dist 980", "980", number = "1f"),
                Tower(0, 1, 2, Date(), "dist 270", "270", number = "3f"),
                Tower(0, 1, 3, Date(), "dist 386", "386", number = "5f"),
                Tower(0, 1, 5, Date(), "dist 850", "850", number = "7f"),
            )
            val additionals = listOf(
                Additional(0, 1, 4, Date(), "obj 89", "2test"),
                Additional(0, 1, 6, Date(), "obj 226", "4test"),
            )
            passportRepository.addAll(passports)
            coordinateRepository.addAll(coordinates)
            towerRepository.addAll(towers)
            additionalRepository.addAll(additionals)
            requireActivity().runOnUiThread {
                Toast.makeText(activity, "Successfully added all data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}