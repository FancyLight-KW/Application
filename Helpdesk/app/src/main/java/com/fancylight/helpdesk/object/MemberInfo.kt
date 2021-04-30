package com.fancylight.helpdesk.`object`

import android.util.Log
import com.google.gson.JsonObject
import org.json.JSONObject

object MemberInfo {
    var User_id : String = ""
    var User_name : String =""
    var User_position : Int =0

    fun infoSet(json : String){
        val jsonObject = JSONObject(json)
        User_id = jsonObject.getString("User_id")
        User_name = jsonObject.getString("User_name")
        User_position = jsonObject.getInt("User_position")
        Log.d("infoset", User_id+"/"+ User_name+"/"+ User_position)
    }
}