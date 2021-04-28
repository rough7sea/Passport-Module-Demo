package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.datamanager.database.entity.Coordinate
import com.example.datamanager.database.entity.Tower
import com.example.datamanager.database.repository.impl.CoordinateRepository
import com.example.datamanager.database.repository.impl.TowerRepository
import com.example.datamanager.external.entities.LoadResult
import com.example.datamanager.utli.QueryBuilder
import com.example.myapplication.App
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_tower_handler_test.view.*
import kotlinx.android.synthetic.main.tower_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TowerHandlerTestFragment : Fragment() {

    private val handler = App.getDataManager()
    private val towerRepository: TowerRepository by lazy {
        App.getDataManager().getRepository(Tower::class.java) as TowerRepository
    }
    private val coordinateRepository: CoordinateRepository by lazy {
        App.getDataManager().getRepository(Coordinate::class.java) as CoordinateRepository
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_tower_handler_test, container, false)

        val handlerTowerLayout = view.handler_tower_layout

        handler.getActualObject(Tower::class.java).observe(viewLifecycleOwner, {
            when(it){
                is LoadResult.Loading -> view.liveDataTextResult.text = "Loading"
                is LoadResult.Success -> view.liveDataTextResult.text = it.data.toString()
                is LoadResult.Error -> view.liveDataTextResult.text = it.error?.message ?: it.error.toString()
            }
            it.data?.let {
                    it1 -> fillTower(handlerTowerLayout, it1)
            }
        })

        view.set_button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val towers = towerRepository.findAll()
                if (towers.isNotEmpty()){
                    val n = (towers.indices).random()
                    handler.setObjectBinding(towers[n])
                    Log.i("HANDLER_TEST", "Set Tower ${towers[n].idtf} & ${towers[n].number} to handler")
                }else {
                    Log.w("HANDLER_TEST", "There are no Towers in database")
                }
            }
        }

        view.get_button.setOnClickListener {
            handler.getActualObject(Tower::class.java)
        }

        view.next_button.setOnClickListener {
            handler.nextObject(Tower::class.java)
        }

        view.prev_button.setOnClickListener {
            handler.previousObject(Tower::class.java)
        }

        return view
    }

    private fun fillTower(handlerTowerLayout: View, tower: Tower){
        handlerTowerLayout.id_textView.text = tower.tower_id.toString()
        handlerTowerLayout.idtf_textView.text = tower.idtf
        if (tower.coord_id != null){
            val coord = coordinateRepository.getById(tower.coord_id!!)
            if (coord != null){
                handlerTowerLayout.longitude_textView.text = coord.longitude.toString()
                handlerTowerLayout.latitude_textView.text = coord.latitude.toString()
            }
        } else {
            handlerTowerLayout.longitude_textView.text = null.toString()
            handlerTowerLayout.latitude_textView.text = null.toString()
        }
    }
}