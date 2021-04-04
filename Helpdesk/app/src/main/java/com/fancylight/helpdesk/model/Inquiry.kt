package com.fancylight.helpdesk.model

import java.time.LocalDate
import java.util.Arrays

const val SERVICE_STAT_A = "완료"
const val SERVICE_STAT_B = "진행"
const val SERVICE_STAT_C = "접수"
const val SERVICE_STAT_D = "접수대기"

const val INQUIRY_TYPE_CHANGE = 1      // 기능 변경수정
const val INQUIRY_TYPE_NORMAL = 2    // 단순문의
const val INQUIRY_TYPE_DEBUG = 3       // 장애처리
const val INQUIRY_TYPE_ETC = 4         // 기타

data class Inquiry (
        val id: Int,
        val serviceStat: String,      // SERVICE_STAT_* 상수 입력
        val type: Int,              // INQUIRY_TYPE_* 상수 입력
        val title: String,
        val date: LocalDate,
)
fun parseDate(date : String) : MutableList<String>{
    val dateArr : MutableList<String> = date.split("-").toMutableList()
    if(dateArr[1].length==2 && dateArr[1].dropLast(1)=="0"){
        dateArr[1] =  dateArr[1].drop(1)
    }
    return dateArr

}

