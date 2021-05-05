package com.fancylight.helpdesk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Response
import com.fancylight.helpdesk.network.AgentListForm
import com.fancylight.helpdesk.network.UserApi
import com.fancylight.helpdesk.network.getRequest
import com.fancylight.helpdesk.R
import com.fancylight.helpdesk.`object`.App
import com.fancylight.helpdesk.adapter.InquiryAdapter
import com.fancylight.helpdesk.adapter.PersonnelAdapter
import com.fancylight.helpdesk.model.Personnel


class PersonnelActivity : AppCompatActivity() {



    private lateinit var application: App

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personnel)

        application = getApplication() as App

        buildPersonnelRecycler()
    }

    // 요원 리사이클러뷰 초기화

    private fun buildPersonnelRecycler() {

        val recycler: RecyclerView = findViewById(R.id.recycler_personnel)

        // 리사이클러뷰 속성 초기화
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)

        // 어댑터 생성 및 연결
        val adapter = PersonnelAdapter(this, application.getPersonnelList())
        recycler.adapter = adapter

        adapter.setOnItemClickListener(object : PersonnelAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // 배정하고 상태바꾸고 액티비티 종료




                finish()
            }
        })
    }

}