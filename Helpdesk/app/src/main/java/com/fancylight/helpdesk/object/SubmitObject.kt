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
    var path =""

    fun init(){
        this.target = ""
        this.systemCode = ""
        this.tmApproval = "N"
        this.title = ""
        this.content = ""
        this.finishDate = ""
        this.corpCode = "법인코드"
        this.csrStatus = "접수대기"
        this.path = ""
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
        "CSR_STATUS":"${csrStatus}"
        }
        """.trimIndent()
    }

    fun dateSet(year : Int, month : Int, day : Int) :String {
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

        return year.toString() + monthStr + dayStr
    }

    fun isEmpty() : String{
        if(target ==""){
            return "문의대상을 선택해주세요."
        }else if(systemCode=="-"){
            return "시스템명을 선택해주세요."
        }else if(title==""){
            return "제목을 작성해주세요"
        }else if(content==""){
            return "요청내용을 작성해주세요"
        }else if(finishDate==""){
            return "희망완료일을 선택해주세요"
        }else{
            return "완료"
        }

    }

}