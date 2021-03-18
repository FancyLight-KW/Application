package com.fancylight.helpdesk

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.fancylight.helpdesk.R
import com.fancylight.helpdesk.SubmitActivity


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