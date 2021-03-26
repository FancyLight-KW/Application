package com.fancylight.helpdesk.network

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.lang.reflect.Type

interface UserService {

    @GET("new_user2")
    fun testGet(): retrofit2.Call<Login>

    @GET("/csrstatus")
    fun csrget(): retrofit2.Call<CSR>


    @FormUrlEncoded
    @POST("/login")
    fun loginPost(@Field("User_id") User_id: String,
                 @Field("User_password") User_password: String,
    ): retrofit2.Call<Login>
}

val retrofit = Retrofit.Builder()
    .baseUrl("http://3.34.2.162:5000/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object UserApi{
    val service : UserService by lazy {
        retrofit.create(UserService::class.java)
    }
}



