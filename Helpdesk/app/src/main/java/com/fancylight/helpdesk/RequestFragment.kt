package com.sample.Helpdesk

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.fancylight.helpdesk.R
import com.sample.Helpdesk.SubmitActivity
import java.util.*

class RequestFragment : Fragment(), View.OnClickListener {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_request, null)

        return view
    }


    override fun onClick(v: View?) {

        if (v?.id == R.id.btn_submit) {
            startSubmitActivity()
        }
    }


    private fun startSubmitActivity() {

        val intent = Intent(context, SubmitActivity::class.java)
        startActivity(intent)
    }

}
