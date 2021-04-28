package com.fancylight.helpdesk.network

import com.google.gson.JsonObject

data class Login(val token:String,val resultCode:Int)
data class CSR(val 요청처리중:Int ,val 접수대기 : Int, val 접수완료 : Int, val 처리지연중 : Int)
data class getRequest(val REQ_SEQ : Int, val TITLE : String, val CONTENT : String,
                       val CORP_CODE : String, val TARGET_CODE : String, val SYSTEM_GROUP_CODE : String,
                        val SYSTEM_CODE : String, val REQ_TYPE_CODE : String, val TM_APPROVAL_REQ_YN : Char,
                        val CSR_STATUS : String, val IMSI_YN : Char, val REQ_FINISH_DATE : String,
                        val REG_USER_ID : String, val MOD_USER_ID : String, val REQ_IMG_PATH : String,
                        val updateAt : String, val createdAt : String)

data class JsonData(val result : JsonObject)
data class Fcm(val resultCode: Int )
data class ChangePassword(val resultCode: Int, val message : String)