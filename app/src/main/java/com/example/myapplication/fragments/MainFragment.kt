package com.example.myapplication.fragments

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.database.DatabaseConst
import com.example.myapplication.database.entity.Additional
import com.example.myapplication.database.entity.Coordinate
import com.example.myapplication.database.entity.Passport
import com.example.myapplication.database.entity.Tower
import com.example.myapplication.database.repository.AdditionalRepository
import com.example.myapplication.database.repository.CoordinateRepository
import com.example.myapplication.database.repository.PassportRepository
import com.example.myapplication.database.repository.TowerRepository
import com.example.myapplication.exchange.ExportFileManager
import com.example.myapplication.exchange.ImportFileManager
import com.example.myapplication.exchange.impl.ExportFileManagerImpl
import com.example.myapplication.exchange.impl.ImportFileManagerImpl
import com.example.myapplication.utli.QueryBuilder
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class MainFragment : Fragment() {

    private val importFileManager: ImportFileManager by lazy{
        ImportFileManagerImpl(App.getDatabaseManager())
    }

    private val exportFileManager: ExportFileManager by lazy{
        ExportFileManagerImpl(App.getDatabaseManager())
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

        if (!isExternalStorageAvailable || isExternalStorageReadOnly) {
            view.import_button.isEnabled = false
            view.import_button2.isEnabled = false
        }

        view.import_button.setOnClickListener {
            import("0100101M.001.xml", view)
        }

        view.import_button2.setOnClickListener {
            import("fullTower.xml", view)
        }


        view.export_button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                exportFileManager.export(
                        App.getDatabaseManager().towerDao().getWithParameters(
                                SimpleSQLiteQuery("select * from ${DatabaseConst.TOWER_TABLE_NAME}"))[0],
                        File(requireActivity().getExternalFilesDir(filepath), "fullTower.xml"))
            }
        }

        view.handler_test_button.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_handlerTestFragment)
        }

        view.additional_handler_test_button.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_additionalHandlerTestFragment)
        }

        view.wipe_data_button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val dbManager = App.getDatabaseManager()
                dbManager.coordinateDao().deleteAll()
                dbManager.additionalDao().deleteAll()
                dbManager.towerDao().deleteAll()
                dbManager.passportDao().deleteAll()
                requireActivity().runOnUiThread {
                    Toast.makeText(activity, "Successfully wiped all data", Toast.LENGTH_SHORT).show()
                }
            }
        }

        view.init_data_button.setOnClickListener {
            initData()
        }

        return view
    }

    private fun import(fileName: String, view: View) {
        myExternalFile = File(requireActivity().getExternalFilesDir(filepath), fileName)

        if (myExternalFile != null && myExternalFile!!.length() != 0L){
            importFileManager.import(myExternalFile!!).observeForever {
                view.data_progress.text = "Progress: ${it.progress.toString()}"
            }
        } else {
            Log.w("FRAGMENT", "file $myExternalFile is empty")
        }
    }

    private val isExternalStorageReadOnly: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState
    }

    private val isExternalStorageAvailable: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == extStorageState
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }


    private fun initData(){
        CoroutineScope(Dispatchers.IO).launch {
            val passports = listOf(
                    Passport(0, Date(), "123", 123, "123", "123", "123"),
                    Passport(0, Date(), "111", 111, "111", "111", "111"),
                    Passport(0, Date(), "112", 112, "112", "112", "112"),
            )
            val coordinates = listOf(
                    Coordinate(0, Date(), 111.2, 222.3),
                    Coordinate(0, Date(), 112.2, 223.3),
                    Coordinate(0, Date(), 113.2, 224.3)
            )
            val towers = listOf(
                    Tower(0, 1, 1, Date(), "123", "123", number = "123f"),
                    Tower(0, 1, 2, Date(), "111", "111", number = "111f"),
                    Tower(0, 1, 3, Date(), "112", "112", number = "112f"),
                    Tower(0, 1, null, Date(), "113", "113", number = "113f"),
                    Tower(0, 2, 2, Date(), "114", "114", number = "114f"),
                    Tower(0, 2, 3, Date(), "115", "115", number = "115f"),
                    Tower(0, 2, 1, Date(), "116", "116", number = "116f"),
                    Tower(0, 2, null, Date(), "117", "117", number = "117f"),
                    Tower(0, 3, 3, Date(), "118", "118", number = "118f"),
                    Tower(0, 3, 1, Date(), "119", "119", number = "119f"),
                    Tower(0, 3, 2, Date(), "120", "120", number = "120f"),
                    Tower(0, 3, null, Date(), "121", "121", number = "121f"),
                    Tower(0, 3, null, Date(), "122", "122", number = "122f"),
                    Tower(0, 3, null, Date(), "124", "124", number = "124f"),

                    )
            val additionals = listOf(
                    Additional(0, 1, 3, Date(), "syper", "2r"),
                    Additional(0, 1, 3, Date(), "syper", "4r"),
                    Additional(0, 1, null, Date(), "ko syper", "5r"),
                    Additional(0, 2, 1, Date(), "syper", "6r"),
                    Additional(0, 2, 2, Date(), "sper", "7r"),
                    Additional(0, 2, 3, Date(), "sypr", "8r"),
                    Additional(0, 2, null, Date(), "sypREer", "9r"),
                    Additional(0, 3, null, Date(), "syperRR", "10r"),
                    Additional(0, 3, 1, Date(), "ultra syper", "1r"),
            )
            PassportRepository(App.getDatabaseManager().passportDao()).addPassports(passports)
            CoordinateRepository(App.getDatabaseManager().coordinateDao()).addCoordinates(coordinates)
            TowerRepository(App.getDatabaseManager().towerDao()).addAllTowers(towers)
            AdditionalRepository(App.getDatabaseManager().additionalDao()).addAllAdditionals(additionals)
            requireActivity().runOnUiThread {
                Toast.makeText(activity, "Successfully added all data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}