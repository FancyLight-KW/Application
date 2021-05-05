package com.fancylight.helpdesk.model

import java.io.Serializable
import java.time.LocalDate
import java.util.Arrays

const val SERVICE_STAT_A = "접수완료"
const val SERVICE_STAT_B = "요청처리"
const val SERVICE_STAT_C = "처리완료"
const val SERVICE_STAT_D = "접수대기"

const val INQUIRY_TYPE_SYSTEM = "업무시스템"      // 기능 변경수정
const val INQUIRY_TYPE_IT = "IT인프라"    // 단순문의
const val INQUIRY_TYPE_OA = "OA장비"      // 장애처리


data class Inquiry (
        val REQ_SEQ : Int, val TITLE : String, val CONTENT : String,
        val CORP_CODE : String, val TARGET_CODE : String, val SYSTEM_GROUP_CODE : String,
        val TM_APPROVAL_REQ_YN : Char,
        val CSR_STATUS : String, val IMSI_YN : Char, val REQ_FINISH_DATE : String,
        val REG_USER_ID : String, val MOD_USER_ID : String, val REQ_IMG_PATH : String,
        val updateAt : String, val createdAt : String
) : Serializable

fun parseDate(date : String) : MutableList<String>{
    val dateArr : MutableList<String> = date.split("-").toMutableList()
    if(dateArr[1].length==2 && dateArr[1].dropLast(1)=="0"){
        dateArr[1] =  dateArr[1].drop(1)
    }
    return dateArr

}

