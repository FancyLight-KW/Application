package com.fancylight.helpdesk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

class RequestFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_request, null)
        val submitBtn = view.findViewById<Button>(R.id.btn_submit)
        submitBtn.setOnClickListener {
            startSubmitActivity()
        }

        return view
    }

    private fun startSubmitActivity() {
            activity?.let {
                val intent = Intent(context,SubmitActivity::class.java)
                startActivity(intent)
            }
    }
}