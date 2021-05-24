package com.fancylight.helpdesk

import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fancylight.helpdesk.adapter.PersonnelAdapter
import com.fancylight.helpdesk.model.Personnel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.fancylight.helpdesk.adapter.InquiryAdapter
import com.fancylight.helpdesk.model.*
import com.fancylight.helpdesk.network.AgentListForm
import com.fancylight.helpdesk.network.JsonData
import com.fancylight.helpdesk.network.UserApi
import com.fancylight.helpdesk.network.getRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.FieldPosition
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class PersonnelActivity : AppCompatActivity() {

    private var personnelSource = mutableListOf<Personnel>()

    private var resultList: MutableList<Personnel>? = null

    private var personnelRecycler: RecyclerView? = null
    private var personnelAdapter: PersonnelAdapter? = null

    private var seqNum: Int = 0

    override fun onCreate(
            savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personnel)





        UserApi.service.agentListGet("Bearer " + UserApi.ttt).enqueue(object :
                retrofit2.Callback<Array<AgentListForm>> {
            override fun onResponse(call: retrofit2.Call<Array<AgentListForm>>, response: Response<Array<AgentListForm>>) {

                if(response.isSuccessful){
                    personnelSource = mutableListOf<Personnel>()
                    var arr = response.body()!!

                    for(i in 0..arr.size-1) {
                        personnelSource.add(Personnel(arr[i].DOING, arr[i].READY, arr[i].User_id, arr[i].User_name))
                    }
                    //Toast.makeText(applicationContext,"성공", Toast.LENGTH_SHORT).show()
                    resultList = personnelSource.toMutableList()

                    buildPersonnelRecycler()



                }
                else{
                    //.makeText(applicationContext,"실패"+ response.code(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<Array<AgentListForm>>, t: Throwable) {
                //Toast.makeText(applicationContext,"실패실패", Toast.LENGTH_LONG).show()
                Log.e("failure error", ""+t)
            }
        })

        return
    }


    private fun buildPersonnelRecycler() {

        personnelRecycler = findViewById(R.id.recycler_personnel)

        if (personnelRecycler != null) {
            personnelRecycler?.setHasFixedSize(true)
            personnelRecycler?.layoutManager = LinearLayoutManager(this)

            personnelAdapter = PersonnelAdapter(this, resultList?.toList()!!)

            personnelRecycler?.adapter = personnelAdapter

            personnelAdapter?.setOnItemClickListener(object : PersonnelAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    allocateAgent(position)


                }

            })
        }

    }

    private fun allocateAgent(position: Int) {
        seqNum = intent.getIntExtra("dd", seqNum)


        UserApi.service.adminPut("Bearer " + UserApi.ttt, resultList?.get(position)?.User_id!!, seqNum).enqueue(object : Callback<JsonData> {
            override fun onResponse(call: Call<JsonData>, response: Response<JsonData>) {
                if(response.isSuccessful){
                        //.makeText(applicationContext,"성공allocate" , Toast.LENGTH_SHORT).show()
                    AlertDialog.Builder(this@PersonnelActivity)
                            .setTitle("할당")
                            .setMessage("해당 요원으로 할당되었습니다!")
                            .setPositiveButton("확인") { _: DialogInterface, _: Int -> startHomeActivity() }
                            .show()
                }
                else{
                    //Toast.makeText(applicationContext,"실패" , Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<JsonData>, t: Throwable) {
                //Toast.makeText(applicationContext,"실패실패", Toast.LENGTH_LONG).show()
                Log.e("failure errorrr", ""+t)
            }
        })

    }

    private fun startHomeActivity() {

        val i = Intent(this, HomeActivity::class.java)
        i.flags =  Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)
    }


}