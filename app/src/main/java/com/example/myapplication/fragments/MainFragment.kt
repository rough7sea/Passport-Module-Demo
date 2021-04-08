package com.example.myapplication.fragments

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.exchange.impl.ImportFileManagerImpl
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.io.File

class MainFragment : Fragment() {

    private lateinit var importFileManagerImpl: ImportFileManagerImpl
    private var filePath : String = ""
    private var uri: Uri = Uri.EMPTY


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

        view.import_button.setOnClickListener {
            importFileManagerImpl.import(File(""))
        }

        view.handler_test_button.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_handlerTestFragment)
        }

        view.additional_handler_test_button.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_additionalHandlerTestFragment)
        }

        // Passport -> 2 Tower( any count of Additional)
        // ObjectHandel(Tower)
        // InternalHandler(Additional)



//        view.filePathButton.setOnClickListener {
//            val intent = Intent()
//                .setType("*/*")
//                .setAction(Intent.ACTION_GET_CONTENT)
//
//            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
//        }

        return view
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == 111 && resultCode == RESULT_OK) {
//            data?.data?.also {
////                context?.contentResolver?.takePersistableUriPermission(
////                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION or
////                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//                uri = it
//                view?.handler_test_button?.text = uri.path
//                // Perform operations on the document using its URI.
//            }
//        }
//    }

//    private fun performFileSearch() : File {
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//
//        startActivityForResult(intent, 1234)
//        return intent.data!!.toFile()
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

}