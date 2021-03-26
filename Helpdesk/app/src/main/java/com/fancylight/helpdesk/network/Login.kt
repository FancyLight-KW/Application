package com.fancylight.helpdesk.network

data class Login(val message:String,val resultCode:Int)
data class CSR(val CSR진행상태:Int, val 완료:Int ,val 접수 : Int, val 진행 : Int)