package com.sample.Helpdesk

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.fancylight.helpdesk.R
import java.util.*

class SubmitActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit)

        //기능들 여기다가 구현

        val spinner: Spinner = findViewById(R.id.spinner)
        val requestComplete: ImageButton = findViewById(R.id.btnRequestComplete)
        val textRequestComplete: TextView = findViewById(R.id.textRequestComplete)

        //spinnerArray.xml에 있는 systemName 가져옴
        var sdata = resources.getStringArray(R.array.systemName)

        //어댑터 만들고 연결
        //var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sdata)
        var adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, sdata)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                //스피너 동작 여기다 구현하면 됨
            }

        }

        //요청완료일 버튼 클릭시 달력으로 날짜 선택하는 기능 구현
        requestComplete.setOnClickListener {
            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)
            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    //요청완료일 기능 여기다 구현하면 됨
                    textRequestComplete.setText("${year}/ ${month + 1}/ ${dayOfMonth}")
                }
            }, year, month, date)
            dlg.show()
        }


    }
}