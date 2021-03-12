package com.fancylight.helpdesk.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

    @GET("new_user2")
    fun testGet(): retrofit2.Call<Login>


    @FormUrlEncoded
    @POST("/login")
    fun testPost(@Field("User_id") User_id: String,
                 @Field("User_password") User_password: String,
                 //@Field("User_name") User_name: String,
                 // @Field("User_lastlogin") User_lastlogin: String
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
