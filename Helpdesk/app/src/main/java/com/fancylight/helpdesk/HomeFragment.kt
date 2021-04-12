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

private const val ARG_AUTH_STATE = "com.fancylight.helpdesk.auth_state"


class HomeFragment : Fragment(), View.OnClickListener {

    // 유저 권한 변수 (1 사원, 2 요원, 3 관리자)
    private var authState: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 액티비티에서 전달받은 권한 변수 획득
        arguments?.let {
            authState = it.getInt(ARG_AUTH_STATE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        // 버튼에 클릭 리스너를 지정한다
        val seeNoticeButton = root.findViewById<ImageButton>(R.id.ibtn_see_notice)
        val quickOrderButton = root.findViewById<TextView>(R.id.txt_quick_order)
        val myListText = root.findViewById<TextView>(R.id.txt_my_list)
        seeNoticeButton.setOnClickListener(this)
        quickOrderButton.setOnClickListener(this)
        myListText.setOnClickListener(this)

        // 권한에 따라 나의 목록에 다른 이름을 준다
        myListText.text = when (authState) {
            1 -> "나의 요청목록"
            2 -> "나의 작업목록"
            3 -> "나의 결재목록"
            else -> "나의 요청목록"
        }

        val approvalnum = root.findViewById<TextView>(R.id.txt_num_approval)
        val waittingnum = root.findViewById<TextView>(R.id.txt_num_waiting)
        val completenum = root.findViewById<TextView>(R.id.txt_num_complete)
        val proceedingnum = root.findViewById<TextView>(R.id.txt_num_proceeding)
        val delaynum = root.findViewById<TextView>(R.id.txt_num_delay)

        UserApi.service.csrget().enqueue(object : Callback<CSR> {
            override fun onResponse(call: Call<CSR>, response: Response<CSR>) {
                if(response.isSuccessful){
                    //Toast.makeText(activity,"성공",Toast.LENGTH_LONG).show()
                    var sum : Int = response.body()!!.요청처리중 +
                            response.body()!!.접수대기 +
                            response.body()!!.접수완료 +
                            response.body()!!.처리지연중
                    approvalnum.setText(""+sum)
                    waittingnum.setText(""+response.body()!!.접수대기)
                    completenum.setText(""+response.body()!!.접수완료)
                    proceedingnum.setText(""+response.body()!!.요청처리중)
                    delaynum.setText(""+response.body()!!.처리지연중)

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
            R.id.txt_my_list -> listener?.myListClicked()
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
        fun myListClicked()
    }

    // 리스너 객체

    var listener : HomeFragmentListener? = null

    // 리스너를 설정하는 메소드. 이 메소드는 액티비티에서 프래그먼트를 감시하기 위해 호출한다

    fun setFragmentListener(listener: HomeFragmentListener) {
        this.listener = listener
    }

    companion object {

        // 액티비티로 권한 변수를 전달받는다
        @JvmStatic
        fun newInstance(authState: Int) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_AUTH_STATE, authState)
                }
            }
    }

}