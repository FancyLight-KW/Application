package com.fancylight.helpdesk.network

data class Login(val token:String,val resultCode:Int)
data class CSR(val CSR진행상태:Int, val 완료:Int ,val 접수 : Int, val 진행 : Int)
data class getRequest(val REQ_SEQ : Int, val TITLE : String, val CONTENT : String,
                       val CORP_CODE : String, val TARGET_CODE : String, val SYSTEM_GROUP_CODE : String,
                        val SYSTEM_CODE : String, val REQ_TYPE_CODE : String, val TM_APPROVAL_REQ_YN : Char,
                        val CSR_STATUS : String, val IMSI_YN : Char, val REQ_FINISH_DATE : String,
                        val REG_USER_ID : String, val MOD_USER_ID : String, val REQ_IMG_PATH : String,
                        val updateAt : String, val createdAt : String)