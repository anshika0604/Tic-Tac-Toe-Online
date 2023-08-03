package com.example.tictactoeonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.TextSnapshot
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

var isCodeMaker = true
var code = "null"
var codeFound = false
var checkTemp = true
var keyValue : String = "null"

class OnlineCodeGeneratorActivity : AppCompatActivity() {

    lateinit var headTv : TextView
    lateinit var codeEdit : EditText
    lateinit var createCodebtn : Button
    lateinit var joinCodebtn : Button
    lateinit var loadingPB : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_code_generator)

        headTv = findViewById(R.id.idTvHead)
        codeEdit = findViewById(R.id.idEditCode)
        createCodebtn = findViewById(R.id.idBtnCreate)
        joinCodebtn = findViewById(R.id.idBtnJoin)
        loadingPB = findViewById(R.id.idPBLoading)

        createCodebtn.setOnClickListener {
            code = "null"
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = codeEdit.text.toString()
            createCodebtn.visibility = View.GONE
            joinCodebtn.visibility = View.GONE
            headTv.visibility = View.GONE
            codeEdit.visibility = View.GONE
            loadingPB.visibility = View.VISIBLE
            if(code != "null" && code != "") {
                isCodeMaker = true
                FirebaseDatabase.getInstance().reference.child("codes").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var check = isValueAvailable(snapshot, code)
                        Handler().postDelayed({
                            if(check == true) {
                                createCodebtn.visibility = View.VISIBLE
                                joinCodebtn.visibility = View.VISIBLE
                                headTv.visibility = View.VISIBLE
                                codeEdit.visibility = View.VISIBLE
                                loadingPB.visibility = View.GONE
                            } else {
                                FirebaseDatabase.getInstance().reference.child("codes").push().setValue(code)
                                isValueAvailable(snapshot, code)
                                checkTemp = false
                                Handler().postDelayed({
                                    accepted()
                                    Toast.makeText(this@OnlineCodeGeneratorActivity, "Please Don't Go Back", Toast.LENGTH_SHORT).show()
                                }, 300)
                            }
                        }, 2000)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            } else {
                createCodebtn.visibility = View.VISIBLE
                joinCodebtn.visibility = View.VISIBLE
                headTv.visibility = View.VISIBLE
                codeEdit.visibility = View.VISIBLE
                loadingPB.visibility = View.GONE
                Toast.makeText(this@OnlineCodeGeneratorActivity, "Please Enter a Valid Code", Toast.LENGTH_SHORT).show()
            }
        }
        joinCodebtn.setOnClickListener {
            code = "null"
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = codeEdit.text.toString()

            if(code != "null" && code != "") {
                createCodebtn.visibility = View.GONE
                joinCodebtn.visibility = View.GONE
                headTv.visibility = View.GONE
                codeEdit.visibility = View.GONE
                loadingPB.visibility = View.VISIBLE
                isCodeMaker = false
                FirebaseDatabase.getInstance().reference.child("codes").addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var data : Boolean = isValueAvailable(snapshot, code)
                        Handler().postDelayed({
                            if(data == true) {
                                codeFound = true
                                accepted()
                                createCodebtn.visibility = View.VISIBLE
                                joinCodebtn.visibility = View.VISIBLE
                                headTv.visibility = View.VISIBLE
                                codeEdit.visibility = View.VISIBLE
                                loadingPB.visibility = View.GONE
                            } else {
                                createCodebtn.visibility = View.VISIBLE
                                joinCodebtn.visibility = View.VISIBLE
                                headTv.visibility = View.VISIBLE
                                codeEdit.visibility = View.VISIBLE
                                loadingPB.visibility = View.GONE
                                Toast.makeText(this@OnlineCodeGeneratorActivity, "Invalid Code", Toast.LENGTH_SHORT).show()
                            }
                        }, 2000)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }else {
                Toast.makeText(this@OnlineCodeGeneratorActivity, "Please Enter a Valid Code", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun accepted() {
        startActivity(Intent(this, OnlineMultiPlayerGameActivity::class.java))
        createCodebtn.visibility = View.VISIBLE
        joinCodebtn.visibility = View.VISIBLE
        headTv.visibility = View.VISIBLE
        codeEdit.visibility = View.VISIBLE
        loadingPB.visibility = View.GONE
    }
    fun isValueAvailable(snapshot: DataSnapshot, code : String): Boolean{
        var data = snapshot.children
        data.forEach{
            var value = it.getValue().toString()
            if(value == code){
                keyValue = it.key.toString()
                return true
            }
        }
        return false
    }
}