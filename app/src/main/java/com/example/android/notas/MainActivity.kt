package com.example.android.notas

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aplicacao.android.notas.R

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var editNomeUser: EditText
    private lateinit var editPwd: EditText
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bntNotas = findViewById<Button>(R.id.pagNotas)
        val btnLogin = findViewById<Button>(R.id.buttonLogin)
        editNomeUser = findViewById(R.id.editTextTextPersonName)
        editPwd = findViewById(R.id.editTextTextPassword)
        // sharedPreferences = getSharedPreferences(getString(R.string.user_creds_file_key), Context.MODE_PRIVATE)

        bntNotas.setOnClickListener {
            val intent = Intent(this@MainActivity, Ecra::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            login()
        }
        //  getPointsWS()


    }

    private fun login() {
        val nomeUser = editNomeUser.text.toString()
        val pwd = editPwd.text.toString()
        if (nomeUser.isNotEmpty() && pwd.isNotEmpty()) {
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            // val encryptedUserName = ChCrypto.aesEncrypt(nomeUser, "4u7x!A%D*G-KaPdSgVkYp3s5v8y/B?E(")
            // val encryptedPwd = ChCrypto.aesEncrypt(pwd, "4u7x!A%D*G-KaPdSgVkYp3s5v8y/B?E(")
            val call = request.login(email = nomeUser, password = pwd)
            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {

                    if (response.isSuccessful) {
                        if (response.body()?.id!! > 0) {
                            Toast.makeText(
                                    this@MainActivity,
                                    "Login realizado com sucesso!",
                                    Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@MainActivity, MapsActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                    this@MainActivity,
                                    "login falhou",
                                    Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, getString(R.string.login_form_error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPointsWS() {

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getAllOcorrencias()

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                var mapIncidences = response.body()!!
                Log.d("ITEM", "entrou")
                for (map in mapIncidences) {
                    Log.d("ITEM", "hey " + map.toString())
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d("ITEM", "erro " + t.message)
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}