package com.fancylight.helpdesk

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.util.*

class AR : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //기능들 여기다가 구현
        val view = inflater.inflate(R.layout.fragment_a_r, null)
        val spinner: Spinner = view.findViewById(R.id.spinner)
        val requestComplete: ImageButton = view.findViewById(R.id.btnRequestComplete)
        val textRequestComplete: TextView = view.findViewById(R.id.textRequestComplete)

        //spinnerArray.xml에 있는 systemName 가져옴
        var sdata = resources.getStringArray(R.array.systemName)

        //어댑터 만들고 연결
        //var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sdata)
        var adapter = ArrayAdapter<String>(requireActivity(),android.R.layout.simple_list_item_1, sdata)
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
            val dlg = DatePickerDialog(requireContext(), object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    //요청완료일 기능 여기다 구현하면 됨
                    textRequestComplete.setText("${year}/ ${month + 1}/ ${dayOfMonth}")
                }
            }, year, month, date)
            dlg.show()
        }
        return view
    }

}
