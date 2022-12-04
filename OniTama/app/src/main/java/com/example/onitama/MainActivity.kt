package com.example.onitama

import android.content.Intent
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

    lateinit var twoplayerBtn:Button
    lateinit var VSAI:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        twoplayerBtn = findViewById(R.id.twoplayerBtn)
        VSAI = findViewById(R.id.vsAIBtn)

        twoplayerBtn.setOnClickListener {
            var intent = Intent(this,TwoPlayer::class.java).apply {
                putExtra("isAI", false)
            }
            startActivity(intent)
        }

        VSAI.setOnClickListener {
            var intent = Intent(this,TwoPlayer::class.java).apply {
                putExtra("isAI", true)
            }
            startActivity(intent)
        }
    }

}