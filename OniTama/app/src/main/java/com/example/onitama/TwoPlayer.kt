package com.example.onitama

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.collections.ArrayList

class TwoPlayer : AppCompatActivity() {

    lateinit var papan:ArrayList<ImageButton>
    lateinit var papanAI:ArrayList<String>
    lateinit var allcard:ArrayList<String>
    lateinit var setupgame : GameSetup
    lateinit var kartuLayout : LinearLayout
    lateinit var cardP2_1TV : TextView
    lateinit var cardP2_2TV : TextView
    lateinit var cardP1_1TV : TextView
    lateinit var cardP1_2TV : TextView
    lateinit var cardNextP2TV : TextView
    lateinit var cardNextP1TV : TextView
    lateinit var cardP1_1: Button
    lateinit var cardP1_2: Button
    lateinit var cardNextP1: Button
    lateinit var cardP2_1: Button
    lateinit var cardP2_2: Button
    lateinit var cardNextP2: Button
    private var card1 = ""
    private var card2 = ""
    private var card3 = ""
    private var card4 = ""
    private var card5 = ""
    private var turn = "P1"
    private var chosecard = ""
    private var pionpick = -1
    private var jumpionP1 = 5
    private var jumpionP2 = 5
    private var isVSAI = false
    private var isWinner = false
    private var posvalidAI = -1
    private var posvalidAIbefore = -1
    private var cardAI = "card"
    private var bestmove = 0
    private var alphabest = Integer.MIN_VALUE
    private var betabest = Integer.MAX_VALUE
    private var difficulty = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two_player)

        //deklarasi seluruh component
        kartuLayout = findViewById(R.id.kartuLayout)
        cardP2_1TV = findViewById(R.id.cardP2_1TV)
        cardP2_2TV = findViewById(R.id.cardP2_2TV)
        cardP1_1TV = findViewById(R.id.cardP1_1TV)
        cardP1_2TV = findViewById(R.id.cardP1_2TV)
        cardNextP1TV = findViewById(R.id.cardNextP1TV)
        cardNextP2TV = findViewById(R.id.cardNextP2TV)
        cardP1_1 = findViewById(R.id.cardP1_1)
        cardP1_2 = findViewById(R.id.cardP1_2)
        cardNextP1 = findViewById(R.id.cardNextP1)
        cardP2_1 = findViewById(R.id.cardP2_1)
        cardP2_2 = findViewById(R.id.cardP2_2)
        cardNextP2 = findViewById(R.id.cardNextP2)
        papan = ArrayList<ImageButton>()
        papanAI = ArrayList<String>()
        allcard = ArrayList<String>()

        isVSAI = intent.getBooleanExtra("isAI",false)

        if (isVSAI==true){
            kartuLayout.rotation=0F
            cardP2_1.rotation=180F
            cardP2_2.rotation=180F
        }

        for (i in 1..5){//dalam loop ini akan menambahkan tiap kotak dalam papan ke arraylist
            for (j in 1..5){
                var kotakID ="img_${i}_${j}"
                val resourcesID = this.resources.getIdentifier(kotakID, "id", packageName)
                papan.add(findViewById(resourcesID))
                //di bawah ini untuk menambahkan tag ke setiap kotak yang ditempati pion
                if (i==1){
                    if (j==3){
                        papan[papan.size-1].setTag("kingP2")
                        papanAI.add("kingP2")
                    }
                    else{
                        papan[papan.size-1].setTag("armyP2")
                        papanAI.add("armyP2")
                    }
                }
                else if (i==5){
                    if (j==3){
                        papan[papan.size-1].setTag("kingP1")
                        papanAI.add("kingP1")
                    }
                    else{
                        papan[papan.size-1].setTag("armyP1")
                        papanAI.add("armyP1")
                    }
                }
                else{
                    papan[papan.size-1].setTag("empty")
                    papanAI.add("empty")
                }
            }
        }

        setupgame = GameSetup()//membuat objek game setup

        setallcard()// merandom card untuk player 1 dan 2

        cardP1_1.setOnClickListener {
            if (turn=="P1") {
                resetBtnColor()
                cardP1_1.setBackgroundColor(resources.getColor(R.color.teal_200))
                chosecard = cardP1_1TV.text.toString()
            }
        }
        cardP1_2.setOnClickListener {
            if (turn=="P1") {
                resetBtnColor()
                cardP1_2.setBackgroundColor(resources.getColor(R.color.teal_200))
                chosecard = cardP1_2TV.text.toString()
            }
        }
        cardP2_1.setOnClickListener {
            if (turn=="P2") {
                resetBtnColor()
                cardP2_1.setBackgroundColor(resources.getColor(R.color.teal_200))
                chosecard = cardP2_1TV.text.toString()
            }
        }
        cardP2_2.setOnClickListener {
            if (turn=="P2") {
                resetBtnColor()
                cardP2_2.setBackgroundColor(resources.getColor(R.color.teal_200))
                chosecard = cardP2_2TV.text.toString()
            }
        }



    }

    fun resetBtnColor(){
        cardP1_1.setBackgroundColor(resources.getColor(R.color.btndefault))
        cardP1_2.setBackgroundColor(resources.getColor(R.color.btndefault))
        cardP2_1.setBackgroundColor(resources.getColor(R.color.btndefault))
        cardP2_2.setBackgroundColor(resources.getColor(R.color.btndefault))
    }



    fun setallcard(){
        //untuk merandom card yang ada agar tidak kembar
        var ctrcard = 1
        do {
            if (ctrcard==1){
                card1 = setupgame.getNamecard(Random(System.nanoTime()).nextInt(15))
                ctrcard++
            }
            else if (ctrcard==2){
                card2 = setupgame.getNamecard(Random(System.nanoTime()).nextInt(15))
                if (card2!=card1){
                    ctrcard++
                }
            }
            else if (ctrcard==3){
                card3 = setupgame.getNamecard(Random(System.nanoTime()).nextInt(15))
                if (card3!=card2&&card3!=card1){
                    ctrcard++
                }
            }
            else if (ctrcard==4){
                card4 = setupgame.getNamecard(Random(System.nanoTime()).nextInt(15))
                if (card4!=card3&&card4!=card2&&card4!=card1){
                    ctrcard++
                }
            }
            else if (ctrcard==5){
                card5 = setupgame.getNamecard(Random(System.nanoTime()).nextInt(15))
                if (card5!=card4&&card5!=card3&&card5!=card2&&card5!=card1){
                    ctrcard++
                }
            }
        }while (ctrcard!=6)


        cardP1_1TV.setText(card1)
        cardP1_2TV.setText(card2)
        cardNextP1TV.setText(card5)
        cardP2_1TV.setText(card3)
        cardP2_2TV.setText(card4)
        cardNextP2TV.setText("")

        setPic(card1,cardP1_1)
        setPic(card2,cardP1_2)
        setPic(card5,cardNextP1)
        setPic(card3,cardP2_1)
        setPic(card4,cardP2_2)

//        cardP1_1.setText(card1)
//        cardP1_2.setText(card2)
//        cardNextP1.setText(card5)
//        cardP2_1.setText(card3)
//        cardP2_2.setText(card4)
//        cardNextP2.setText("")

        allcard.add(card1)//card player 1/human
        allcard.add(card2)
        allcard.add(card3)//card player 2/AI
        allcard.add(card4)
        allcard.add(card5)// extra card

    }

    fun setPic(namacard:String,btnTemp:Button){
        if (namacard == "TIGER") {
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tiger,0,0,0)
        }
        else if (namacard == "DRAGON") {
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dragon,0,0,0)
        }
        else if (namacard == "FROG") {
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.frog,0,0,0)
        }
        else if (namacard == "RABBIT") {
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rabbit,0,0,0)
        }
        else if (namacard == "CRAB") {
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.crab,0,0,0)
        }
        else if (namacard == "ELEPHANT") {
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.elephant,0,0,0)
        }
        else if (namacard == "GOOSE") {
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.goose,0,0,0)
        }
        else if (namacard == "ROOSTER") {
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rooster,0,0,0)
        }
        else if (namacard == "MONKEY") {
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.monkey,0,0,0)
        }
        else if (namacard == "MANTIS") {
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mantis,0,0,0)
        }
        else if (namacard == "HORSE") {
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.horse,0,0,0)
        }
        else if (namacard == "OX") {
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ox,0,0,0)
        }
        else if (namacard == "CRANE") {
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.crane,0,0,0)
        }
        else if (namacard == "BOAR") {
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.boar,0,0,0)
        }
        else if (namacard == "EEL") {
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.eel,0,0,0)
        }
        else if (namacard == "COBRA"){
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cobra,0,0,0)
        }
        else{
            btnTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.blank_background,0,0,0)
        }
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

        if (isVSAI == false) {
            //di bawah ini melakukan pengecekan
            if (turn == "P1") {
                if (chosecard != "" && (papan[ctridx].getTag()
                        .toString() == "kingP1" || papan[ctridx].getTag().toString() == "armyP1")
                ) {
                    var idxlangkah =
                        setupgame.getCard(chosecard)// mendapatkan array dari card yang diklik
                    pionpick = ctridx//menyimpan index nilai pion yang dipilih
                    whiteAllboard()// memutihkan seluruh papan
                    resetBtnColor()
                    var posvalid = ctridx
                    for (i in 0..idxlangkah.size - 1) {
                        posvalid = ctridx
                        //karena papan menggunakan arraylist dan card yang dibuat dalam bentuk array maka rumus yang digunakan yaitu
                        //idxlangkah yang x ditambah dengan posisi pion yang diklik
                        //idxlangkah yang y * 5 dan dikurangi posisi pion yang diklik, dalam kasus ini p1 arah jalannya ke atas maka harus dikurangi

//                    Log.i("nilaibandingpos1", Math.ceil((posvalid/5).toDouble()).toString())
                        var banding1 =
                            Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sebelum ditambah kordinat langkah valid
                        posvalid += (idxlangkah[i][0])//untuk menghitung kordinat x
//                    Log.i("nilaibandingpos2", Math.ceil((posvalid/5).toDouble()).toString())
                        var banding2 =
                            Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sesudah ditambah kordinat x langkah valid
                        posvalid -= (5 * idxlangkah[i][1])// untuk menghitung kordinat y
//                    Log.i("kor x", Arrays.deepToString(arrayOf(idxlangkah[i][0])) )
//                    Log.i("kor y", Arrays.deepToString(arrayOf(idxlangkah[i][1])) )

                        try {
                            if (papan[posvalid].getTag() != "armyP1" && papan[posvalid].getTag() != "kingP1" && banding1 == banding2) {// melakukan pengecekan apakah langkah yang divalidasi merupakan tempat pin kelompok P1
                                papan[posvalid].setBackgroundColor(resources.getColor(R.color.valid_papan))
                                if (papan[posvalid].getTag() != "armyP2" && papan[posvalid].getTag() != "kingP2") {
                                    papan[posvalid].setTag("valid")
                                } else if (papan[posvalid].getTag() == "armyP2") {
                                    papan[posvalid].setTag("armyP2v")
                                } else if (papan[posvalid].getTag() == "kingP2") {
                                    papan[posvalid].setTag("kingP2v")
                                }
                            }
                        } catch (e: Exception) {

                        }
                    }

                } else if (chosecard != "" && (papan[ctridx].getTag()
                        .toString() == "valid" || papan[ctridx].getTag()
                        .toString() == "armyP2v" || papan[ctridx].getTag().toString() == "kingP2v")
                ) {// kondisi jika klik pada kotak yang sudah valid untuk melangkah
                    if (papan[ctridx].getTag().toString() == "armyP2v") {
                        jumpionP2--
                    } else if (papan[ctridx].getTag().toString() == "kingP2v" || ctridx == 2) {
                        jumpionP2 = 0
                    }
                    if (papan[pionpick].getTag() == "kingP1") {
                        papan[ctridx].setImageDrawable(resources.getDrawable(R.drawable.blue_king))//merubah posisi pion pada kotak yang valid
                        papan[ctridx].setTag(papan[pionpick].getTag())//set tag posisi pion baru dengan tag yang sebelumnya di posisi lama
                    }
                    else{
                        papan[ctridx].setImageDrawable(resources.getDrawable(R.drawable.blue_pawn))//merubah posisi pion pada kotak yang valid
                        papan[ctridx].setTag(papan[pionpick].getTag())//set tag posisi pion baru dengan tag yang sebelumnya di posisi lama
                    }

                    whiteAllboard()// memutihkan semua papan
                    resetBtnColor()
                    papan[pionpick].setTag("empty")// mengubah tag posisi pion yang sebelumnya ke empty
                    papan[pionpick].setImageDrawable(resources.getDrawable(R.drawable.blank_background))// menghapus gambar pion pada posisi sebelumnya

                    changeTurn()

                    checkWin()
                }
            } else if (turn == "P2") {
                if (chosecard != "" && (papan[ctridx].getTag()
                        .toString() == "kingP2" || papan[ctridx].getTag().toString() == "armyP2")
                ) {
                    var idxlangkah =
                        setupgame.getCard(chosecard)// mendapatkan array dari card yang diklik
                    pionpick = ctridx//menyimpan index nilai pion yang dipilih
                    whiteAllboard()// memutihkan seluruh papan
                    resetBtnColor()
                    var posvalid = ctridx
                    for (i in 0..idxlangkah.size - 1) {
                        posvalid = ctridx
                        //karena papan menggunakan arraylist dan card yang dibuat dalam bentuk array maka rumus yang digunakan yaitu
                        //idxlangkah yang x dikurangi dengan posisi pion yang diklik
                        //idxlangkah yang y * 5 dan ditambah posisi pion yang diklik, dalam kasus ini p2 arah jalannya ke bawah maka harus ditambah
                        var banding1 =
                            Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sebelum ditambah kordinat langkah valid
                        posvalid -= (idxlangkah[i][0])//untuk menghitung kordinat x
                        var banding2 =
                            Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sesudah ditambah kordinat x langkah valid
                        posvalid += (5 * idxlangkah[i][1])// untuk menghitung kordinat y
                        try {
                            if (papan[posvalid].getTag() != "armyP2" && papan[posvalid].getTag() != "kingP2" && banding1 == banding2) {// melakukan pengecekan apakah langkah yang divalidasi merupakan tempat pin kelompok P1
                                papan[posvalid].setBackgroundColor(resources.getColor(R.color.valid_papan))
                                if (papan[posvalid].getTag() != "armyP1" && papan[posvalid].getTag() != "kingP1") {
                                    papan[posvalid].setTag("valid")
                                } else if (papan[posvalid].getTag() == "armyP1") {
                                    papan[posvalid].setTag("armyP1v")
                                } else if (papan[posvalid].getTag() == "kingP1") {
                                    papan[posvalid].setTag("kingP1v")
                                }
                            }
                        } catch (e: Exception) {

                        }
                    }

                } else if (chosecard != "" && (papan[ctridx].getTag()
                        .toString() == "valid" || papan[ctridx].getTag()
                        .toString() == "armyP1v" || papan[ctridx].getTag().toString() == "kingP1v")
                ) {// kondisi jika klik pada kotak yang sudah valid untuk melangkah
                    //untuk mengecek apakah langkah dari pion ini menuju pion yang akan dikalahkan
                    if (papan[ctridx].getTag().toString() == "armyP1v") {
                        jumpionP1--
                    } else if (papan[ctridx].getTag().toString() == "kingP1v" || ctridx == 22) {
                        jumpionP1 = 0
                    }
                    if (papan[pionpick].getTag() == "kingP2") {
                        papan[ctridx].setImageDrawable(resources.getDrawable(R.drawable.red_king))//merubah posisi pion pada kotak yang valid
                        papan[ctridx].setTag(papan[pionpick].getTag())//set tag posisi pion baru dengan tag yang sebelumnya di posisi lama
                    }
                    else{
                        papan[ctridx].setImageDrawable(resources.getDrawable(R.drawable.red_pawn))//merubah posisi pion pada kotak yang valid
                        papan[ctridx].setTag(papan[pionpick].getTag())//set tag posisi pion baru dengan tag yang sebelumnya di posisi lama
                    }

                    whiteAllboard()// memutihkan semua papan
                    resetBtnColor()
                    papan[pionpick].setTag("empty")// mengubah tag posisi pion yang sebelumnya ke empty
                    papan[pionpick].setImageDrawable(resources.getDrawable(R.drawable.blank_background))// menghapus gambar pion pada posisi sebelumnya

                    changeTurn()

                    checkWin()
                }

            }
        }
        else{
            //di bawah ini melakukan pengecekan
            Log.i("papan klik", papan[ctridx].getTag().toString())
            if (turn == "P1") {
                if (chosecard != "" && (papan[ctridx].getTag()
                        .toString() == "kingP1" || papan[ctridx].getTag().toString() == "armyP1")
                ) {
                    var idxlangkah =
                        setupgame.getCard(chosecard)// mendapatkan array dari card yang diklik
                    pionpick = ctridx//menyimpan index nilai pion yang dipilih
                    whiteAllboard()// memutihkan seluruh papan
                    resetBtnColor()
                    var posvalid = ctridx
                    for (i in 0..idxlangkah.size - 1) {
                        posvalid = ctridx
                        //karena papan menggunakan arraylist dan card yang dibuat dalam bentuk array maka rumus yang digunakan yaitu
                        //idxlangkah yang x ditambah dengan posisi pion yang diklik
                        //idxlangkah yang y * 5 dan dikurangi posisi pion yang diklik, dalam kasus ini p1 arah jalannya ke atas maka harus dikurangi

//                    Log.i("nilaibandingpos1", Math.ceil((posvalid/5).toDouble()).toString())
                        var banding1 =
                            Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sebelum ditambah kordinat langkah valid
                        posvalid += (idxlangkah[i][0])//untuk menghitung kordinat x
//                    Log.i("nilaibandingpos2", Math.ceil((posvalid/5).toDouble()).toString())
                        var banding2 =
                            Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sesudah ditambah kordinat x langkah valid
                        posvalid -= (5 * idxlangkah[i][1])// untuk menghitung kordinat y
//                    Log.i("kor x", Arrays.deepToString(arrayOf(idxlangkah[i][0])) )
//                    Log.i("kor y", Arrays.deepToString(arrayOf(idxlangkah[i][1])) )

                        try {
                            if (papan[posvalid].getTag() != "armyP1" && papan[posvalid].getTag() != "kingP1" && banding1 == banding2) {// melakukan pengecekan apakah langkah yang divalidasi merupakan tempat pin kelompok P1
                                papan[posvalid].setBackgroundColor(resources.getColor(R.color.valid_papan))
                                if (papan[posvalid].getTag() != "armyP2" && papan[posvalid].getTag() != "kingP2") {
                                    papan[posvalid].setTag("valid")
                                } else if (papan[posvalid].getTag() == "armyP2") {
                                    papan[posvalid].setTag("armyP2v")
                                } else if (papan[posvalid].getTag() == "kingP2") {
                                    papan[posvalid].setTag("kingP2v")
                                }
                            }
                        } catch (e: Exception) {

                        }
                    }

                } else if (chosecard != "" && (papan[ctridx].getTag()
                        .toString() == "valid" || papan[ctridx].getTag()
                        .toString() == "armyP2v" || papan[ctridx].getTag().toString() == "kingP2v")
                ) {// kondisi jika klik pada kotak yang sudah valid untuk melangkah
                    if (papan[ctridx].getTag().toString() == "armyP2v") {
                        jumpionP2--
                    } else if (papan[ctridx].getTag().toString() == "kingP2v" || ctridx == 2) {
                        jumpionP2 = 0
                    }

                    if (papan[pionpick].getTag() == "kingP1") {
                        papan[ctridx].setImageDrawable(resources.getDrawable(R.drawable.blue_king))//merubah posisi pion pada kotak yang valid
                        papan[ctridx].setTag(papan[pionpick].getTag())//set tag posisi pion baru dengan tag yang sebelumnya di posisi lama
                        papanAI[ctridx]=papan[pionpick].getTag().toString()
                    }
                    else{
                        papan[ctridx].setImageDrawable(resources.getDrawable(R.drawable.blue_pawn))//merubah posisi pion pada kotak yang valid
                        papan[ctridx].setTag(papan[pionpick].getTag())//set tag posisi pion baru dengan tag yang sebelumnya di posisi lama
                        papanAI[ctridx]=papan[pionpick].getTag().toString()
                    }

                    whiteAllboard()// memutihkan semua papan
                    resetBtnColor()
                    papan[pionpick].setTag("empty")// mengubah tag posisi pion yang sebelumnya ke empty
                    papanAI[pionpick]="empty"
                    papan[pionpick].setImageDrawable(resources.getDrawable(R.drawable.blank_background))// menghapus gambar pion pada posisi sebelumnya


                    checkWin()

                    changeTurnAI()
                    var copypapan = ArrayList(papanAI)
                    var copycard = ArrayList(allcard)
//                    Log.i("allcard",allcard.toString())
//                    Log.i("allcard",copypapan.toString())
//
                    val alpha = arrayOf(Integer.MIN_VALUE)
                    val beta = arrayOf(Integer.MAX_VALUE)

//                    alphabeta(copypapan,copycard,3,true,alpha,beta,-1)
//
                    if (isWinner==false){
                        searchBest(copypapan,copycard,difficulty,true,-1,"papan",-1,-1,"card")
                        alphabest=Integer.MIN_VALUE
                        bestmove=0

                        var tagbefore = papan[posvalidAIbefore].getTag()
                        papan[posvalidAIbefore].setImageDrawable(resources.getDrawable(R.drawable.blank_background))
                        papan[posvalidAIbefore].setTag("empty")
                        papanAI[posvalidAIbefore]=("empty")

                        if (tagbefore == "kingP2") {
                            papan[posvalidAI].setImageDrawable(resources.getDrawable(R.drawable.red_king))
                        }
                        else{
                            papan[posvalidAI].setImageDrawable(resources.getDrawable(R.drawable.red_pawn))
                        }
                        papan[posvalidAI].setTag("$tagbefore")
                        papanAI[posvalidAI]=("$tagbefore")



                        cekWinAI(papanAI)
                        changeTurnAI()
                    }

                    Log.i("posvalidbefore",posvalidAIbefore.toString())
                    Log.i("posvalid",posvalidAI.toString())
                    Log.i("cardvalid",cardAI.toString())
                    Log.i("turn", turn)

//                    test(5,5)
                }
            }

        }
    }

