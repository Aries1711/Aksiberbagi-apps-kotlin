package com.inddevid.aksiberbagi_donatur.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inddevid.aksiberbagi_donatur.R
import com.inddevid.aksiberbagi_donatur.model.BerandaProgramAll
import com.inddevid.aksiberbagi_donatur.presenter.BerandaProgramAllAdapter

class ProgramAllActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.program_all_activity)
        val toolbar: Toolbar = findViewById(R.id.upAppbarAllProgram)
        toolbar.title = "Program Galang Dana"
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        //deklarasi variabel sementara untuk array ProgramAll (Recycler view)
        val imageProgram:String = "https://aksiberbagi.com/storage/program/Sedekah%20Terbaik%20untuk%20Anak%20Yatim-banner.jpg"
        val titleAll:String = "Sedekah Terbaik Untuk Anak Yatim"
        val titleSummary:String = "Sesurga bersama Rasulullah"
        val volunteer:String = "Aksiberbagi.com"
        val fundAll:String = "765.987.079"
        val dayAll:String = "37 hari lagi"
        val arrayProgramAll = ArrayList<BerandaProgramAll>()
        arrayProgramAll.add(
            BerandaProgramAll(imageProgram, titleAll,titleSummary,volunteer,fundAll
                /**,dayAll*/
                /**,dayAll*/)
        )
        arrayProgramAll.add(
            BerandaProgramAll(imageProgram, titleAll,titleSummary,volunteer,fundAll
                /**,dayAll*/
                /**,dayAll*/)
        )
        arrayProgramAll.add(
            BerandaProgramAll(imageProgram, titleAll,titleSummary,volunteer,fundAll
                /**,dayAll*/
                /**,dayAll*/)
        )
        val myAdapterAll = BerandaProgramAllAdapter(arrayProgramAll,this)

        var mainMenuAll = findViewById(R.id.recyclerProgramAllSearch) as RecyclerView
        mainMenuAll.layoutManager = LinearLayoutManager(this)
        mainMenuAll.adapter =myAdapterAll
//        toolbar.setNavigationOnClickListener{startActivity(Intent(this@ProgramDetailActivity, DashboardActivity::class.java))}
    }
}