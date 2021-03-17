package com.example.android.notas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.aplicacao.android.notas.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bntNotas = findViewById<Button>(R.id.pagNotas)

        bntNotas.setOnClickListener {
            val intent = Intent(this@MainActivity, Ecra::class.java)
            startActivity(intent)
        }

    }
}