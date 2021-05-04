

package com.example.android.notas

//User Model
data class Problem(
        val id: Int=0,
        val status: String = "",
        val problem: String = "",
        val lat: String = "",
        val lon: String ="",
        val userId: Int=0
)
