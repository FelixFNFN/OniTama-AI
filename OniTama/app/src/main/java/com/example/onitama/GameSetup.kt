package com.example.onitama

class GameSetup {
    private var allcard: Array<String> = arrayOf<String>("TIGER","DRAGON","FROG","RABBIT","CRAB","ELEPHANT","GOOSE","ROOSTER","MONKEY","MANTIS","HORSE","OX","CRANE","BOAR","EEL","COBRA")
    private var TIGER : Array<IntArray> = arrayOf(intArrayOf(0,2), intArrayOf(0,-1))// semua index mulai dari atas pojok kiri
    private var DRAGON : Array<IntArray> = arrayOf(intArrayOf(-2,1),intArrayOf(2,1),intArrayOf(-1,-1),intArrayOf(1,1))
    private var FROG : Array<IntArray> = arrayOf(intArrayOf(-1,1),intArrayOf(-2,0),intArrayOf(1,-1))
    private var RABBIT : Array<IntArray> = arrayOf(intArrayOf(1,1),intArrayOf(2,0),intArrayOf(-1,-1))
    private var CRAB : Array<IntArray> = arrayOf(intArrayOf(0,1),intArrayOf(-2,0),intArrayOf(2,0))
    private var ELEPHANT : Array<IntArray> = arrayOf(intArrayOf(-1,1),intArrayOf(1,1),intArrayOf(-1,0),intArrayOf(1,0))
    private var GOOSE : Array<IntArray> = arrayOf(intArrayOf(-1,1),intArrayOf(-1,0),intArrayOf(1,0),intArrayOf(1,-1))
    private var ROOSTER : Array<IntArray> = arrayOf(intArrayOf(1,1),intArrayOf(-1,0),intArrayOf(1,0),intArrayOf(-1,1))
    private var MONKEY : Array<IntArray> = arrayOf(intArrayOf(-1,1),intArrayOf(1,1),intArrayOf(-1,-1),intArrayOf(1,-1))
    private var MANTIS : Array<IntArray> = arrayOf(intArrayOf(-1,1),intArrayOf(1,1),intArrayOf(0,-1))
    private var HORSE : Array<IntArray> = arrayOf(intArrayOf(0,1),intArrayOf(-1,0),intArrayOf(0,-1))
    private var OX : Array<IntArray> = arrayOf(intArrayOf(0,1),intArrayOf(1,0),intArrayOf(0,-1))
    private var CRANE : Array<IntArray> = arrayOf(intArrayOf(0,1),intArrayOf(-1,-1),intArrayOf(1,-1))
    private var BOAR : Array<IntArray> = arrayOf(intArrayOf(0,1),intArrayOf(-1,0),intArrayOf(1,0))
    private var EEL : Array<IntArray> = arrayOf(intArrayOf(-1,1),intArrayOf(1,0),intArrayOf(-1,-1))
    private var COBRA : Array<IntArray> = arrayOf(intArrayOf(1,1),intArrayOf(-1,0),intArrayOf(1,-1))


    fun getCard(namacard:String):Array<IntArray>{
        if (namacard == "TIGER") {
            return TIGER
        }
        else if (namacard == "DRAGON") {
            return DRAGON
        }
        else if (namacard == "FROG") {
            return FROG
        }
        else if (namacard == "RABBIT") {
            return RABBIT
        }
        else if (namacard == "CRAB") {
            return CRAB
        }
        else if (namacard == "ELEPHANT") {
            return ELEPHANT
        }
        else if (namacard == "GOOSE") {
            return GOOSE
        }
        else if (namacard == "ROOSTER") {
            return ROOSTER
        }
        else if (namacard == "MONKEY") {
            return MONKEY
        }
        else if (namacard == "MANTIS") {
            return MANTIS
        }
        else if (namacard == "HORSE") {
            return HORSE
        }
        else if (namacard == "OX") {
            return OX
        }
        else if (namacard == "CRANE") {
            return CRANE
        }
        else if (namacard == "BOAR") {
            return BOAR
        }
        else if (namacard == "EEL") {
            return EEL
        }
        else{
            return COBRA
        }
    }

    fun getNamecard(idx:Int):String{
        return allcard[idx]
    }
}