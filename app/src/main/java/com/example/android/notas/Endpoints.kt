package com.example.android.notas

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    //Endpoint para a realização do login
    @FormUrlEncoded
    @POST("problemm/add")
    fun addProblem(@Field("userId") userId: String?, @Field("lat") lat: String?,@Field("long") long: String?,@Field("problem") problem: String?): Call<Problem>

    //Endpoint para a realização do login
    @FormUrlEncoded
    @POST("users/login")
    fun login(@Field("email") email: String?, @Field("password") password: String?): Call<User>

    @GET("users/all")
    fun getAllOcorrencias(): Call<List<User>>


    //Endpoint para a submissão de uma imagem
    //  @Multipart
    //@POST("ocorrencias/submeterImagem")
    // fun submeterImagem(@Part imagem :MultipartBody.Part, @Part("name") name: RequestBody) : Call<RespostaImg>


}