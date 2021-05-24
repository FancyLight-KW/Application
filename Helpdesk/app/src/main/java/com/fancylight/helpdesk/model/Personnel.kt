package com.fancylight.helpdesk.model

import java.io.Serializable

// 요원 데이터 클래스

data class Personnel(
        val DOING: Int,
        val READY: Int,
        val User_id: String,
        val User_name: String,
) : Serializable

