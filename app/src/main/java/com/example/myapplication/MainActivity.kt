package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.database.AppDatabase

class MainActivity : AppCompatActivity() {

    private val dataManager: AppDatabase by lazy { App.getDatabaseManager() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        setSupportActionBar()
//        setupActionBarWithNavController(findNavController(R.id.fragment))


//        setSupportActionBar(findViewById(R.id.toolbar))
//
//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

//        findViewById<Button>(R.id.button).setOnClickListener {

//            val tower = Tower(Random(33).nextLong(),
//                    Random(33).nextLong(),
//                    Random(33).toString())
//            val passport = Passport()
//            dataManager.passportDao().insert(passport)
//            tower.passportId = passport.passport_id
//            dataManager.towerDao().insert(tower)
//            val all = dataManager.towerDao().getAll()
//            all.forEach{ println(it) }


//        }
    }
}