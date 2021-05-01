package com.fancylight.helpdesk

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import com.fancylight.helpdesk.model.Inquiry
import com.fancylight.helpdesk.`object`.MemberInfo
import com.fancylight.helpdesk.`object`.SubmitObject
import java.util.*


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


        serviceStatText.text = inquiry.serviceStat
        noText.text = "$inquiryNo"
        titleText.text = inquiry.title
        contentText.text = inquiry.content

        // 버튼에 리스너를 설정한다 (권한 변수에 따라 다른 버튼을 대상으로)
        when (authState) {
            1 -> {
                val modifyButton: Button = findViewById(R.id.btn_modify)
                modifyButton.setOnClickListener(this)
            }
            2 -> {
                val startWorkButton: Button = findViewById(R.id.btn_start_work)
                val finishWorkButton: Button = findViewById(R.id.btn_finish_work)
                startWorkButton.setOnClickListener(this)
                finishWorkButton.setOnClickListener(this)
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
            // TODO : (수정) 버튼
            }
            R.id.btn_start_work -> {
            // TODO : (작업 시작) 버튼
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
            R.id.btn_finish_work -> {
            // TODO : (작업 완료) 버튼


            }

            R.id.btn_approve -> {
            // TODO : (승인) 버튼 -> 요원목록 띄우고 고른 후 창 닫기


            }
            R.id.btn_dismiss -> {
            // TODO : (반려) 버튼 -> '요청반려' 상태로 변경
            }
        }
    }
}