//    fun test(tambah:Int, depth: Int):Int{
//
//        if (depth==2){
//            return 5
//        }
//        for (i in 0..4){
//            if (i==2){
//                return 2
//            }
//            Log.i("testsaja","$i")
//            test(5,depth-1)
//        }
//        return 3
//    }


    fun searchBest(papancopy:ArrayList<String>, allcard:ArrayList<String>, depth: Int, isMaximaize: Boolean, tempPos: Int, namePos:String, before:Int, after:Int, namecard:String): Int{



        if (depth<=0){// jika telah mencapai batas kedalaman pencaharian
//
            var ctrarmyP1=0
            var ctrarmyP2=0
            var ctrkingP1=0
            var ctrkingP2=0
            var P1inP2=0
            var P2inP1=0
            if ((tempPos==22)&&isMaximaize==false){// kondisi jika ada jalan menuju base
                P2inP1 = 100
            }
            else if ((tempPos==2)&&isMaximaize==true){
                P1inP2 = -100
            }
            for (i in 0..papancopy.size-1){// looping untuk menghitung seluruh jumlah pion yang ada
                if (papancopy[i]=="armyP1"){
                    ctrarmyP1++
                }
                else if (papancopy[i]=="armyP2"){
                    ctrarmyP2++
                }
                else if (papancopy[i]=="kingP1"){
                    ctrkingP1++
                }
                else if (papancopy[i]=="kingP2"){
                    ctrkingP2++
                }
            }
            return (((ctrarmyP1*10)+(ctrkingP1*100))+P1inP2)-(((ctrarmyP2*-10)+(ctrkingP2*-100))+P2inP1)// mereturn nilai sbe
        }
        if (isMaximaize) {
            var v = Integer.MIN_VALUE
            for (i in 2..3) {// ini untuk ngeloop card
                var idxlangkah = setupgame.getCard(allcard[i])
//                Log.i("ini card ke", "$i dari P2 dengan alpha ${tempAlpha}")
                for (j in 0..papancopy.size - 1) {// untuk mengecek seluruh papancopy
                    if (papancopy[j] == "empty" || papancopy[j] == "armyP1" || papancopy[j] == "kingP1") {// di sini jika ketemu kordinat yang tidak pasti langsung dicontinue
                        continue
                    }
//                    Log.i("ini mengecek pos ke","$j dengan depth $depth")
                    for (k in 0..idxlangkah.size - 1) {
                        var posvalid = j
                        var banding1 =
                            Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sebelum ditambah kordinat langkah valid
                        posvalid -= (idxlangkah[k][0])//untuk menghitung kordinat x
                        var banding2 =
                            Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sesudah ditambah kordinat x langkah valid
                        posvalid += (5 * idxlangkah[k][1])// untuk menghitung kordinat y

                        try {
                            if (papancopy[posvalid] != "armyP2" && papancopy[posvalid] != "kingP2" && banding1 == banding2) {// melakukan pengecekan apakah langkah yang divalidasi merupakan tempat pin kelompok P1

//
                                //kondisi pruning
                                if (papancopy[posvalid] == "kingP1" || posvalid == 22) {
//
                                    if (depth==difficulty){
                                        posvalidAIbefore = j
                                        posvalidAI = posvalid
                                        cardAI = allcard[i]
                                        bestmove=1
                                    }
                                    return 400
                                }
                                else{

                                    // di bawah ini membuat kopian papan dan card
                                    var pcopy: ArrayList<String> = ArrayList<String>(papancopy)
                                    var ccopy: ArrayList<String> = ArrayList<String>(allcard)
                                    var temp = pcopy[j]
                                    pcopy[posvalid]=temp
                                    pcopy[j]="empty"
//                                Log.i("papan rec",pcopy.toString())

                                    var tempnamecard = ccopy[i]
                                    ccopy[i] = ccopy[4]
                                    ccopy[4] = tempnamecard

                                    // membuat papan yang paling valid untuk digunakan, ini akan dipakai kordinatnya saat kondisi pruning
                                    var pasbefore = -1
                                    var pasafter = -1
                                    var pasnamecard = "card"
                                    if (depth==difficulty){
                                        pasbefore=j
                                        pasafter=posvalid
                                        pasnamecard=allcard[i]
                                    }
                                    else{
                                        pasbefore=before
                                        pasafter=after
                                        pasnamecard=namecard
                                    }
                                    // melakukan recursive
                                    val retVal = searchBest(pcopy,ccopy,depth-1,false,posvalid,papancopy[posvalid],pasbefore,pasafter,pasnamecard)
//


                                    if (v<retVal){// kondisi mendapatkan nilai max
                                        v = retVal // nilai v diganti dengan nilai max
//                                    Log.i("retval","$v")
                                        if (depth==difficulty&&bestmove==0) {// jika sudah mendapatkan seluruh nilai akan mencari langkah dengan max yang paling tinggi
//
                                            posvalidAIbefore = j
                                            posvalidAI = posvalid
                                            cardAI = allcard[i]
                                        }
                                    }
                                }
                            } else {
                                continue
                            }
                        } catch (e: Exception) {
                            continue
                        }
                    }
                }
//                var tempnamecard = allcard[i]
//                allcard[i] = allcard[4]
//                allcard[4] = tempnamecard
            }
            return v
        }
        else{
            var v = Integer.MAX_VALUE
            for (i in 0..1){// ini untuk ngeloop card
                var idxlangkah = setupgame.getCard(allcard[i])

//                Log.i("ini card ke", "$i dari P1 dengan beta ${tempBeta}")
//                Log.i("ini card ke", "$i dari P1")
                for(j in 0..papancopy.size-1){// untuk mengecek seluruh papancopy
                    if (papancopy[j]=="empty"||papancopy[j]=="armyP2"||papancopy[j]=="kingP2"){// di sini jika ketemu kordinat yang tidak pasti langsung dicontinue
                        continue
                    }
                    for(k in 0..idxlangkah.size-1){
                        var posvalid = j
                        var banding1 =
                            Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sebelum ditambah kordinat langkah valid
                        posvalid += (idxlangkah[k][0])//untuk menghitung kordinat x
                        var banding2 =
                            Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sesudah ditambah kordinat x langkah valid
                        posvalid -= (5 * idxlangkah[k][1])// untuk menghitung kordinat y

                        try {
                            if (papancopy[posvalid] != "armyP1" && papancopy[posvalid] != "kingP1" && banding1 == banding2) {// melakukan pengecekan apakah langkah yang divalidasi merupakan tempat pin kelompok P1
//
                                if (papancopy[posvalid] == "kingP2" || posvalid == 2) {
                                    return -400
                                }
                                else{
                                    var pcopy: ArrayList<String> = ArrayList<String>(papancopy)
                                    var ccopy: ArrayList<String> = ArrayList<String>(allcard)
                                    var temp = pcopy[j]
                                    pcopy[posvalid]=temp
                                    pcopy[j]="empty"

                                    var tempnamecard = ccopy[i]
                                    ccopy[i] = ccopy[4]
                                    ccopy[4] = tempnamecard
                                    val retVal = searchBest(pcopy,ccopy,depth-1,true,posvalid,papancopy[posvalid],before,after,namecard)

                                    if (v>retVal){// kondisi mendapatkan nilai min
                                        v = retVal // nilai v diganti dengan nilai min
//
                                    }
                                }
                            }
                            else{
                                continue
                            }
                        } catch (e: Exception) {
                            continue
                        }
                    }
                }
            }
            return v
        }
    }







    fun whiteAllboard(){// function ini untuk memutihkan seluruh papan terlebih dahulu
        for (i in 0..papan.size-1){
            papan[i].setBackgroundColor(resources.getColor(R.color.white))
            if (papan[i].getTag()=="armyP1v"){
                papan[i].setTag("armyP1")
            }
            else if (papan[i].getTag()=="kingP1v"){
                papan[i].setTag("kingP1")
            }
            else if (papan[i].getTag()=="armyP2v"){
                papan[i].setTag("armyP2")
            }
            else if (papan[i].getTag()=="kingP2v"){
                papan[i].setTag("kingP2")
            }
            else if (papan[i].getTag()=="valid"){
                papan[i].setTag("empty")
            }
        }
    }

    fun changeTurn(){
        if (turn == "P1"){// kondisi jika player 1 selesai bermain
            turn = "P2"
            setPic(chosecard,cardNextP2)
            cardNextP2TV.setText(chosecard)
//            cardNextP2.setText(chosecard)
            if (cardP1_1TV.text==chosecard){
                setPic(cardNextP1TV.text.toString(),cardP1_1)
                cardP1_1TV.setText(cardNextP1TV.text)
//                cardP1_1.setText(cardNextP1.text)
            }
            else{
                setPic(cardNextP1TV.text.toString(),cardP1_2)
                cardP1_2TV.setText(cardNextP1TV.text)
//                cardP1_2.setText(cardNextP1.text)
            }
            cardNextP1TV.setText("")
            setPic(cardNextP1TV.text.toString(),cardNextP1)
        }
        else{// kondisi jika player 2 selesai bermain
            turn = "P1"
            setPic(chosecard,cardNextP1)
            cardNextP1TV.setText(chosecard)
//            cardNextP1.setText(chosecard)
            if (cardP2_1TV.text==chosecard){
                setPic(cardNextP2TV.text.toString(),cardP2_1)
                cardP2_1TV.setText(cardNextP2TV.text)
//                cardP2_1.setText(cardNextP2.text)
            }
            else{
                setPic(cardNextP2TV.text.toString(),cardP2_2)
                cardP2_2TV.setText(cardNextP2TV.text)
//                cardP2_2.setText(cardNextP2.text)
            }
            setPic(cardNextP2TV.text.toString(),cardNextP2)
            cardNextP2TV.setText("")
        }
        chosecard = ""
        pionpick = -1

    }

    fun changeTurnAI(){
        if (turn == "P1"){// kondisi jika player 1 selesai bermain
            if (cardP1_1TV.text==chosecard){
                setPic(cardNextP1TV.text.toString(),cardP1_1)
                cardP1_1TV.setText(cardNextP1TV.text)
                allcard.set(0,cardNextP1TV.text.toString())
            }
            else{
                setPic(cardNextP1TV.text.toString(),cardP1_2)
                cardP1_2TV.setText(cardNextP1TV.text)
                allcard.set(1,cardNextP1TV.text.toString())
            }
            cardNextP1TV.setText("")
            setPic(cardNextP1TV.text.toString(),cardNextP1)

            allcard.set(4,chosecard)
            cardNextP2TV.setText(allcard[4])
            setPic(cardNextP2TV.text.toString(),cardNextP2)
            turn = "AI"
        }
        else{// kondisi jika player AI selesai bermain
            if (cardP2_1TV.text==cardAI){
                setPic(cardNextP2TV.text.toString(),cardP2_1)
                cardP2_1TV.setText(cardNextP2TV.text)
                allcard.set(2,cardNextP2TV.text.toString())
            }
            else{
                setPic(cardNextP2TV.text.toString(),cardP2_2)
                cardP2_2TV.setText(cardNextP2TV.text)
                allcard.set(3,cardNextP2TV.text.toString())
            }
            cardNextP2TV.setText("")
            setPic(cardNextP2TV.text.toString(),cardNextP2)

            allcard.set(4,cardAI)
            cardNextP1TV.setText(allcard[4])
            setPic(cardNextP1TV.text.toString(),cardNextP1)
            turn = "P1"
        }
        chosecard = ""
        pionpick = -1

    }


    fun checkWin(){
        if (jumpionP1<=0){
            var intent = Intent(this,EndGame::class.java).apply {
                putExtra("pemenang", "Player2")
            }
            startActivity(intent)
            finish()
        }
        else if (jumpionP2<=0){
            isWinner=true// ini dipakai untuk vsAI
            var intent = Intent(this,EndGame::class.java).apply {
                putExtra("pemenang", "Player1")
            }
            startActivity(intent)
            finish()
        }
    }

    fun cekWinAI(papanAI:ArrayList<String>){
        if (papanAI[22]=="kingP2"||papanAI[22]=="armyP2"){
            var intent = Intent(this,EndGame::class.java).apply {
                putExtra("pemenang", "AI")
            }
//            Thread.sleep(3_000)
            startActivity(intent)
            finish()
        }
        else{
            var ctrKing = 0
            for (i in (papanAI.size-1) downTo 0){
                if (papanAI[i]=="kingP1"){
                    ctrKing++
                    break
                }
            }
            if (ctrKing==0){
                var intent = Intent(this,EndGame::class.java).apply {
                    putExtra("pemenang", "AI")
                }
//                Thread.sleep(3_000)
                startActivity(intent)
                finish()
            }
        }
    }
}