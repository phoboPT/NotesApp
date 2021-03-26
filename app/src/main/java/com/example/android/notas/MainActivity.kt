package com.example.android.notas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aplicacao.android.notas.R
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result;
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bntNotas = findViewById<Button>(R.id.pagNotas)
        var Problems = ArrayList<Problem>()

        bntNotas.setOnClickListener {
            val intent = Intent(this@MainActivity, Ecra::class.java)
            startActivity(intent)
        }


        "http://localhost:8888/api/users/all".httpGet().responseObject(Problem.Deserializer()) { req, res, result ->

            val (people, err) = result
            Log.d("ITEM","PEOPLE "+ people.toString())
            //Add to ArrayList
            people?.forEach { problem ->
              Log.d("ITEM","problem "+problem.toString())
                Problems.add(problem)
            }

            println(Problems)
        }

    }
}