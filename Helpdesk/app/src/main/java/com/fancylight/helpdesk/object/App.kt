package com.fancylight.helpdesk.`object`

import com.fancylight.helpdesk.model.Personnel

class App {
    private val personnelList = mutableListOf(
            Personnel("sch", "김ㅇㅇ", 2, 2),
            Personnel("pk", "이ㅇㅇ", 1, 3),
            Personnel("abc", "박ㅇㅇ", 2, 1),
            Personnel("lsd", "최ㅇㅇ", 3, 4),
            Personnel("werds", "정ㅇㅇ", 2, 2),
            Personnel("vx", "한ㅇㅇ", 5, 3),
            Personnel("qlq", "송ㅇㅇ", 3, 4),
            Personnel("hpb", "임ㅇㅇ", 4, 3),
            Personnel("fjw", "배ㅇㅇ", 4, 2),
            Personnel("bdf", "양ㅇㅇ", 7, 5),
    )

    fun getPersonnelList(): List<Personnel> {
        return personnelList.toList()
    }
}