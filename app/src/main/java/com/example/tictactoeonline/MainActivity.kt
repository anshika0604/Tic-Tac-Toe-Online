package com.example.tictactoeonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

var singleuser = false
class MainActivity : AppCompatActivity() {

    lateinit var singlePlayerbtn : Button
    lateinit var multiPlayerbtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        singlePlayerbtn = findViewById(R.id.idBtnSinglePlayer)
        multiPlayerbtn = findViewById(R.id.idBtnMultiPlayer)

        singlePlayerbtn.setOnClickListener {
            singleuser = true;
            startActivity(Intent(this, GamePlayerActivity::class.java))
        }
        multiPlayerbtn.setOnClickListener {
            singleuser = false;
            startActivity(Intent(this, GamePlayerActivity::class.java))
        }
    }
}