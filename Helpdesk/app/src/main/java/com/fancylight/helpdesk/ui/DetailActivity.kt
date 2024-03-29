package com.fancylight.helpdesk.ui

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fancylight.helpdesk.R
import com.fancylight.helpdesk.`object`.MemberInfo
import com.fancylight.helpdesk.`object`.SubmitObject
import com.fancylight.helpdesk.model.Inquiry
import com.fancylight.helpdesk.network.ResultMessage
import com.fancylight.helpdesk.network.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


// Intent 로 전달될 값(extra)들의 키 상수
const val EXTRA_INQUIRY = "com.fancylight.helpdesk.inquiry";
const val EXTRA_INQUIRY_NO = "com.fancylight.helpdesk.inquiry_no";

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var inquiry: Inquiry
    private var authState: Int = 0      // 로그인 상태 (권한 변수)
    private var vdate : String =""

    private var resultList: MutableList<Inquiry>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // DB 로부터 authState 값을 불러옴

        authState = MemberInfo.User_position

        // 인텐트로 전달된 Inquiry 와 순서값을 받는다
        inquiry = intent.getSerializableExtra(EXTRA_INQUIRY) as Inquiry

        // authState 에 따라 다른 레이아웃 생성
        setContentView(
                when (authState) {
                    1 -> R.layout.activity_detail_reque
                    2 ->
                    {
                        when(inquiry.CSR_STATUS){
                            "접수완료" -> R.layout.activity_detail_work
                            else -> R.layout.activity_detail_work_complete
                        }
                    }
                    3 -> R.layout.activity_detail_approval
                    else -> R.layout.activity_detail_reque
                }
        )


        // 전달받은 Inquiry 정보로 UI 를 업데이트한다
        val serviceStatText: TextView = findViewById(R.id.txt_service_stat)
        val noText: TextView = findViewById(R.id.txt_no)
        val titleText: TextView = findViewById(R.id.txt_inquiry_title)
        val contentText: TextView = findViewById(R.id.txt_inquiry_content)
        val desiredate: TextView = findViewById(R.id.textDesiredDate)
        val creatdate : TextView = findViewById(R.id.textRecieptDate)
        val image : TextView = findViewById(R.id.textAttachment)


        serviceStatText.text = inquiry.CSR_STATUS
        noText.text = inquiry.REQ_SEQ.toString()
        titleText.text = inquiry.TITLE
        contentText.text = inquiry.CONTENT
        desiredate.text = inquiry.REQ_FINISH_DATE.substring(0,4)+"-"+inquiry.REQ_FINISH_DATE.substring(4,6)+"-"+inquiry.REQ_FINISH_DATE.substring(6)
        creatdate.text =inquiry.createdAt.substring(0,10)
        if (inquiry.REQ_IMG_PATH != "") {
            image.text = inquiry.REQ_IMG_PATH.substring(53)
        } else {
            image.text = "첨부파일 없음"
        }

        val imageBtn :Button = findViewById(R.id.btn_viewImage)
        imageBtn.setOnClickListener(this)

        // 버튼에 리스너를 설정한다 (권한 변수에 따라 다른 버튼을 대상으로)
        when (authState) {
            1 -> {
                val modifyButton: Button = findViewById(R.id.btn_modify)
                val deleteButton : Button = findViewById(R.id.btn_delete)
                modifyButton.setOnClickListener(this)
                deleteButton.setOnClickListener(this)
            }
            2 -> {
                when (inquiry.CSR_STATUS) {
                    "접수완료" -> {
                        val pickExpectedDateButton: ImageButton = findViewById(R.id.btnExpectedDate)
                        pickExpectedDateButton.setOnClickListener(this)

                        val startWorkButton: Button = findViewById(R.id.btn_start_work)
                        startWorkButton.setOnClickListener(this)
                    }
                    else -> {
                        val pickCompleteDateButton: ImageButton = findViewById(R.id.btnCompleteDate)
                        pickCompleteDateButton.setOnClickListener(this)

                        val finishWorkButton: Button = findViewById(R.id.btn_finish_work)
                        finishWorkButton.setOnClickListener(this)
                    }
                }
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
            R.id.btn_viewImage -> {
                // TODO : 더보기 버튼 -> 이미지 띄우기
                val intent = Intent(this, Image_Activity::class.java)
                intent.putExtra("ImagePath",inquiry.REQ_IMG_PATH)
                startActivity(intent)
            }

            R.id.btn_modify -> {
            // TODO : (수정) 버튼 -> detail 항목들 수정하기
                if(inquiry.CSR_STATUS == "접수대기"){
                    val intent = Intent(this, ReviseActivity::class.java)
                    intent.putExtra("inquiry",inquiry)
                    startActivity(intent)
                }
                else{
                    //Toast.makeText(applicationContext, "수정할 수 없는 상태입니다.",Toast.LENGTH_LONG).show()
                }

            }

            R.id.btn_delete -> {
                // TODO : (삭제) 버튼 -> 삭제하기
                UserApi.service.requestDelete("Bearer " + UserApi.ttt, inquiry.REQ_SEQ).enqueue(object : Callback<ResultMessage> {
                    override fun onResponse(call: Call<ResultMessage>, response: Response<ResultMessage>) {
                        if(response.isSuccessful){
                            val result=response.body()!!.resultCode
                            val message=response.body()!!.message

                            if(result==0){
                                if(result==0){
                                    //Toast.makeText(applicationContext,"성공" + message, Toast.LENGTH_SHORT).show()
                                    AlertDialog.Builder(this@DetailActivity)
                                            .setTitle("삭제")
                                            .setMessage("해당 요청은 삭제되었습니다.")
                                            .setPositiveButton("확인") { _: DialogInterface, _: Int -> startHomeActivity() }
                                            .show()
                                }
                                else {
                                    //Toast.makeText(applicationContext,"실패" + message, Toast.LENGTH_SHORT).show()
                                }
                            }
                            else {
                                //Toast.makeText(applicationContext,"실패" + message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            //Toast.makeText(applicationContext,"실패" , Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResultMessage>, t: Throwable) {
                        //Toast.makeText(applicationContext,"실패실패", Toast.LENGTH_LONG).show()
                        Log.e("failure errorrr", ""+t)
                    }
                })


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
                        vdate =SubmitObject.dateSet(year,month+1,dayOfMonth)
                    }
                }, year, month, date)
                dlg.show()
            }

            R.id.btnCompleteDate -> {
                val textCompleteDate : TextView = findViewById(R.id.textCompleteDate)
                val today = GregorianCalendar()
                val year: Int = today.get(Calendar.YEAR)
                val month: Int = today.get(Calendar.MONTH)
                val date: Int = today.get(Calendar.DATE)
                val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

                        textCompleteDate.setText("${year}/ ${month + 1}/ ${dayOfMonth}")
                        vdate =SubmitObject.dateSet(year,month+1,dayOfMonth)
                    }
                }, year, month, date)
                dlg.show()
            }
            R.id.btn_start_work -> {
            // 작업시작
                val textExpectedDate : TextView = findViewById(R.id.textExpectedDate)

                if(textExpectedDate.text == "") {
                    //Toast.makeText(applicationContext,"예상 완료일을 지정해 주세요." , Toast.LENGTH_SHORT).show()
                } else {

                    UserApi.service.WorkChangePut("Bearer " + UserApi.ttt, inquiry.REQ_SEQ, 0, "요청처리중", vdate).enqueue(object : Callback<ResultMessage> {
                        override fun onResponse(call: Call<ResultMessage>, response: Response<ResultMessage>) {
                            if(response.isSuccessful){
                                val result=response.body()!!.resultCode
                                val message=response.body()!!.message

                                if(result==0){
                                    //Toast.makeText(applicationContext,"성공 : " + textExpectedDate.text , Toast.LENGTH_SHORT).show()
                                    AlertDialog.Builder(this@DetailActivity)
                                            .setTitle("시작")
                                            .setMessage("해당 작업이 시작되었습니다")
                                            .setPositiveButton("확인") { _: DialogInterface, _: Int -> startHomeActivity() }
                                            .show()

                                }
                                else {
                                    //Toast.makeText(applicationContext,"실패" + message, Toast.LENGTH_SHORT).show()
                                }
                            }
                            else{
                                //Toast.makeText(applicationContext,"실패" , Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<ResultMessage>, t: Throwable) {
                            //Toast.makeText(applicationContext,"실패실패", Toast.LENGTH_LONG).show()
                            Log.e("failure errorrr", ""+t)
                        }
                    })
                }



            }

            R.id.btn_finish_work -> {
            // TODO : (작업 완료) 버튼 -> '처리완료' 상태로 변경
                val textCompleteText = findViewById<TextView>(R.id.textCompleteDate)

                if(textCompleteText.text == "") {
                    //Toast.makeText(applicationContext,"작업  완료일을 지정해 주세요." , Toast.LENGTH_SHORT).show()
                } else {
                    UserApi.service.WorkChangePut("Bearer " + UserApi.ttt, inquiry.REQ_SEQ, 1, "처리완료", vdate).enqueue(object : Callback<ResultMessage> {
                        override fun onResponse(call: Call<ResultMessage>, response: Response<ResultMessage>) {
                            if(response.isSuccessful){
                                val result=response.body()!!.resultCode
                                val message=response.body()!!.message

                                if(result==0){
                                    //Toast.makeText(applicationContext,"성공" + message, Toast.LENGTH_SHORT).show()
                                    AlertDialog.Builder(this@DetailActivity)
                                            .setTitle("완료")
                                            .setMessage("해당 작업이 [처리 완료] 상태로 변경되었습니다!")
                                            .setPositiveButton("확인") { _: DialogInterface, _: Int -> startHomeActivity() }
                                            .show()
                                }
                                else {
                                    //Toast.makeText(applicationContext,"실패" + message, Toast.LENGTH_SHORT).show()
                                }
                            }
                            else{
                                //Toast.makeText(applicationContext,"실패" , Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<ResultMessage>, t: Throwable) {
                            //.makeText(applicationContext,"실패실패", Toast.LENGTH_LONG).show()
                            Log.e("failure errorrr", ""+t)
                        }
                    })
                }




            }

            R.id.btn_approve -> {
            // TODO : (승인) 버튼 -> 요원목록 띄우고 고른 후 창 닫기

                startPersonnelActivity()

            }

            R.id.btn_dismiss -> {
            // TODO : (반려) 버튼 -> '요청반려' 상태로 변경
                UserApi.service.adminDenyPut("Bearer " + UserApi.ttt, inquiry.REQ_SEQ).enqueue(object : Callback<ResultMessage> {
                    override fun onResponse(call: Call<ResultMessage>, response: Response<ResultMessage>) {
                        if(response.isSuccessful){
                            val result=response.body()!!.resultCode
                            val message=response.body()!!.message

                            if(result==0){
                                //Toast.makeText(applicationContext,"성공" + message, Toast.LENGTH_SHORT).show()
                                AlertDialog.Builder(this@DetailActivity)
                                        .setTitle("반려")
                                        .setMessage("해당 접수는 반려되었습니다!")
                                        .setPositiveButton("확인") { _: DialogInterface, _: Int -> startHomeActivity() }
                                        .show()
                            }
                            else {
                                //Toast.makeText(applicationContext,"실패" + message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            //Toast.makeText(applicationContext,"실패" , Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResultMessage>, t: Throwable) {
                        //Toast.makeText(applicationContext,"실패실패", Toast.LENGTH_LONG).show()
                        Log.e("failure errorrr", ""+t)
                    }
                })


            }
        }
    }

    // PersonnelActivity 시작
    private fun startPersonnelActivity() {

        val intent = Intent(this, PersonnelActivity::class.java)

        intent.putExtra("dd", inquiry.REQ_SEQ)
        startActivity(intent)
    }

    private fun startHomeActivity() {

        val i = Intent(this, HomeActivity::class.java)
        i.flags =  Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)


    }
}

