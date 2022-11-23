package com.example.onitama

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class EndGame : AppCompatActivity() {

    lateinit var playerwinnerTv:TextView
    lateinit var playagainBtn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game)

        var pemenang = intent.getStringExtra("pemenang")

        playerwinnerTv = findViewById(R.id.playerwinnerTv)
        playagainBtn = findViewById(R.id.playagainBtn)

        playerwinnerTv.setText(pemenang)

        playagainBtn.setOnClickListener {
            finish()
        }
    }
}