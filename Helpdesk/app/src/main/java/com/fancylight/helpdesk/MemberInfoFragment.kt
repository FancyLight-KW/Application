package com.fancylight.helpdesk

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class MemberInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_member_info, container, false)
        val btnChange = view.findViewById<Button>(R.id.btn_changePassword)
        btnChange.setOnClickListener{
            val intent = Intent(activity, PasswordChangeActivity::class.java)
            startActivity(intent)
        }
        return view
    }


}