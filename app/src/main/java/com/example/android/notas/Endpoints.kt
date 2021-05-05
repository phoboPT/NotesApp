package com.example.android.notas

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    //Endpoint para a realização do login
    @FormUrlEncoded
    @POST("problems/add")
    fun addProblem(@Field("userId") userId: String?, @Field("lat") lat: String?,@Field("lon") long: String?,@Field("problem") problem: String?,@Field("type") type: String?): Call<Problem>

    @FormUrlEncoded
    @PUT("problems/update")
    fun updateProblem(@Field("userId") userId: Int?, @Field("lat") lat: String?,@Field("lon") long: String?,@Field("problem") problem: String?,@Field("id") id: Int?,@Field("type") type: String?): Call<Problem>

    //Endpoint para a realização do login
    @FormUrlEncoded
    @POST("users/login")
    fun login(@Field("email") email: String?, @Field("password") password: String?): Call<User>

    @GET("users/all")
    fun getAllUsers(): Call<List<User>>

    @GET("problems/all")
    fun getAllOcorrencias(): Call<List<Problem>>




    //Endpoint para a submissão de uma imagem
    //  @Multipart
    //@POST("ocorrencias/submeterImagem")
    // fun submeterImagem(@Part imagem :MultipartBody.Part, @Part("name") name: RequestBody) : Call<RespostaImg>


}