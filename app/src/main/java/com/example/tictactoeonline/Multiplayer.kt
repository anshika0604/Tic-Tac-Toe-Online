package com.example.tictactoeonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Multiplayer : AppCompatActivity() {
    lateinit var onlineBtn : Button
    lateinit var offlineBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayer)

        onlineBtn = findViewById(R.id.idBtnOnline)
        offlineBtn = findViewById(R.id.idBtnOffline)

        onlineBtn.setOnClickListener {
            singleuser = false
            startActivity(Intent(this, OnlineCodeGeneratorActivity::class.java))
        }
        offlineBtn.setOnClickListener {
            singleuser = false
            startActivity(Intent(this, GamePlayerActivity::class.java))
        }
    }
}