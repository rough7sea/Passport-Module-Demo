package com.example.myapplication.fragments

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.exchange.ExportFileManager
import com.example.myapplication.exchange.ImportFileManager
import com.example.myapplication.exchange.impl.ExportFileManagerImpl
import com.example.myapplication.exchange.impl.ImportFileManagerImpl
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

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
            import_button.isEnabled = false
        }

        view.import_button.setOnClickListener {
            myExternalFile = File(requireActivity().getExternalFilesDir(filepath),
//                "fullTower.xml")
                "0100101M.001.xml")

            if (myExternalFile != null && myExternalFile!!.length() != 0L){
                importFileManager.import(myExternalFile!!).observeForever {
                    view.data_progress.text = "Progress: ${it.progress.toString()}"
                }

            } else {
                Log.w("FRAGMENT", "file $myExternalFile is empty")
            }
        }


        view.export_button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                requireActivity().runOnUiThread {
                    Toast.makeText(activity, "Successfully started", Toast.LENGTH_SHORT).show()
                }

                exportFileManager.export(App.getDatabaseManager().towerDao().getById(52),
                    File(requireActivity().getExternalFilesDir(filepath), "fullTower.xml"))

                requireActivity().runOnUiThread {
                    Toast.makeText(activity, "data save", Toast.LENGTH_SHORT).show()
                }
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

        return view
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
}