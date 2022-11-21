package com.example.onitama

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var setupgame : GameSetup
    lateinit var textView6 : TextView
    private var card1 = ""
    private var card2 = ""
    private var card3 = ""
    private var card4 = ""
    private var card5 = ""
    private lateinit var card1arr : Array<IntArray>
    private lateinit var card2arr : Array<IntArray>
    private lateinit var card3arr : Array<IntArray>
    private lateinit var card4arr : Array<IntArray>
    private lateinit var card5arr : Array<IntArray>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView6 = findViewById(R.id.textView6)

        setupgame = GameSetup()//membuat objek game setup

        //untuk merandom card yang ada agar tidak kembar
        var ctrcard = 1
        do {
            if (ctrcard==1){
                card1 = setupgame.getNamecard((0..15).random())
                card1arr = setupgame.getCard(card1)
                ctrcard++
            }
            else if (ctrcard==2){
                card2 = setupgame.getNamecard((0..15).random())
                if (card2!=card1){
                    card2arr = setupgame.getCard(card2)
                    ctrcard++
                }
            }
            else if (ctrcard==3){
                card3 = setupgame.getNamecard((0..15).random())
                if (card3!=card2&&card3!=card1){
                    card3arr = setupgame.getCard(card3)
                    ctrcard++
                }
            }
            else if (ctrcard==4){
                card4 = setupgame.getNamecard((0..15).random())
                if (card4!=card3&&card4!=card2&&card4!=card1){
                    card4arr = setupgame.getCard(card4)
                    ctrcard++
                }
            }
            else if (ctrcard==5){
                card5 = setupgame.getNamecard((0..15).random())
                if (card5!=card4&&card5!=card3&&card5!=card2&&card5!=card1){
                    card5arr = setupgame.getCard(card5)
                    ctrcard++
                }
            }
        }while (ctrcard!=6)
    }
}