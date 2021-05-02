package com.fancylight.helpdesk.model

// 요원 데이터 클래스

data class Personnel(
    val id: String? = null,         // 요원 ID
    val name: String? = null,       // 이름
    val numberOngoing: Int = 0,     // 진행중 개수
    val numberAssigned: Int = 0,    // 할당량 개수
)

