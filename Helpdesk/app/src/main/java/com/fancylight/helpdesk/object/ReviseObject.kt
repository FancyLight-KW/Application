package com.fancylight.helpdesk.`object`

import com.fancylight.helpdesk.model.Inquiry

object ReviseObject {
    var REQ_SEQ = 0
    var TITLE = ""
    var CONTENT = ""
    var CORP_CODE = ""
    var TARGET_CODE = ""
    var SYSTEM_GROUP_CODE = ""
    var TM_APPROVAL_REQ_YN = 'N'
    var CSR_STATUS = ""
    var IMSI_YN ='w'
    var REQ_FINISH_DATE = ""
    var REG_USER_ID = ""
    var MOD_USER_ID = ""
    var REQ_IMG_PATH = ""
    var createdAt = ""
    var updatedAt = ""

    fun init(inquiry: Inquiry){
        REQ_SEQ = inquiry.REQ_SEQ
        TITLE = inquiry.TITLE
        CONTENT = inquiry.CONTENT
        CORP_CODE = inquiry.CORP_CODE
        TARGET_CODE = inquiry.TARGET_CODE
        SYSTEM_GROUP_CODE = inquiry.SYSTEM_GROUP_CODE
        TM_APPROVAL_REQ_YN = inquiry.TM_APPROVAL_REQ_YN
        CSR_STATUS = inquiry.CSR_STATUS
        IMSI_YN =inquiry.IMSI_YN
        REQ_FINISH_DATE = inquiry.REQ_FINISH_DATE
        REG_USER_ID = inquiry.REG_USER_ID
        MOD_USER_ID = inquiry.MOD_USER_ID
        REQ_IMG_PATH = inquiry.REQ_IMG_PATH
        createdAt = inquiry.createdAt
        updatedAt = inquiry.updateAt
    }

    fun convertJson(case : Int) : String{
        /*
        0-> "MOD_USER_ID": null, "REQ_IMG_PATH": 필요없을때
        1-> "MOD_USER_ID": null, "REQ_IMG_PATH": 필요할때
        2-> "MOD_USER_ID": 값있음, "REQ_IMG_PATH": 필요없을때
        3-> "MOD_USER_ID": 값있음, "REQ_IMG_PATH": 필요할때
         */
        var result = ""
        when(case){
            0 -> {
                result ="""{
                    "REQ_SEQ": "${this.REQ_SEQ}",
                    "TITLE": "${this.TITLE}",
                    "CONTENT": "${this.CONTENT}",
                    "CORP_CODE": "${this.CORP_CODE}",
                    "TARGET_CODE": "${this.TARGET_CODE}",
                    "SYSTEM_GROUP_CODE": "${this.SYSTEM_GROUP_CODE}",
                    "TM_APPROVAL_REQ_YN": "${this.TM_APPROVAL_REQ_YN}",
                    "CSR_STATUS": "${this.CSR_STATUS}",
                    "IMSI_YN": "${this.IMSI_YN}",
                    "REQ_FINISH_DATE": "${this.REQ_FINISH_DATE}",
                    "REG_USER_ID": "${this.REG_USER_ID}",
                    "createdAt": "${this.createdAt}",
                    "updatedAt": "${this.updatedAt}"
                }""".trimIndent()
            }

            1 -> {
                result ="""{
                    "REQ_SEQ": "${this.REQ_SEQ}",
                    "TITLE": "${this.TITLE}",
                    "CONTENT": "${this.CONTENT}",
                    "CORP_CODE": "${this.CORP_CODE}",
                    "TARGET_CODE": "${this.TARGET_CODE}",
                    "SYSTEM_GROUP_CODE": "${this.SYSTEM_GROUP_CODE}",
                    "TM_APPROVAL_REQ_YN": "${this.TM_APPROVAL_REQ_YN}",
                    "CSR_STATUS": "${this.CSR_STATUS}",
                    "IMSI_YN": "${this.IMSI_YN}",
                    "REQ_FINISH_DATE": "${this.REQ_FINISH_DATE}",
                    "REG_USER_ID": "${this.REG_USER_ID}", 
                    "REQ_IMG_PATH": "${this.REQ_IMG_PATH}",
                    "createdAt": "${this.createdAt}",
                    "updatedAt": "${this.updatedAt}"
                }""".trimIndent()
            }

            2 -> {
                result ="""{
                    "REQ_SEQ": "${this.REQ_SEQ}",
                    "TITLE": "${this.TITLE}",
                    "CONTENT": "${this.CONTENT}",
                    "CORP_CODE": "${this.CORP_CODE}",
                    "TARGET_CODE": "${this.TARGET_CODE}",
                    "SYSTEM_GROUP_CODE": "${this.SYSTEM_GROUP_CODE}",
                    "TM_APPROVAL_REQ_YN": "${this.TM_APPROVAL_REQ_YN}",
                    "CSR_STATUS": "${this.CSR_STATUS}",
                    "IMSI_YN": "${this.IMSI_YN}",
                    "REQ_FINISH_DATE": "${this.REQ_FINISH_DATE}",
                    "REG_USER_ID": "${this.REG_USER_ID}",
                    "MOD_USER_ID" : "${this.MOD_USER_ID}",
                    "createdAt": "${this.createdAt}",
                    "updatedAt": "${this.updatedAt}"
                }""".trimIndent()
            }

            3 -> {
                result ="""{
                    "REQ_SEQ": "${this.REQ_SEQ}",
                    "TITLE": "${this.TITLE}",
                    "CONTENT": "${this.CONTENT}",
                    "CORP_CODE": "${this.CORP_CODE}",
                    "TARGET_CODE": "${this.TARGET_CODE}",
                    "SYSTEM_GROUP_CODE": "${this.SYSTEM_GROUP_CODE}",
                    "TM_APPROVAL_REQ_YN": "${this.TM_APPROVAL_REQ_YN}",
                    "CSR_STATUS": "${this.CSR_STATUS}",
                    "IMSI_YN": "${this.IMSI_YN}",
                    "REQ_FINISH_DATE": "${this.REQ_FINISH_DATE}",
                    "REG_USER_ID": "${this.REG_USER_ID}", 
                    "MOD_USER_ID" : "${this.MOD_USER_ID}",
                    "REQ_IMG_PATH": "${this.REQ_IMG_PATH}",
                    "createdAt": "${this.createdAt}",
                    "updatedAt": "${this.updatedAt}"
                }""".trimIndent()
            }
        }

        return result
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

        return year.toString() + month + dayStr
    }


}