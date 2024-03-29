package com.fancylight.helpdesk.network

import com.google.gson.JsonObject

data class Login(val token:String,val resultCode:Int)

data class CSR(val 요청처리중:Int ,val 접수대기 : Int, val 접수완료 : Int, val 처리완료 : Int, val 요청반려 : Int)
data class JsonData(val result : JsonObject)
data class Fcm(val resultCode: Int )
data class ChangePassword(val resultCode: Int, val message : String)
data class ResultMessage(val resultCode: Int, val message : String)
data class sResultMessage(val resultcode: Int, val message : String)
data class ChatbotReturn(val fulfillmentText : String)
data class AgentListForm(val DOING : Int, val READY : Int, val User_id : String, val User_name : String)

data class getRequest(val REQ_SEQ : Int, val TITLE : String, val CONTENT : String,
                      val CORP_CODE : String, val TARGET_CODE : String, val SYSTEM_GROUP_CODE : String,
                      val TM_APPROVAL_REQ_YN : Char, val CSR_STATUS : String, val REQ_FINISH_DATE : String,
                      val REG_USER_ID : String, val MOD_USER_ID : String, val REQ_IMG_PATH : String,
                      val EXPECTED_FINISH_DATE: String, val REAL_FINISH_DATE: String,
                      val updatedAt : String, val createdAt : String)