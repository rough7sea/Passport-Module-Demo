package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.database.entity.Additional
import com.example.myapplication.database.repository.AdditionalRepository
import com.example.myapplication.external.entities.LoadResult
import com.example.myapplication.external.handler.impl.InternalObjectBindingHandlerImpl
import com.example.myapplication.external.handler.impl.ObjectBindingHandlerImpl
import kotlinx.android.synthetic.main.fragment_tower_handler_test.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AdditionalHandlerTestFragment : Fragment() {

    private val handler = InternalObjectBindingHandlerImpl(App.getDatabaseManager())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_additional_handler_test, container, false)

        handler.getLiveDataResult().observe(viewLifecycleOwner, {
            when(it){
                is LoadResult.Loading -> view.liveDataTextResult.text = "Loading"
                is LoadResult.Success -> view.liveDataTextResult.text = it.data.toString()
                is LoadResult.Error -> view.liveDataTextResult.text = it.error?.message ?: it.error.toString()
            }
        })

        val additionalRepository =
            AdditionalRepository(App.getDatabaseManager().additionalDao())
        val list = listOf(
            Additional(0, 3, null, Date(), "syper", "2r"),
            Additional(0, 3, null, Date(), "syper", "4r"),
            Additional(0, 3, null, Date(), "ko syper", "5r"),
            Additional(0, 3, 1, Date(), "syper", "6r"),
            Additional(0, 3, null, Date(), "sper", "7r"),
            Additional(0, 3, null, Date(), "sypr", "8r"),
            Additional(0, 3, null, Date(), "sypREer", "9r"),
            Additional(0, 3, null, Date(), "syperRR", "10r"),
            Additional(0, 3, 1, Date(), "ultra syper", "1r"),
        )
        additionalRepository.addAllAdditionals(list)

        view.set_button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                val additional = additionalRepository.findById(3)
                handler.setInternalObject(additional)
            }
        }

        view.get_button.setOnClickListener {
            handler.getActualInternalObject()
        }

        view.next_button.setOnClickListener {
            handler.nextObject()
        }

        view.prev_button.setOnClickListener {
            handler.previousObject()
        }

        return view
    }

}