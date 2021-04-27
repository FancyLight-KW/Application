package com.fancylight.helpdesk.`object`

object SubmitObject {
    var target = ""
    var systemCode = ""
    var tmApproval = "N"
    var title = ""
    var content = ""
    var finishDate = ""
    var corpCode = "법인코드"
    var csrStatus = "접수대기"
    var imsi = "w"
    var path = ""

    fun init(){
        this.target = ""
        this.systemCode = ""
        this.tmApproval = "N"
        this.title = ""
        this.content = ""
        this.finishDate = ""
        this.corpCode = "법인코드"
        this.csrStatus = "접수대기"
        this.imsi = "w"
        var path = ""
    }

    fun convertJson() : String{
        return """{
        "TARGET_CODE":"${target}",
        "SYSTEM_GROUP_CODE":"${systemCode}",
        "TM_APPROVAL_REQ_YN":"${tmApproval}",
        "TITLE":"${title}",
        "CONTENT":"${content}",
        "REQ_FINISH_DATE":"${finishDate}",
        "CORP_CODE":"${corpCode}",
        "CSR_STATUS":"${csrStatus}",
        "IMSI_YN":"${imsi}"
        }
        """.trimIndent()
    }

    fun dateSet(year : Int, month : Int, day : Int) {
        var monthStr : String
        var dayStr : String
        if(month <10)
            monthStr = "0" + month
        else
            monthStr = ""+month

        if(day <10)
            dayStr = "0" + day
        else
            dayStr = ""+day

        finishDate =year.toString() + month + dayStr
    }

}