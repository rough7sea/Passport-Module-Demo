package com.example.myapplication.fragments.testfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.datamanager.database.entity.Additional
import com.example.datamanager.database.repository.impl.AdditionalRepository
import com.example.datamanager.external.entities.LoadResult
import com.example.myapplication.App
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_additional_handler_test.view.*
import kotlinx.android.synthetic.main.fragment_tower_handler_test.view.*
import kotlinx.android.synthetic.main.fragment_tower_handler_test.view.get_button
import kotlinx.android.synthetic.main.fragment_tower_handler_test.view.next_button
import kotlinx.android.synthetic.main.fragment_tower_handler_test.view.prev_button
import kotlinx.android.synthetic.main.fragment_tower_handler_test.view.set_button
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdditionalHandlerTestFragment : Fragment() {

    private val handler = App.getDataManager()
    private val additionalRepository: AdditionalRepository by lazy {
        App.getDataManager().getRepository(Additional::class.java) as AdditionalRepository
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_additional_handler_test, container, false)

        handler.getActualObject(Additional::class.java).observe(viewLifecycleOwner, {
            when(it){
                is LoadResult.Loading ->{
                    view.additional_liveDataTextResult.text = "Loading"
                }
                is LoadResult.Success -> {
                    view.additional_liveDataTextResult.text = it.data.toString()
                    view.add_number_textView.text = it.data?.number
                }
                is LoadResult.Error -> {
                    view.additional_liveDataTextResult.text = it.error?.message ?: it.error.toString()
                }
            }
        })

        view.set_button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val additionals = additionalRepository.findAll()
                if (additionals.isNotEmpty()){
                    val n = (additionals.indices).random()
                    handler.setObjectBinding(additionals[n])
                    Log.i("HANDLER_TEST", "Set Additional ${additionals[n].add_id} &" +
                            " ${additionals[n].number} to handler")
                }else {
                    Log.w("HANDLER_TEST", "There are no Additional in database")
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