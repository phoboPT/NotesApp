package com.example.android.notas

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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
        sharedPreferences = getSharedPreferences(getString(R.string.user_creds_file_key), Context.MODE_PRIVATE)


        val username = sharedPreferences.getString(getString(R.string.username), "")
        val password = sharedPreferences.getString(getString(R.string.password), "")
        val userId = sharedPreferences.getInt(getString(R.string.userId), 0)
        if (username != "" && password != "") {
            if (username != null && password != null) {
                Toast.makeText(
                        this@MainActivity,
                        "user " + userId,
                        Toast.LENGTH_SHORT
                ).show()
                login(username, password)
            }
        }

        bntNotas.setOnClickListener {
            val intent = Intent(this@MainActivity, Ecra::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            login("", "")
        }
    }

    private fun login(username: String, password: String) {
        var nomeUser = editNomeUser.text.toString()
        var pwd = editPwd.text.toString()

        if (username != "" && password != "") {
            nomeUser = username
            pwd = password
        }

        if (nomeUser.isNotEmpty() && pwd.isNotEmpty()) {
            val request = ServiceBuilder.buildService(EndPoints::class.java)
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

                            val editor = sharedPreferences.edit()

                            val userId = response.body()!!.id
                            editor.putString(getString(R.string.username), nomeUser)
                            editor.putString(getString(R.string.password), pwd)
                            editor.putInt(getString(R.string.userId), userId)
                            editor.apply()

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


}