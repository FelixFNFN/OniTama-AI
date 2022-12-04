package com.example.onitama

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TwoPlayer : AppCompatActivity() {

    lateinit var papan:ArrayList<ImageButton>
    lateinit var papanAI:ArrayList<String>
    lateinit var allcard:ArrayList<String>
    lateinit var setupgame : GameSetup
    lateinit var textView6 : TextView
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
    private lateinit var card1arr : Array<IntArray>
    private lateinit var card2arr : Array<IntArray>
    private lateinit var card3arr : Array<IntArray>
    private lateinit var card4arr : Array<IntArray>
    private lateinit var card5arr : Array<IntArray>
    private var turn = "P1"
    private var chosecard = ""
    private var pionpick = -1
    private var jumpionP1 = 5
    private var jumpionP2 = 5
    private var isVSAI = false
    private var posvalidAI = -1
    private var posvalidAIbefore = -1
    private var cardAI = "card"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two_player)

        //deklarasi seluruh component
        textView6 = findViewById(R.id.textView6)
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
                chosecard = cardP1_1.text.toString()
            }
        }
        cardP1_2.setOnClickListener {
            if (turn=="P1") {
                chosecard = cardP1_2.text.toString()
            }
        }
        cardP2_1.setOnClickListener {
            if (turn=="P2") {
                chosecard = cardP2_1.text.toString()
            }
        }
        cardP2_2.setOnClickListener {
            if (turn=="P2") {
                chosecard = cardP2_2.text.toString()
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

        allcard.add(card1)//card player 1/human
        allcard.add(card2)
        allcard.add(card3)//card player 2/AI
        allcard.add(card4)
        allcard.add(card5)// extra card

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
                                    papan[posvalid].setTag("army21v")
                                } else if (papan[posvalid].getTag() == "kingP2") {
                                    papan[posvalid].setTag("kingP2v")
                                }
                            }
                        } catch (e: Exception) {

                        }
                    }

                } else if (chosecard != "" && (papan[ctridx].getTag()
                        .toString() == "valid" || papan[ctridx].getTag()
                        .toString() == "armyP1v" || papan[ctridx].getTag().toString() == "kingP1v")
                ) {// kondisi jika klik pada kotak yang sudah valid untuk melangkah
                    if (papan[ctridx].getTag().toString() == "armyP2v") {
                        jumpionP2--
                    } else if (papan[ctridx].getTag().toString() == "kingP2v" || ctridx == 2) {
                        jumpionP2 = 0
                    }
                    papan[ctridx].setImageDrawable(resources.getDrawable(R.drawable.blue_pawn))//merubah posisi pion pada kotak yang valid
                    papan[ctridx].setTag(papan[pionpick].getTag())//set tag posisi pion baru dengan tag yang sebelumnya di posisi lama

                    whiteAllboard()// memutihkan semua papan
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

                    papan[ctridx].setImageDrawable(resources.getDrawable(R.drawable.red_pawn))//merubah posisi pion pada kotak yang valid
                    papan[ctridx].setTag(papan[pionpick].getTag())//set tag posisi pion baru dengan tag yang sebelumnya di posisi lama

                    whiteAllboard()// memutihkan semua papan
                    papan[pionpick].setTag("empty")// mengubah tag posisi pion yang sebelumnya ke empty
                    papan[pionpick].setImageDrawable(resources.getDrawable(R.drawable.blank_background))// menghapus gambar pion pada posisi sebelumnya

                    changeTurn()

                    checkWin()
                }

            }
        }
        else{
            //di bawah ini melakukan pengecekan
            if (turn == "P1") {
                Log.i("isklik", "ini klik ${papan[ctridx].getTag()}")
//                for (kotak in papan){
//                    Log.i("papantag",kotak.getTag().toString())
//                }
                if (chosecard != "" && (papan[ctridx].getTag()
                        .toString() == "kingP1" || papan[ctridx].getTag().toString() == "armyP1")
                ) {
                    var idxlangkah =
                        setupgame.getCard(chosecard)// mendapatkan array dari card yang diklik
                    pionpick = ctridx//menyimpan index nilai pion yang dipilih
                    whiteAllboard()// memutihkan seluruh papan
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
                        Log.i("posvalid",posvalid.toString())

                        try {
                            if (papan[posvalid].getTag() != "armyP1" && papan[posvalid].getTag() != "kingP1" && banding1 == banding2) {// melakukan pengecekan apakah langkah yang divalidasi merupakan tempat pin kelompok P1
                                papan[posvalid].setBackgroundColor(resources.getColor(R.color.valid_papan))
                                if (papan[posvalid].getTag() != "armyP2" && papan[posvalid].getTag() != "kingP2") {
                                    papan[posvalid].setTag("valid")
                                } else if (papan[posvalid].getTag() == "armyP2") {
                                    papan[posvalid].setTag("army21v")
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
                    papan[ctridx].setImageDrawable(resources.getDrawable(R.drawable.blue_pawn))//merubah posisi pion pada kotak yang valid
                    papan[ctridx].setTag(papan[pionpick].getTag())//set tag posisi pion baru dengan tag yang sebelumnya di posisi lama
                    papanAI[ctridx]=papan[pionpick].getTag().toString()

                    whiteAllboard()// memutihkan semua papan
                    papan[pionpick].setTag("empty")// mengubah tag posisi pion yang sebelumnya ke empty
                    papanAI[pionpick]="empty"
                    papan[pionpick].setImageDrawable(resources.getDrawable(R.drawable.blank_background))// menghapus gambar pion pada posisi sebelumnya


//                    changeTurnAI()
//                    var getpapan = alphabetaPruning(papan,allcard,0,true,jumpionP1,jumpionP2,0,0)
//
//                    for (i in 0..getpapan.size-1) {
//                        Log.i("papan", getpapan[i].getTag().toString())
//                    }
//

//                    var papancopy : MutableList<ImageButton>
//                    papancopy = ArrayList()
//                    papancopy.addAll(papan)
//                    var listcopy = ArrayList(papan)
//                    var copy2 = papan.clone() as ArrayList<ImageButton>
//                    val mutableList = papan.toMutableList()
//                    var leadList1: ArrayList<ImageButton> = ArrayList()
//                    leadList1.addAll(papan.filterNotNull())

//                    val copy1 = papan

//                    val papancopy : ArrayList<ImageButton> = ArrayList()
//
//                    for (kotak in papan){
//                        papancopy.add(kotak)
//                    }
//
//

//                    val papancopy: ArrayList<ImageButton>
//                    papancopy = ArrayList()
//                    papancopy.addAll()
//                    val papancopy = papan.clone() as ArrayList<ImageButton>
//                    val papancopy : ArrayList<ImageButton>
//                    papancopy = ArrayList()
//                    papancopy.addAll(papan.clone() as ArrayList<ImageButton>)

//                    val temp :MutableList<ImageButton>
//                    temp = mutableListOf<ImageButton>()
//                    temp.addAll(papan)
////                    var papancopy : MutableList<ImageButton>
////                    papancopy = mutableListOf()
////                    papancopy.addAll(papan)
//                    val papancopy = ArrayList(temp)


                    changeTurnAI()
                    Log.i("posvalid",allcard.toString())

                    alphabeta(papanAI,allcard,3,true,Integer.MIN_VALUE,Integer.MAX_VALUE)


                    // di bawah ini untuk memindahkan pion AI
                    var tagbefore = papan[posvalidAIbefore].getTag()
                    papan[posvalidAIbefore].setImageDrawable(resources.getDrawable(R.drawable.blank_background))
                    papan[posvalidAIbefore].setTag("empty")
                    papanAI[posvalidAIbefore]=("empty")

                    papan[posvalidAI].setImageDrawable(resources.getDrawable(R.drawable.red_pawn))
                    papan[posvalidAI].setTag("$tagbefore")
                    papanAI[posvalidAI]=("$tagbefore")

                    changeTurnAI()
                    Log.i("posvalid",allcard.toString())

                    Log.i("posvalid",posvalidAI.toString())
                    Log.i("cardvalid",cardAI.toString())
                    Log.i("turn", turn)

                    checkWin()
                }
            }

        }
    }


    fun alphabeta(papancopy:ArrayList<String>, allcard:ArrayList<String>, depth:Int, isMaximaize:Boolean, alpha:Int, beta:Int) : Int{
//        if (papancopy)// di sini harus mengecek apakah ai bisa menang
//        if(depth<=0){
//            int curcoun
//            return
//        }
        if (isMaximaize){
            //maximize untuk p2
            var v = Integer.MIN_VALUE
            for (i in 2..3){// ini untuk ngeloop card
                var idxlangkah = setupgame.getCard(allcard[i])
                for(j in 0..papancopy.size-1){// untuk mengecek seluruh papancopy
                    if (papancopy[j]=="empty"||papancopy[j]=="armyP1"||papancopy[j]=="kingP1"){// di sini jika ketemu kordinat yang tidak pasti langsung dicontinue
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
                            if (papancopy[posvalid] != "armyP2" && papancopy[posvalid] != "kingP2" && banding1 == banding2) {// melakukan pengecekan apakah langkah yang divalidasi merupakan tempat pin kelompok P1
//                                    papancopy[posvalid].setBackgroundColor(resources.getColor(R.color.valid_papan))
                                // harus membuat bentuk papancopy setelah pion dipindahkan dan dimasukkan ke dalam recursive
                                // di bawah ini belum tentu jalan sempurna
                                var kordinatsekarang = posvalid
                                var cardsekarang = allcard[i]
                                var tagbefore = papancopy[j]
                                papancopy[j]="empty"
                                if (depth<=0) {
                                    if (papancopy[posvalid]=="kingP1"||posvalid == 22){
                                        return 100
                                    }
                                    else if(papancopy[posvalid]=="armyP1"){
                                        return 50
                                    }
                                    else {
                                        return 0
                                    }
                                }
                                // di bawah ini untuk ngeswipe posisi card dari dalam arraylist
                                var tempnamecard = allcard[i]
                                allcard[i] = allcard[4]
                                allcard[4] = tempnamecard
                                // di bawah ini untuk mengubah tag posisi pion baru dengan nama tag posisi sebelumnya
                                papancopy[posvalid]="$tagbefore"


                                // menampung nilai return dari recursive
                                var retVal = alphabeta(papancopy, allcard,depth-1,false,alpha,beta)

                                if (v<retVal){// kondisi mendapatkan nilai max
                                    v = retVal // nilai v diganti dengan nilai max
                                    if(alpha<v){// jika alpha kurang dari nilai v (nilai alpha itu adalah min tak hingga)
                                        if (depth == 3){// jika sudah di kedalaman 3 maka dibuat kordinat yang paling baik untuk dituju
                                            posvalidAIbefore = j
                                            posvalidAI = kordinatsekarang
                                            cardAI = cardsekarang
                                        }
                                        if (beta<=v){
                                            return v
                                        }
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
        else{
            //minimize untuk nilai p1
            var v = Integer.MAX_VALUE
            for (i in 0..1){// ini untuk ngeloop card
                var idxlangkah = setupgame.getCard(allcard[i])
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
//                                    papancopy[posvalid].setBackgroundColor(resources.getColor(R.color.valid_papan))
                                // harus membuat bentuk papancopy setelah pion dipindahkan dan dimasukkan ke dalam recursive
                                var tagbefore = papancopy[j]
                                papancopy[j]="empty"
                                if (depth<=0) {
                                    if (papancopy[posvalid]=="kingP2"||posvalid == 2){
                                        return -100
                                    }
                                    else if(papancopy[posvalid]=="armyP2"){
                                        return -50
                                    }
                                    else {
                                        return 0
                                    }
                                }
                                // di bawah ini untuk ngeswipe posisi card dari dalam arraylist
                                var tempnamecard = allcard[i]
                                allcard[i] = allcard[4]
                                allcard[4] = tempnamecard
                                // di bawah ini untuk mengubah tag posisi pion baru dengan nama tag posisi sebelumnya
                                papancopy[posvalid]="$tagbefore"

                                // menampung nilai return dari recursive
                                var retVal = alphabeta(papancopy, allcard,depth-1,false,alpha,beta)

                                if (v<retVal){// kondisi mendapatkan nilai max
                                    v = retVal // nilai v diganti dengan nilai max
                                    if(alpha<v){// jika alpha kurang dari nilai v (nilai alpha itu adalah min tak hingga)
                                        if (v<=alpha){
                                            return v
                                        }
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

    fun changeTurnAI(){
        if (turn == "P1"){// kondisi jika player 1 selesai bermain
            if (cardP1_1.text==chosecard){
                cardP1_1.setText(cardNextP1.text)
                allcard.set(0,cardNextP1.text.toString())
            }
            else{
                cardP1_2.setText(cardNextP1.text)
                allcard.set(1,cardNextP1.text.toString())
            }
            cardNextP1.setText("")

            allcard.set(4,chosecard)
            cardNextP2.setText(allcard[4])
            turn = "AI"
        }
        else{// kondisi jika player AI selesai bermain
            if (cardP2_1.text==cardAI){
                cardP2_1.setText(cardNextP2.text)
                allcard.set(2,cardNextP2.text.toString())
            }
            else{
                cardP2_2.setText(cardNextP2.text)
                allcard.set(3,cardNextP2.text.toString())
            }
            cardNextP2.setText("")

            allcard.set(4,cardAI)
            cardNextP1.setText(allcard[4])
            turn = "P1"
        }
        chosecard = ""
        pionpick = -1

    }

//    fun alphabetaPruning(papan:ArrayList<ImageButton>,allcard:ArrayList<String>,depth:Int,isMaximizing:Boolean, jumpionP1:Int, jumpionP2: Int,alpha:Int,beta:Int) : ArrayList<ImageButton>{
//        if (depth>maxDept){
//            return papan
//        }
//        else{
//            if (isMaximizing) {
//                var ctr = 0// untuk mengcounter apakah pion yang dicari jalannya sudah semua pion atau belum
//                for (i in 0..papan.size - 1) {
//                    if (ctr>=jumpionP2){
//                        break
//                    }
//                    var idxlangkah1 = setupgame.getCard(allcard[2])// mendapatkan array dari card yang diklik
//                    var idxlangkah2 = setupgame.getCard(allcard[3])// mendapatkan array dari card yang diklik
//                    if (papan[i].getTag()=="armyP2"||papan[i].getTag()=="kingP2"){
//                        for(j in 0..idxlangkah1.size-1) {
//                            var posvalid = i
//                            var banding1 =
//                                Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sebelum ditambah kordinat langkah valid
//                            posvalid += (idxlangkah1[j][0])//untuk menghitung kordinat x
//                            var banding2 =
//                                Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sesudah ditambah kordinat x langkah valid
//                            posvalid -= (5 * idxlangkah1[j][1])// untuk menghitung kordinat y
//
//                            try {
//                                if (papan[posvalid].getTag() != "armyP1" && papan[posvalid].getTag() != "kingP1" && banding1 == banding2) {// melakukan pengecekan apakah langkah yang divalidasi merupakan tempat pin kelompok P1
////                                    papan[posvalid].setBackgroundColor(resources.getColor(R.color.valid_papan))
//                                    // harus membuat bentuk papan setelah pion dipindahkan dan dimasukkan ke dalam recursive
//                                    var tagbefore = papan[i].getTag()
//                                    papan[i].setTag("empty")
//                                    if (papan[posvalid].getTag()=="kingP2"){
//                                        var tempnamecard = allcard[2]
//                                        allcard[2] = allcard[5]
//                                        allcard[5] = tempnamecard
//                                        return alphabetaPruning(papan,allcard,depth-1,false,0,jumpionP2,0,0)
//                                    }
//                                    else if(papan[posvalid].getTag()=="armyP2"){
//                                        var tempnamecard = allcard[2]
//                                        allcard[2] = allcard[5]
//                                        allcard[5] = tempnamecard
//                                        return alphabetaPruning(papan,allcard,depth-1,false,jumpionP1-1,jumpionP2,0,0)
//                                    }
//                                    papan[posvalid].setTag("$tagbefore")
//
//
//                                    //di sini harusnya ada recursive
//                                }
//                            } catch (e: Exception) {
//
//                            }
//                        }
//                        for(j in 0..idxlangkah2.size-1) {
//                            var posvalid = i
//                            var banding1 =
//                                Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sebelum ditambah kordinat langkah valid
//                            posvalid += (idxlangkah2[j][0])//untuk menghitung kordinat x
//                            var banding2 =
//                                Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sesudah ditambah kordinat x langkah valid
//                            posvalid -= (5 * idxlangkah2[j][1])// untuk menghitung kordinat y
//
//                            try {
//                                if (papan[posvalid].getTag() != "armyP1" && papan[posvalid].getTag() != "kingP1" && banding1 == banding2) {// melakukan pengecekan apakah langkah yang divalidasi merupakan tempat pin kelompok P1
////                                    papan[posvalid].setBackgroundColor(resources.getColor(R.color.valid_papan))
//                                    // harus membuat bentuk papan setelah pion dipindahkan dan dimasukkan ke dalam recursive
//                                    var tagbefore = papan[i].getTag()
//                                    papan[i].setTag("empty")
//                                    if (papan[posvalid].getTag()=="kingP2"){
//                                        var tempnamecard = allcard[3]
//                                        allcard[3] = allcard[5]
//                                        allcard[5] = tempnamecard
//                                        return alphabetaPruning(papan,allcard,depth-1,false,0,jumpionP2,0,0)
//                                    }
//                                    else if(papan[posvalid].getTag()=="armyP2"){
//                                        var tempnamecard = allcard[3]
//                                        allcard[3] = allcard[5]
//                                        allcard[5] = tempnamecard
//                                        return alphabetaPruning(papan,allcard,depth-1,false,jumpionP1-1,jumpionP2,0,0)
//                                    }
//                                    papan[posvalid].setTag("$tagbefore")
//
//
//                                    //di sini harusnya ada recursive
//                                }
//                            } catch (e: Exception) {
//
//                            }
//                        }
//
//                        ctr++
//                    }
//                }
//                return alphabetaPruning(papan,allcard,depth-1,false,jumpionP1,jumpionP2,0,0)
//            }
//            else{
//                var ctr = 0// untuk mengcounter apakah pion yang dicari jalannya sudah semua pion atau belum
//                for (i in 0..papan.size - 1) {
//                    if (ctr>=jumpionP1){
//                        break
//                    }
//                    var idxlangkah1 = setupgame.getCard(allcard[0])// mendapatkan array dari card yang diklik
//                    var idxlangkah2 = setupgame.getCard(allcard[1])// mendapatkan array dari card yang diklik
//                    if (papan[i].getTag()=="armyP1"||papan[i].getTag()=="kingP1"){
//                        for(j in 0..idxlangkah1.size-1) {
//                            var posvalid = i
//                            var banding1 =
//                                Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sebelum ditambah kordinat langkah valid
//                            posvalid += (idxlangkah1[j][0])//untuk menghitung kordinat x
//                            var banding2 =
//                                Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sesudah ditambah kordinat x langkah valid
//                            posvalid -= (5 * idxlangkah1[j][1])// untuk menghitung kordinat y
//
//                            try {
//                                if (papan[posvalid].getTag() != "armyP2" && papan[posvalid].getTag() != "kingP2" && banding1 == banding2) {// melakukan pengecekan apakah langkah yang divalidasi merupakan tempat pin kelompok P1
////                                    papan[posvalid].setBackgroundColor(resources.getColor(R.color.valid_papan))
//                                    // harus membuat bentuk papan setelah pion dipindahkan dan dimasukkan ke dalam recursive
//                                    var tagbefore = papan[i].getTag()
//                                    papan[i].setTag("empty")
//                                    if (papan[posvalid].getTag()=="kingP1"){
//                                        var tempnamecard = allcard[0]
//                                        allcard[0] = allcard[5]
//                                        allcard[5] = tempnamecard
//                                        return alphabetaPruning(papan,allcard,depth-1,true,jumpionP1,0,0,0)
//                                    }
//                                    else if(papan[posvalid].getTag()=="armyP1"){
//                                        var tempnamecard = allcard[0]
//                                        allcard[0] = allcard[5]
//                                        allcard[5] = tempnamecard
//                                        return alphabetaPruning(papan,allcard,depth-1,true,jumpionP1,jumpionP2-1,0,0)
//                                    }
//                                    papan[posvalid].setTag("$tagbefore")
//
//
//                                    //di sini harusnya ada recursive
//                                }
//                            } catch (e: Exception) {
//
//                            }
//                        }
//                        for(j in 0..idxlangkah2.size-1) {
//                            var posvalid = i
//                            var banding1 =
//                                Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sebelum ditambah kordinat langkah valid
//                            posvalid += (idxlangkah2[j][0])//untuk menghitung kordinat x
//                            var banding2 =
//                                Math.ceil((posvalid / 5).toDouble())//nilai pembulatan ke atas dari posisi valid sesudah ditambah kordinat x langkah valid
//                            posvalid -= (5 * idxlangkah2[j][1])// untuk menghitung kordinat y
//
//                            try {
//                                if (papan[posvalid].getTag() != "armyP2" && papan[posvalid].getTag() != "kingP2" && banding1 == banding2) {// melakukan pengecekan apakah langkah yang divalidasi merupakan tempat pin kelompok P1
////                                    papan[posvalid].setBackgroundColor(resources.getColor(R.color.valid_papan))
//                                    // harus membuat bentuk papan setelah pion dipindahkan dan dimasukkan ke dalam recursive
//                                    var tagbefore = papan[i].getTag()
//                                    papan[i].setTag("empty")
//                                    if (papan[posvalid].getTag()=="kingP1"){
//                                        var tempnamecard = allcard[1]
//                                        allcard[1] = allcard[5]
//                                        allcard[5] = tempnamecard
//                                        return alphabetaPruning(papan,allcard,depth-1,true,jumpionP1,0,0,0)
//                                    }
//                                    else if(papan[posvalid].getTag()=="armyP1"){
//                                        var tempnamecard = allcard[1]
//                                        allcard[1] = allcard[5]
//                                        allcard[5] = tempnamecard
//                                        return alphabetaPruning(papan,allcard,depth-1,true,jumpionP1,jumpionP2-1,0,0)
//                                    }
//                                    papan[posvalid].setTag("$tagbefore")
//
//
//                                    //di sini harusnya ada recursive
//                                }
//                            } catch (e: Exception) {
//
//                            }
//                        }
//
//                        ctr++
//                    }
//                }
//                return alphabetaPruning(papan,allcard,depth-1,true,jumpionP1,jumpionP2,0,0)
//            }
//        }
//    }

    fun checkWin(){
        if (jumpionP1<=0){
            var intent = Intent(this,EndGame::class.java).apply {
                putExtra("pemenang", "Player2")
            }
            startActivity(intent)
            finish()
        }
        else if (jumpionP2<=0){
            var intent = Intent(this,EndGame::class.java).apply {
                putExtra("pemenang", "Player1")
            }
            startActivity(intent)
            finish()
        }
    }
}