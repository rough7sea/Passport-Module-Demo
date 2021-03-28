package com.example.myapplication.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.exchange.ImportFileManagerImpl
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.io.File

class MainFragment : Fragment() {

    private lateinit var importFileManagerImpl: ImportFileManagerImpl
    private var filePath : Uri = Uri.EMPTY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        importFileManagerImpl = ImportFileManagerImpl(App.getDatabaseManager())

        view.tower_list_button.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_towerListFragment)
        }

        view.passport_list_button.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_passportListFragment)
        }

        view.test_button.setOnClickListener {
            importFileManagerImpl.import(File(filePath.path!!))
        }

        view.filePathButton.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }

        return view
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file
            if (selectedFile != null) {
                filePath = selectedFile
                view?.filePathButton?.text = selectedFile.path
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

}