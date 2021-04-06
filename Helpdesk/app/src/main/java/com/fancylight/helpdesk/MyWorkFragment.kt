package com.fancylight.helpdesk

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fancylight.helpdesk.adapter.InquiryAdapter
import com.fancylight.helpdesk.model.*
import com.fancylight.helpdesk.network.UserApi
import com.fancylight.helpdesk.network.getRequest
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MyWorkFragment : Fragment(), View.OnClickListener {

    // 모든 문의 데이터
    private var inquirySource = mutableListOf<Inquiry>()

    // 분류기준에 맞춰 검색된 문의 데이터
    private var resultList: MutableList<Inquiry>? = null

    // 문의 데이터를 표시하는 리사이클러뷰 / 어댑터
    private var inquiryRecycler: RecyclerView? = null
    private var inquiryAdapter: InquiryAdapter? = null

    // 분류 기준 위젯
    private var serviceStatSpinner: Spinner? = null
    private var inquiryTypeSpinner: Spinner? = null
    private var inquiryTitleEdit: EditText? = null
    private var startDateText: TextView? = null
    private var endDateText: TextView? = null
    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null

    // 스피너 요소
    private var serviceStatItems = arrayOf(
        "-", SERVICE_STAT_A, SERVICE_STAT_B, SERVICE_STAT_C, SERVICE_STAT_D
    )
    private var inquiryTypeItems = arrayOf(
        0, INQUIRY_TYPE_CHANGE, INQUIRY_TYPE_NORMAL, INQUIRY_TYPE_DEBUG, INQUIRY_TYPE_ETC
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_my_work, container, false)

        // 버튼에 리스너 설정
        val submtBtn = view.findViewById<Button>(R.id.btn_submit)
        val searchBtn = view.findViewById<Button>(R.id.btn_search)
        val seeMoreBtn = view.findViewById<Button>(R.id.btn_more_table)
        val serviceStatBtn = view.findViewById<ImageButton>(R.id.ibtn_service_stat)
        val inquiryTypeBtn = view.findViewById<ImageButton>(R.id.ibtn_inquiry_type)
        val startDateBtn = view.findViewById<ImageButton>(R.id.btn_date_start)
        val endDateBtn = view.findViewById<ImageButton>(R.id.btn_date_end)
        submtBtn.setOnClickListener(this)
        searchBtn.setOnClickListener(this)
        seeMoreBtn.setOnClickListener(this)
        serviceStatBtn.setOnClickListener(this)
        inquiryTypeBtn.setOnClickListener(this)
        startDateBtn.setOnClickListener(this)
        endDateBtn.setOnClickListener(this)

        // 분류 기준 위젯
        serviceStatSpinner = view.findViewById(R.id.spinner_service_stat)
        inquiryTypeSpinner = view.findViewById(R.id.spinner_inquiry_type)
        inquiryTitleEdit = view.findViewById(R.id.edit_inquiry_title)
        startDateText = view.findViewById(R.id.txt_date_start)
        endDateText = view.findViewById(R.id.txt_date_end)

        // 날짜 텍스트뷰는 클릭시 대화상자가 뜨도록 클릭 리스너 설정
        startDateText?.setOnClickListener(this)
        endDateText?.setOnClickListener(this)

        // 스피너 초기화
        // - 서비스 상태 스피너 초기화
        serviceStatSpinner?.adapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_dropdown_item,
            serviceStatItems
        )
        // - 문의 유형 스피너 초기화
        inquiryTypeSpinner?.adapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_dropdown_item,
            // 문의 유형 상수값을 그에 맞는 문자열로 치환한 배열을 map() 메소드로 얻기
            inquiryTypeItems.map { type -> when (type) {
                INQUIRY_TYPE_CHANGE -> "기능 변경/수정"
                INQUIRY_TYPE_NORMAL -> "단순문의"
                INQUIRY_TYPE_DEBUG -> "장애처리"
                INQUIRY_TYPE_ETC -> "기타"
                else -> "-"
            }
            }
        )

        UserApi.service.testGet("Bearer "+ UserApi.ttt).enqueue(object :
            retrofit2.Callback<Array<getRequest>> {
            override fun onResponse(call: retrofit2.Call<Array<getRequest>>, response: Response<Array<getRequest>>) {
                if(response.isSuccessful){
                    var arr = response.body()!!
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                    for(i in 0..arr.size-1){
                        inquirySource.add(Inquiry(i, arr[i].CSR_STATUS, 4, arr[i].TITLE,
                            LocalDate.parse(arr[i].createdAt.substring(0,10), DateTimeFormatter.ISO_DATE)))
                    }

                    // 결과 리스트 구성. 처음엔 검색 필터 미적용 (= 모든 글 추가)
                    resultList = filterInquiries(null, null, null, null, null)

                    // 리사이클러뷰 빌드
                    buildInquiryRecycler(view)

                }
                else{
                    //Toast.makeText(applicationContext,"실패"+ UserApi.ttt, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: retrofit2.Call<Array<getRequest>>, t: Throwable) {
                //Toast.makeText(applicationContext,"실패실패", Toast.LENGTH_LONG).show()
                Log.e("failure error", ""+t)
            }
        })

        return view
    }


    // 리사이클러뷰 빌드하기

    private fun buildInquiryRecycler(view: View) {

        inquiryRecycler = view.findViewById(R.id.recycler_inquiry)

        if (inquiryRecycler != null) {
            // 리사이클러뷰 속성 초기화
            inquiryRecycler?.setHasFixedSize(true)
            inquiryRecycler?.layoutManager = LinearLayoutManager(context)

            // 어댑터 생성 및 연결
            inquiryAdapter = InquiryAdapter(context!!, resultList!!)
            inquiryAdapter?.inquirySize = 10
            inquiryRecycler?.adapter = inquiryAdapter
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            // Help 버튼 클릭 : NewActivity 시작
            R.id.btn_submit -> startSubmitActivity()
            // 검색 버튼 클릭 : 검색해서 결과 보여주기
            R.id.btn_search -> searchInquiries()
            // 더보기 버튼 클릭 : 리사이클러뷰에 보이는 아이템 수 증가
            R.id.btn_more_table -> showMoreInquiries()
            // 기간 날짜 버튼/텍스트뷰 클릭 : 유저에게서 기간을 입력받는다
            R.id.btn_date_start, R.id.txt_date_start -> promptPeriodDate(true)
            R.id.btn_date_end, R.id.txt_date_end -> promptPeriodDate(false)
            // 스피너 옆 버튼 클릭시 스피너가 클릭된 것으로 처리하기
            R.id.ibtn_service_stat -> serviceStatSpinner?.performClick()
            R.id.ibtn_inquiry_type -> inquiryTypeSpinner?.performClick()
        }
    }

    private fun startSubmitActivity() {

        val intent = Intent(context, SubmitActivity::class.java)
        startActivity(intent)
    }

    // 리사이클러뷰에 보이는 아이템 수 증가

    private fun showMoreInquiries() {

        // let 이하 블록은 inquiryAdapter 가 null 이 아닐때 실행됨
        inquiryAdapter?.let { // inquiryAdapter 가 it 으로 참조됨 (let it be)
            if (it.inquirySize < resultList!!.size) {
                it.inquirySize += 10        // 10개만큼 더 보여주기
                it.notifyDataSetChanged()   // 리사이클러뷰에게 다시 그리기 요청
                inquiryRecycler?.scrollToPosition(it.inquirySize - 1) // 한칸 밑으로 이동
            } else {
                Toast.makeText(context, "추가할 글이 없습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 주어진 조건으로 문의 검색하고 보여주기

    private fun searchInquiries() {

        // 위젯에서 필터값 받기
        val fruitQualityPos = serviceStatSpinner!!.selectedItemPosition
        val inquiryTypePos = inquiryTypeSpinner!!.selectedItemPosition

        val fruitQuality = if (fruitQualityPos > 0) serviceStatItems[fruitQualityPos] else null
        val inquiryType = if (inquiryTypePos > 0) inquiryTypeItems[inquiryTypePos] else null
        val title: String? = inquiryTitleEdit?.text?.toString()

        // 필터로 검색한 결과를 검색 결과값으로 설정
        resultList?.clear()
        resultList?.addAll(filterInquiries(fruitQuality, inquiryType, title, startDate, endDate))

        // 리사이클러뷰 업데이트
        inquiryAdapter?.inquirySize = 10    // 보여줄 사이즈는 10으로 초기화
        inquiryAdapter?.notifyDataSetChanged()

        if (resultList!!.isEmpty()) {
            Toast.makeText(context, "검색 결과가 없습니다", Toast.LENGTH_SHORT).show()
        }
    }

    // 필터로 문의 검색한 결과 리턴

    private fun filterInquiries(fruitQuality: String?, inquiryType: Int?, title: String?,
                                startDate: LocalDate?, endDate: LocalDate?)
            : MutableList<Inquiry> {

        // 5개의 인수는 필터 역할을 함. null 이면 필터로 동작하지 않음
        // filter() 메소드는 필터에 통과된 문의로만 새로운 리스트를 구성해 결과값으로 리턴
        return inquirySource.filter {
            when {
                (fruitQuality != null && it.serviceStat != fruitQuality) -> false
                (inquiryType != null && it.type != inquiryType) -> false
                (!title.isNullOrBlank() && !it.title.contains(title, true)) -> false
                (startDate != null && it.date.isBefore(startDate)) -> false
                (endDate != null && it.date.isAfter(endDate)) -> false
                else -> true    // 5개의 필터 모두 통과
            }
        }.sortedByDescending {
            it.id   // 필터링된 결과를 번호(No) 내림차순으로 정렬
        }.toMutableList()
    }

    // 대화상자로 유저에게 기간의 시작날짜 또는 종료날짜를 입력받는다.
    // startOrEnd 가 true 이면 시작날짜를, false 이면 종료날짜를 입력받는다

    private fun promptPeriodDate(startOrEnd: Boolean) {

        // 요청완료일 버튼 클릭시 달력으로 날짜 선택하는 기능 구현
        // LocalDate 는 Month 를 1부터 세지만 (January=1)
        // Calendar 와 DatePicker 는 0부터 세는것 (January=0) 에 주의
        val initialDate = (if (startOrEnd) startDate else endDate) ?: LocalDate.now()

        val dlg = DatePickerDialog(context!!,
            { _, year, month, dayOfMonth -> //
                val strDate = String.format(Locale.getDefault(),
                    "%d/%02d/%02d",year, month + 1, dayOfMonth)
                if (startOrEnd) {
                    startDateText?.text = strDate
                    startDate = LocalDate.of(year, month + 1, dayOfMonth)
                } else {
                    endDateText?.text = strDate
                    endDate = LocalDate.of(year, month + 1, dayOfMonth)
                }
            }, initialDate.year, initialDate.monthValue - 1, initialDate.dayOfMonth)

        dlg.show()
    }

}
