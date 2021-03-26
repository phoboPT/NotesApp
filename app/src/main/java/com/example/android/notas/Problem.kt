package com.example.android.notas

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

//User Model


data class Problem(
        val name: String = "",
        val email: String = "",
        val password: String = ""
){
    class Deserializer: ResponseDeserializable<Array<Problem>> {
        override fun deserialize(content: String): Array<Problem>? = Gson().fromJson(content, Array<Problem>::class.java)
    }
}