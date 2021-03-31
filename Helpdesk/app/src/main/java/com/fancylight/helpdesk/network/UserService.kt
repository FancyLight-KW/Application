package com.fancylight.helpdesk.network

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.lang.reflect.Type

interface UserService {

    @GET("requests")
    fun testGet(
            @Header ("Authorization") Authorization :String
    ): retrofit2.Call<Array<getRequest>>

    @GET("/csrstatus")
    fun csrget(): retrofit2.Call<CSR>


    @FormUrlEncoded
    @POST("/auth/login")
    fun loginPost(@Field("User_id") User_id: String,
                 @Field("User_password") User_password: String,
    ): retrofit2.Call<Login>
}

val retrofit = Retrofit.Builder()
    .baseUrl("http://54.180.123.42:5000/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object UserApi{
    var ttt : String =""
    val service : UserService by lazy {
        retrofit.create(UserService::class.java)
    }
}



