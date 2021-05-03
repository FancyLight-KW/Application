package com.fancylight.helpdesk

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fancylight.helpdesk.model.Inquiry
import com.fancylight.helpdesk.`object`.MemberInfo
import com.fancylight.helpdesk.`object`.SubmitObject
import java.util.*
import android.content.Intent
import android.util.Log
import android.widget.*
import com.fancylight.helpdesk.network.Fcm
import com.fancylight.helpdesk.network.ResultMessage
import com.fancylight.helpdesk.network.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// Intent 로 전달될 값(extra)들의 키 상수
const val EXTRA_INQUIRY = "com.fancylight.helpdesk.inquiry";
const val EXTRA_INQUIRY_NO = "com.fancylight.helpdesk.inquiry_no";

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var inquiry: Inquiry
    private var authState: Int = 3      // 로그인 상태 (권한 변수)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // DB 로부터 authState 값을 불러옴

        authState = MemberInfo.User_position

        // authState 에 따라 다른 레이아웃 생성
        setContentView(
            when (authState) {
                1 -> R.layout.activity_detail_reque
                2 -> R.layout.activity_detail_work
                3 -> R.layout.activity_detail_approval
                else -> R.layout.activity_detail_reque
            }
        )

        // 인텐트로 전달된 Inquiry 와 순서값을 받는다
        inquiry = intent.getSerializableExtra(EXTRA_INQUIRY) as Inquiry
        val inquiryNo = intent.getIntExtra(EXTRA_INQUIRY_NO, 0)

        // 전달받은 Inquiry 정보로 UI 를 업데이트한다
        val serviceStatText: TextView = findViewById(R.id.txt_service_stat)
        val noText: TextView = findViewById(R.id.txt_no)
        val titleText: TextView = findViewById(R.id.txt_inquiry_title)
        val contentText: TextView = findViewById(R.id.txt_inquiry_content)
        val date: TextView = findViewById(R.id.textDesiredDate)


        serviceStatText.text = inquiry.serviceStat
        noText.text = "$inquiryNo"
        titleText.text = inquiry.title
        contentText.text = inquiry.content
        date.text = inquiry.date.toString()

        // 버튼에 리스너를 설정한다 (권한 변수에 따라 다른 버튼을 대상으로)
        when (authState) {
            1 -> {
                val modifyButton: Button = findViewById(R.id.btn_modify)
                modifyButton.setOnClickListener(this)
            }
            2 -> {
                val startWorkButton: Button = findViewById(R.id.btn_start_work)
                val finishWorkButton: Button = findViewById(R.id.btn_finish_work)
                val pickExpectedDateButton: ImageButton = findViewById(R.id.btnExpectedDate)
                startWorkButton.setOnClickListener(this)
                finishWorkButton.setOnClickListener(this)
                pickExpectedDateButton.setOnClickListener(this)
            }
            3 -> {
                val approveButton: Button = findViewById(R.id.btn_approve)
                val dismissButton: Button = findViewById(R.id.btn_dismiss)
                approveButton.setOnClickListener(this)
                dismissButton.setOnClickListener(this)
            }
            else -> {
            }
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btn_modify -> {
            // TODO : (수정) 버튼 -> detail 항목들 수정하기


            }

            R.id.btnExpectedDate -> {
                val textExpectedDate : TextView = findViewById(R.id.textExpectedDate)
                val today = GregorianCalendar()
                val year: Int = today.get(Calendar.YEAR)
                val month: Int = today.get(Calendar.MONTH)
                val date: Int = today.get(Calendar.DATE)
                val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                        textExpectedDate.setText("${year}/ ${month + 1}/ ${dayOfMonth}")
                        SubmitObject.dateSet(year,month+1,dayOfMonth)
                    }
                }, year, month, date)
                dlg.show()
            }
            R.id.btn_start_work -> {
            // 작업시작
                val textExpectedDate : TextView = findViewById(R.id.textExpectedDate)

                if(textExpectedDate.text == "") {
                    Toast.makeText(applicationContext,"예상 완료일을 지정해 주세요." , Toast.LENGTH_SHORT).show()
                } else {

                    UserApi.service.WorkChangePut("Bearer " + UserApi.ttt, 5, 0, "요청처리중", "19941212").enqueue(object : Callback<ResultMessage> {
                        override fun onResponse(call: Call<ResultMessage>, response: Response<ResultMessage>) {
                            if(response.isSuccessful){
                                val result=response.body()!!.resultCode
                                val message=response.body()!!.message

                                if(result==0){
                                    Toast.makeText(applicationContext,"성공 : " + textExpectedDate.text , Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                else {
                                    Toast.makeText(applicationContext,"실패" + message, Toast.LENGTH_SHORT).show()
                                }
                            }
                            else{
                                Toast.makeText(applicationContext,"실패" , Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<ResultMessage>, t: Throwable) {
                            Toast.makeText(applicationContext,"실패실패", Toast.LENGTH_LONG).show()
                            Log.e("failure errorrr", ""+t)
                        }
                    })
                }

            }

            R.id.btn_finish_work -> {
            // TODO : (작업 완료) 버튼 -> '처리완료' 상태로 변경
                UserApi.service.WorkChangePut("Bearer " + UserApi.ttt, 7, 1, "처리완료", "20210501").enqueue(object : Callback<ResultMessage> {
                    override fun onResponse(call: Call<ResultMessage>, response: Response<ResultMessage>) {
                        if(response.isSuccessful){
                            val result=response.body()!!.resultCode
                            val message=response.body()!!.message

                            if(result==0){
                                Toast.makeText(applicationContext,"성공" + message, Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            else {
                                Toast.makeText(applicationContext,"실패" + message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Toast.makeText(applicationContext,"실패" , Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResultMessage>, t: Throwable) {
                        Toast.makeText(applicationContext,"실패실패", Toast.LENGTH_LONG).show()
                        Log.e("failure errorrr", ""+t)
                    }
                })


            }

            R.id.btn_approve -> {
            // TODO : (승인) 버튼 -> 요원목록 띄우고 고른 후 창 닫기

                startPersonnelActivity()


            }
            R.id.btn_dismiss -> {
            // TODO : (반려) 버튼 -> '요청반려' 상태로 변경
                UserApi.service.adminDenyPut("Bearer " + UserApi.ttt, inquiry.id).enqueue(object : Callback<ResultMessage> {
                    override fun onResponse(call: Call<ResultMessage>, response: Response<ResultMessage>) {
                        if(response.isSuccessful){
                            val result=response.body()!!.resultCode
                            val message=response.body()!!.message

                            if(result==0){
                                Toast.makeText(applicationContext,"성공" + message, Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            else {
                                Toast.makeText(applicationContext,"실패" + message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Toast.makeText(applicationContext,"실패" , Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResultMessage>, t: Throwable) {
                        Toast.makeText(applicationContext,"실패실패", Toast.LENGTH_LONG).show()
                        Log.e("failure errorrr", ""+t)
                    }
                })


            }
        }
    }

    // PersonnelActivity 시작
    private fun startPersonnelActivity() {

        val intent = Intent(this, PersonnelActivity::class.java)
        startActivity(intent)
    }
}

