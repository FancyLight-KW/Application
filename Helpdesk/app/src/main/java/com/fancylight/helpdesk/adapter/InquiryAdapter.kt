package com.fancylight.helpdesk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fancylight.helpdesk.R
import com.fancylight.helpdesk.model.*
import java.util.*

class InquiryAdapter(
        private val context: Context,
        private val inquiryList: List<Inquiry>
) : RecyclerView.Adapter<InquiryAdapter.ViewHolder>() {

    // 사용자가 보여주기를 원하는 아이템의 개수
    // (더보기 버튼 클릭 시, 이 개수를 늘려서 보여지는 아이템을 추가)
    var inquirySize = inquiryList.size
        set(newSize) {
            if (newSize >= 0) {
                val maxSize = inquiryList.size
                field = if (newSize > maxSize) maxSize else newSize
            }
        }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        // 아이템뷰의 위젯 획득
        private val numberText: TextView = itemView.findViewById(R.id.txt_inquiry_number)
        private val serviceStatText: TextView = itemView.findViewById(R.id.txt_service_stat)
        private val typeText: TextView = itemView.findViewById(R.id.txt_inquiry_type)
        private val titleText: TextView = itemView.findViewById(R.id.txt_inquiry_title)
        private val dateText: TextView = itemView.findViewById(R.id.txt_inquiry_date)

        fun bind(model: Inquiry) {

            // 각 위젯에 데이터 입력
            numberText.text = model.id.toString()
            serviceStatText.text = model.serviceStat;
            titleText.text = model.title

            val strDate = String.format(Locale.getDefault(),
                    "%d/%02d/%02d", model.date.year, model.date.monthValue, model.date.dayOfMonth)
            dateText.text = strDate

            typeText.text = when(model.type) {
                // 영문도 함께 정의된 문자열 리소스(R.string.~) 를 리턴하는것이 원칙
                INQUIRY_TYPE_CHANGE -> "기능 변경/수정"
                INQUIRY_TYPE_NORMAL -> "단순문의"
                INQUIRY_TYPE_DEBUG -> "장애처리"
                INQUIRY_TYPE_ETC -> "기타"
                else -> "-"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        // 아이템뷰 생성
        val itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_inquiry, parent, false)

        // 뷰홀더 생성, 리턴
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // 아이템뷰의 위젯에 데이터 입력
        holder.bind(inquiryList[position])
    }

    override fun getItemCount(): Int {

        // 모든 아이템이 아닌, 사용자가 원하는 개수만큼의 아이템만 보여줘야 하므로
        // 아래 변수를 리턴해야 함
        return inquirySize
    }
}