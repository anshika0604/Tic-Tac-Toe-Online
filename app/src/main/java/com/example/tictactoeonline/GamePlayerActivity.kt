package com.example.tictactoeonline

import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlin.system.exitProcess

var playerTurn = true
class GamePlayerActivity : AppCompatActivity() {

    lateinit var player1Tv : TextView
    lateinit var player2Tv : TextView
    lateinit var box1Btn : Button
    lateinit var box2Btn : Button
    lateinit var box3Btn : Button
    lateinit var box4Btn : Button
    lateinit var box5Btn : Button
    lateinit var box6Btn : Button
    lateinit var box7Btn : Button
    lateinit var box8Btn : Button
    lateinit var box9Btn : Button
    lateinit var resetBtn : Button
    var Player1count = 0
    var Player2count = 0
    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()
    var emptyCells = ArrayList<Int>()
    var activeUser = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_player)
        player1Tv = findViewById(R.id.idTvPlayer1)
        player2Tv = findViewById(R.id.idTvPlayer2)
        box1Btn = findViewById(R.id.idBtnbox1)
        box2Btn = findViewById(R.id.idBtnbox2)
        box3Btn = findViewById(R.id.idBtnbox3)
        box4Btn = findViewById(R.id.idBtnbox4)
        box5Btn = findViewById(R.id.idBtnbox5)
        box6Btn = findViewById(R.id.idBtnbox6)
        box7Btn = findViewById(R.id.idBtnbox7)
        box8Btn = findViewById(R.id.idBtnbox8)
        box9Btn = findViewById(R.id.idBtnbox9)
        resetBtn = findViewById(R.id.idBtnReset)

        resetBtn.setOnClickListener {
            reset()
        }


    }

    private fun reset() {
        player1.clear()
        player2.clear()
        emptyCells.clear()
        activeUser = 1
        for(i in 1..9) {
            var buttonSelected : Button?
            buttonSelected = when(i) {
                1->box1Btn
                2->box2Btn
                3->box3Btn
                4->box4Btn
                5->box5Btn
                6->box6Btn
                7->box7Btn
                8->box8Btn
                9->box9Btn
                else->{
                    box1Btn
                }
            }
            buttonSelected.isEnabled = true
            buttonSelected.text = ""
            player1Tv.text = "Player 1 : $Player1count"
            player2Tv.text = "Player 2 : $Player2count"
        }
    }

    fun buttonClick(view: View) {
        if(playerTurn) {
            val but = view as Button
            var cellId = 0
            when(but.id) {
                R.id.idBtnbox1 ->cellId = 1
                R.id.idBtnbox2 ->cellId = 2
                R.id.idBtnbox3 ->cellId = 3
                R.id.idBtnbox4 ->cellId = 4
                R.id.idBtnbox5 ->cellId = 5
                R.id.idBtnbox6 ->cellId = 6
                R.id.idBtnbox7 ->cellId = 7
                R.id.idBtnbox8 ->cellId = 8
                R.id.idBtnbox9 ->cellId = 9
            }
            playerTurn = false
            Handler().postDelayed(Runnable { playerTurn = true }, 600)
            playNow(but, cellId)
        }
    }

    private fun playNow(buttonSelected: Button, currCell: Int) {
        val audio = MediaPlayer.create(this, R.raw.poutch)
        if(activeUser == 1) {
            buttonSelected.text = "X"
            buttonSelected.setTextColor(Color.parseColor("#EC0C0C"))
            player1.add(currCell)
            emptyCells.add(currCell)
            buttonSelected.isEnabled = false
            Handler().postDelayed(Runnable {audio.release()}, 200)
            val checkWinner = checkWinner()
            if(checkWinner == 1) {
                Handler().postDelayed(Runnable { reset() }, 2000)
            }
            else if(singleuser) {
                Handler().postDelayed(Runnable { robot() }, 500)
            } else {
                activeUser = 2
            }
        } else {
            buttonSelected.text = "0"
            audio.start()
            buttonSelected.setTextColor(Color.parseColor("D22BB8804"))
            activeUser = 1
            player2.add(currCell)
            emptyCells.add(currCell)
            Handler().postDelayed(Runnable { audio.release() }, 200)
            buttonSelected.isEnabled = false
            val checkWinner = checkWinner()
            if(checkWinner == 1) {
                Handler().postDelayed(Runnable { reset() }, 4000)
            }
        }


    }

    private fun robot() {
        val rnd = (1..9).random()
        if(emptyCells.contains(rnd)) {
            robot()
        } else {
            val buttonSelected = when(rnd) {
                1->box1Btn
                2->box2Btn
                3->box3Btn
                4->box4Btn
                5->box5Btn
                6->box6Btn
                7->box7Btn
                8->box8Btn
                9->box9Btn
                else->{
                    box1Btn
                }
            }
            emptyCells.add(rnd)
            val audio = MediaPlayer.create(this, R.raw.poutch)
            audio.start()
            Handler().postDelayed(Runnable {  audio.release()}, 500)
            buttonSelected.text = "0"
            buttonSelected.setTextColor(Color.parseColor("D22BB804"))
            player2.add(rnd)
            buttonSelected.isEnabled = true
            var checkWinner = checkWinner()
            if(checkWinner == 1) {
                Handler().postDelayed(Runnable {  reset()}, 200)
            }
        }
    }

    private fun checkWinner(): Int {
        val audio = MediaPlayer.create(this, R.raw.success)
        if((player1.contains(1) && player1.contains(2) && player1.contains(3)) ||
            (player1.contains(1) && player1.contains(4) && player1.contains(7)) ||
            (player1.contains(3) && player1.contains(6) && player1.contains(9)) ||
            (player1.contains(7) && player1.contains(8) && player1.contains(9)) ||
            (player1.contains(4) && player1.contains(5) && player1.contains(6)) ||
            (player1.contains(1) && player1.contains(5) && player1.contains(9)) ||
            (player1.contains(3) && player1.contains(5) && player1.contains(7)) ||
            (player1.contains(2) && player1.contains(5) && player1.contains(8))) {

            Player1count += 1
            buttonDisable()
            audio.start()
            disableReset()
            Handler().postDelayed(Runnable { audio.release() }, 4000)
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage("Player 1 Wins\n\n "+"Do you want to play again")
            build.setPositiveButton("Ok") {dialog, which ->
                reset()
                audio.release()
            }
            build.setNegativeButton("Exit") {dialog, which ->
                audio.release()
                exitProcess(1)
            }
            Handler().postDelayed(Runnable { build.show() }, 2000)
            return 1
        }
        else if((player2.contains(1) && player2.contains(2) && player2.contains(3)) ||
            (player2.contains(1) && player2.contains(4) && player2.contains(7)) ||
            (player2.contains(3) && player2.contains(6) && player2.contains(9)) ||
            (player2.contains(7) && player2.contains(8) && player2.contains(9)) ||
            (player2.contains(4) && player2.contains(5) && player2.contains(6)) ||
            (player2.contains(1) && player2.contains(5) && player2.contains(9)) ||
            (player2.contains(3) && player2.contains(5) && player2.contains(7)) ||
            (player2.contains(2) && player2.contains(5) && player2.contains(8))) {

            Player2count += 1
            buttonDisable()
            audio.start()
            disableReset()
            Handler().postDelayed(Runnable { audio.release() }, 4000)
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage("Player 2 Wins\n\n "+"Do you want to play again")
            build.setPositiveButton("Ok") {dialog, which ->
                reset()
                audio.release()
            }
            build.setNegativeButton("Exit") {dialog, which ->
                audio.release()
                exitProcess(1)
            }
            Handler().postDelayed(Runnable { build.show() }, 2000)
            return 1
        } else if(emptyCells.contains(1) && emptyCells.contains(2) && emptyCells.contains(3) &&
            emptyCells.contains(4) && emptyCells.contains(5) && emptyCells.contains(6) &&
            emptyCells.contains(7) && emptyCells.contains(8) && emptyCells.contains(9)
                ) {

            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage("Game Draw\n\n "+"Do you want to play again")
            build.setPositiveButton("Ok") {dialog, which ->
                reset()
            }
            build.setNegativeButton("Exit") {dialog, which ->
                exitProcess(1)
            }
            build.show()
            return 1
        }
        return 0
    }

    private fun disableReset() {
        resetBtn.isEnabled = false
        Handler().postDelayed(Runnable { resetBtn.isEnabled  = true}, 2200)
    }

    private fun buttonDisable() {
        player1.clear()
        player2.clear()
        emptyCells.clear()
        activeUser = 1
        for(i in 1..9) {
            var buttonSelected : Button
            buttonSelected = when(i) {
                1->box1Btn
                2->box2Btn
                3->box3Btn
                4->box4Btn
                5->box5Btn
                6->box6Btn
                7->box7Btn
                8->box8Btn
                9->box9Btn
                else->{
                    box1Btn
                }
            }
            buttonSelected.isEnabled = true
            buttonSelected.text = ""
            player1Tv.text = "Player 1 : $Player1count"
            player2Tv.text = "Player 2 : $Player2count"
        }
    }
}