package com.fancylight.helpdesk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.fancylight.helpdesk.R
import com.fancylight.helpdesk.SubmitActivity
import com.fancylight.helpdesk.network.CSR
import com.fancylight.helpdesk.network.Login
import com.fancylight.helpdesk.network.UserApi
import com.fancylight.helpdesk.network.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        // 버튼에 클릭 리스너를 지정한다
        val seeNoticeButton = root.findViewById<ImageButton>(R.id.ibtn_see_notice)
        val quickOrderButton = root.findViewById<TextView>(R.id.txt_quick_order)
        seeNoticeButton.setOnClickListener(this)
        quickOrderButton.setOnClickListener(this)

        val approvalnum = root.findViewById<TextView>(R.id.txt_num_approval)
        val waittingnum = root.findViewById<TextView>(R.id.txt_num_waiting)
        val completenum = root.findViewById<TextView>(R.id.txt_num_complete)
        val proceedingnum = root.findViewById<TextView>(R.id.txt_num_proceeding)

        UserApi.service.csrget().enqueue(object : Callback<CSR> {
            override fun onResponse(call: Call<CSR>, response: Response<CSR>) {
                if(response.isSuccessful){
                    Toast.makeText(activity,"성공",Toast.LENGTH_LONG).show()
                    approvalnum.setText(""+response.body()!!.CSR진행상태)
                    waittingnum.setText(""+response.body()!!.접수)
                    completenum.setText(""+response.body()!!.완료)
                    proceedingnum.setText(""+response.body()!!.진행)

                }
                else{
                    Toast.makeText(activity,"실패",Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<CSR>, t: Throwable) {
                Toast.makeText(activity,"실패실패",Toast.LENGTH_LONG).show()
                Log.e("failure error", ""+t)
            }
        })



        return root
    }

    // 버튼 클릭을 처리한다

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.ibtn_see_notice -> listener?.seeNoticeButtonClicked()
            R.id.txt_quick_order -> startSubmitActivity()
        }
    }

    // SubmitActivity 시작

    private fun startSubmitActivity() {

        val intent = Intent(context, SubmitActivity::class.java)
        startActivity(intent)
    }

    // 이 프래그먼트에서 일어나는 이벤트를 처리할 리스너 타입

    interface HomeFragmentListener {
        fun seeNoticeButtonClicked()
    }

    // 리스너 객체

    var listener : HomeFragmentListener? = null

    // 리스너를 설정하는 메소드. 이 메소드는 액티비티에서 프래그먼트를 감시하기 위해 호출한다

    fun setFragmentListener(listener: HomeFragmentListener) {
        this.listener = listener
    }

}