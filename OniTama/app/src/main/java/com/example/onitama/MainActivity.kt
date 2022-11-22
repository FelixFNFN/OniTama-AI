package com.example.onitama

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var papan:ArrayList<ImageButton>
    lateinit var setupgame : GameSetup
    lateinit var textView6 : TextView
    lateinit var cardP1_1:Button
    lateinit var cardP1_2:Button
    lateinit var cardNextP1:Button
    lateinit var cardP2_1:Button
    lateinit var cardP2_2:Button
    lateinit var cardNextP2:Button
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
    private var turn = "P1"
    private var chosecard = ""
    private var pionpick = -1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //deklarasi seluruh component
        textView6 = findViewById(R.id.textView6)
        cardP1_1 = findViewById(R.id.cardP1_1)
        cardP1_2 = findViewById(R.id.cardP1_2)
        cardNextP1 = findViewById(R.id.cardNextP1)
        cardP2_1 = findViewById(R.id.cardP2_1)
        cardP2_2 = findViewById(R.id.cardP2_2)
        cardNextP2 = findViewById(R.id.cardNextP2)
        papan = ArrayList<ImageButton>()

        for (i in 1..5){//dalam loop ini akan menambahkan tiap kotak dalam papan ke arraylist
            for (j in 1..5){
                var kotakID ="img_${i}_${j}"
                val resourcesID = this.resources.getIdentifier(kotakID, "id", packageName)
                papan.add(findViewById(resourcesID))
                //di bawah ini untuk menambahkan tag ke setiap kotak yang ditempati pion
                if (i==1){
                    if (j==3){
                        papan[papan.size-1].setTag("kingP2")
                    }
                    else{
                        papan[papan.size-1].setTag("armyP2")
                    }
                }
                else if (i==5){
                    if (j==3){
                        papan[papan.size-1].setTag("kingP1")
                    }
                    else{
                        papan[papan.size-1].setTag("armyP1")
                    }
                }
                else{
                    papan[papan.size-1].setTag("empty")
                }
            }
        }

        setupgame = GameSetup()//membuat objek game setup

        setallcard()// merandom card untuk player 1 dan 2

        cardP1_1.setOnClickListener {
            if (turn=="P1") {
                chosecard = cardP1_1.text.toString()
                Toast.makeText(this, "$chosecard", Toast.LENGTH_SHORT).show()
            }
        }
        cardP1_2.setOnClickListener {
            if (turn=="P1") {
                chosecard = cardP1_2.text.toString()
                Toast.makeText(this, "$chosecard", Toast.LENGTH_SHORT).show()
            }
        }
        cardP2_1.setOnClickListener {
            if (turn=="P2") {
                chosecard = cardP2_1.text.toString()
                Toast.makeText(this, "$chosecard", Toast.LENGTH_SHORT).show()
            }
        }
        cardP2_2.setOnClickListener {
            if (turn=="P2") {
                chosecard = cardP2_2.text.toString()
                Toast.makeText(this, "$chosecard", Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun setallcard(){
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


        cardP1_1.setText(card1)
        cardP1_2.setText(card2)
        cardNextP1.setText(card5)
        cardP2_1.setText(card3)
        cardP2_2.setText(card4)
        cardNextP2.setText("")

    }

    fun kotakClick(v: View){
        //dibawah ini utnuk mendapatkan tempat kotak diklik
        var ctridx=0
        for(kotak in papan){
            if (kotak.id == v.id){
                break
            }
            ctridx++
        }
//        papan[ctridx].setBackgroundColor(resources.getColor(R.color.valid_papan))

        //di bawah ini melakukan pengecekan
        if (turn=="P1"){
            if (chosecard!=""&&(papan[ctridx].getTag().toString()=="kingP1"||papan[ctridx].getTag().toString()=="armyP1")){
                var idxlangkah = setupgame.getCard(chosecard)// mendapatkan array dari card yang diklik
                pionpick = ctridx//menyimpan index nilai pion yang dipilih
                whiteAllboard()// memutihkan seluruh papan
                var posvalid = ctridx
                for (i in 0..idxlangkah.size-1){
                    posvalid = ctridx
                    //karena papan menggunakan arraylist dan card yang dibuat dalam bentuk array maka rumus yang digunakan yaitu
                    //idxlangkah yang x ditambah dengan posisi pion yang diklik
                    //idxlangkah yang y * 5 dan dikurangi posisi pion yang diklik, dalam kasus ini p1 arah jalannya ke atas maka harus dikurangi
                    posvalid+=(idxlangkah[i][0])//untuk menghitung kordinat x
                    posvalid-=(5*idxlangkah[i][1])// untuk menghitung kordinat y
//                    Log.i("kor x", Arrays.deepToString(arrayOf(idxlangkah[i][0])) )
//                    Log.i("kor y", Arrays.deepToString(arrayOf(idxlangkah[i][1])) )
                    try {
                        if (papan[posvalid].getTag()!="armyP1"&&papan[posvalid].getTag()!="kingP1") {// melakukan pengecekan apakah langkah yang divalidasi merupakan tempat pin kelompok P1
                            papan[posvalid].setBackgroundColor(resources.getColor(R.color.valid_papan))
                            papan[posvalid].setTag("valid")
                        }
                    }
                    catch (e: Exception){

                    }
                }

            }
            else if (papan[ctridx].getTag().toString()=="valid"){// kondisi jika klik pada kotak yang sudah valid untuk melangkah
                papan[ctridx].setImageDrawable(resources.getDrawable(R.drawable.blue_pawn))//merubah posisi pion pada kotak yang valid
                papan[ctridx].setTag(papan[pionpick].getTag())//set tag posisi pion baru dengan tag yang sebelumnya di posisi lama

                whiteAllboard()// memutihkan semua papan
                papan[pionpick].setTag("empty")// mengubah tag posisi pion yang sebelumnya ke empty
                papan[pionpick].setImageDrawable(resources.getDrawable(R.drawable.blank_background))// menghapus gambar pion pada posisi sebelumnya

                changeTurn()
            }
        }
        else if (turn=="P2"){
            if (chosecard!=""&&(papan[ctridx].getTag().toString()=="kingP2"||papan[ctridx].getTag().toString()=="armyP2")){
                var idxlangkah = setupgame.getCard(chosecard)// mendapatkan array dari card yang diklik
                pionpick = ctridx//menyimpan index nilai pion yang dipilih
                whiteAllboard()// memutihkan seluruh papan
                var posvalid = ctridx
                for (i in 0..idxlangkah.size-1){
                    posvalid = ctridx
                    //karena papan menggunakan arraylist dan card yang dibuat dalam bentuk array maka rumus yang digunakan yaitu
                    //idxlangkah yang x dikurangi dengan posisi pion yang diklik
                    //idxlangkah yang y * 5 dan ditambah posisi pion yang diklik, dalam kasus ini p2 arah jalannya ke bawah maka harus ditambah
                    posvalid-=(idxlangkah[i][0])//untuk menghitung kordinat x
                    posvalid+=(5*idxlangkah[i][1])// untuk menghitung kordinat y
                    try {
                        if (papan[posvalid].getTag()!="armyP2"&&papan[posvalid].getTag()!="kingP2") {// melakukan pengecekan apakah langkah yang divalidasi merupakan tempat pin kelompok P1
                            papan[posvalid].setBackgroundColor(resources.getColor(R.color.valid_papan))
                            papan[posvalid].setTag("valid")
                        }
                    }
                    catch (e: Exception){

                    }
                }

            }
            else if (papan[ctridx].getTag().toString()=="valid"){// kondisi jika klik pada kotak yang sudah valid untuk melangkah
                papan[ctridx].setImageDrawable(resources.getDrawable(R.drawable.red_pawn))//merubah posisi pion pada kotak yang valid
                papan[ctridx].setTag(papan[pionpick].getTag())//set tag posisi pion baru dengan tag yang sebelumnya di posisi lama

                whiteAllboard()// memutihkan semua papan
                papan[pionpick].setTag("empty")// mengubah tag posisi pion yang sebelumnya ke empty
                papan[pionpick].setImageDrawable(resources.getDrawable(R.drawable.blank_background))// menghapus gambar pion pada posisi sebelumnya

                changeTurn()
            }

        }
    }


    fun whiteAllboard(){// function ini untuk memutihkan seluruh papan terlebih dahulu
        for (i in 0..papan.size-1){
            papan[i].setBackgroundColor(resources.getColor(R.color.white))
            if (papan[i].getTag()!="armyP1"&&papan[i].getTag()!="kingP1"&&papan[i].getTag()!="armyP2"&&papan[i].getTag()!="kingP2"){
                papan[i].setTag("empty")
            }
        }
    }

    fun changeTurn(){
        if (turn == "P1"){// kondisi jika player 1 selesai bermain
            turn = "P2"
            cardNextP2.setText(chosecard)
            if (cardP1_1.text==chosecard){
                cardP1_1.setText(cardNextP1.text)
            }
            else{
                cardP1_2.setText(cardNextP1.text)
            }
            cardNextP1.setText("")
        }
        else{// kondisi jika player 2 selesai bermain
            turn = "P1"
            cardNextP1.setText(chosecard)
            if (cardP2_1.text==chosecard){
                cardP2_1.setText(cardNextP2.text)
            }
            else{
                cardP2_2.setText(cardNextP2.text)
            }
            cardNextP2.setText("")
        }
        chosecard = ""
        pionpick = -1

    }
}