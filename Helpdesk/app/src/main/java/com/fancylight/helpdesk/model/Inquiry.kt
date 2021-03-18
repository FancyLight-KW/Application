package com.fancylight.helpdesk.model

import java.time.LocalDate

const val SERVICE_STAT_A = "접수완료"
const val SERVICE_STAT_B = "변경관리 처리중"
const val SERVICE_STAT_C = "접수대기"
const val SERVICE_STAT_D = "기타"

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

