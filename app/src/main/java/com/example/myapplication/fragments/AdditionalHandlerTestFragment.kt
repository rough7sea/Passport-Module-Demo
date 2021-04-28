package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.database.DatabaseConst
import com.example.myapplication.database.entity.Additional
import com.example.myapplication.external.entities.LoadResult
import kotlinx.android.synthetic.main.fragment_tower_handler_test.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdditionalHandlerTestFragment : Fragment() {

    private val handler = App.getDataManager()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_additional_handler_test, container, false)

        handler.getActualObject(Additional::class.java).observe(viewLifecycleOwner, {
            when(it){
                is LoadResult.Loading -> view.liveDataTextResult.text = "Loading"
                is LoadResult.Success -> view.liveDataTextResult.text = it.data.toString()
                is LoadResult.Error -> view.liveDataTextResult.text = it.error?.message ?: it.error.toString()
            }
        })

        view.set_button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val additionals = App.getDatabase().additionalDao().getWithParameters(
                        SimpleSQLiteQuery("select * from ${DatabaseConst.ADDITIONAL_TABLE_NAME}"))
                if (additionals.isNotEmpty()){
                    handler.setObjectBinding(additionals[0])
                    Log.i("HANDLER_TEST", "Set Additional ${additionals[0].add_id} & ${additionals[0].number} to handler")
                }else {
                    Log.w("HANDLER_TEST", "No Additional in database")
                }
            }
        }

        view.get_button.setOnClickListener {
            handler.getActualObject(Additional::class.java)
        }

        view.next_button.setOnClickListener {
            handler.nextObject(Additional::class.java)
        }

        view.prev_button.setOnClickListener {
            handler.previousObject(Additional::class.java)
        }

        return view
    }

}