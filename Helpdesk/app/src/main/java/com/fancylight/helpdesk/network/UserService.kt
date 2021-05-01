package com.fancylight.helpdesk.network

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.lang.reflect.Type

interface UserService {

    //모든 접수리스트 가져오기
    @GET("requests")
    fun testGet(
            @Header ("Authorization") Authorization :String
    ): retrofit2.Call<Array<getRequest>>

    //사원의 완료되지 않은 요청 모두 보여
    @GET("mypage")
    fun requestListGet(
            @Header ("Authorization") Authorization :String
    ): retrofit2.Call<Array<getRequest>>

    //요원 자신에게 할당된 요청 가져오기
    @GET("agent")
    fun workListGet(
            @Header ("Authorization") Authorization :String
    ): retrofit2.Call<Array<getRequest>>

    //요원에게 할당전인 요청 관리자에게 가져오
    @GET("admin")
    fun adminListGet(
            @Header ("Authorization") Authorization :String
    ): retrofit2.Call<Array<getRequest>>

    //csr 상태 가져오기
    @GET("csrstatus")
    fun csrget(): retrofit2.Call<CSR>


    @FormUrlEncoded
    @POST("auth/login")
    fun loginPost(@Field("User_id") User_id: String,
                 @Field("User_password") User_password: String,
    ): retrofit2.Call<Login>


    @FormUrlEncoded
    @POST("android")
    fun FcmPost(
            @Header ("Authorization") Authorization :String,
            @Field("DEVICE_ID") DEVICE_ID: String,
    ): retrofit2.Call<Fcm>

    @Multipart
    @POST("requests")
    fun dataPost(
            @Header ("Authorization") Authorization :String,
            @Part imagefile : MultipartBody.Part,
            @Part("body") body : String
    ): retrofit2.Call<JsonData>

    //챗봇 통신
    @FormUrlEncoded
    @POST("dialogflow/textQuery")
    fun ChatbotPost(
            @Header ("Authorization") Authorization :String,
            @Field("text") text: String,
    ): retrofit2.Call<ChatbotReturn>




    //비밀번호 변경할 때
    @FormUrlEncoded
    @PUT("users/password")
    fun passwordChangePut(
        @Header ("Authorization") Authorization :String,
        @Field("origin_password") origin_password: String,
        @Field("new_password") new_password: String
    ): retrofit2.Call<ChangePassword>

//요원이 할당된 요청을 작업중이나 작업완료로 바꿀 때
    @FormUrlEncoded
    @PUT("agent/:{num}/:{complete}")
    fun WorkChangePut(
            @Header ("Authorization") Authorization :String,
            @Path("num") num : Int,
            @Path("complete") complete : Int,
            @Field("CSR_STATUS") CSR_STATUS: String,
            @Field("DATE") DATE: String
    ): retrofit2.Call<ResultMessage>


}

val retrofit = Retrofit.Builder()
    .baseUrl("http://54.180.123.42/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object UserApi{
    var ttt : String =""
    var fcmToken : String =""
    val service : UserService by lazy {
        retrofit.create(UserService::class.java)
    }
}



