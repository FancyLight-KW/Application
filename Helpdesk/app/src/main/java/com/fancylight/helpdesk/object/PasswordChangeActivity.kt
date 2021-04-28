package com.fancylight.helpdesk.`object`

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.fancylight.helpdesk.R
import com.fancylight.helpdesk.network.ChangePassword
import com.fancylight.helpdesk.network.JsonData
import com.fancylight.helpdesk.network.UserApi
import retrofit2.Response

class PasswordChangeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_change)

        val putButton = findViewById<Button>(R.id.btn_ChangPut)
        val editCurrent = findViewById<EditText>(R.id.edit_currentPassword)
        val editNew = findViewById<EditText>(R.id.edit_NewPassword)

        putButton.setOnClickListener{
            UserApi.service.passwordChangePut("Bearer "+ UserApi.ttt,editCurrent.text.toString(), editNew.text.toString()).enqueue(object : retrofit2.Callback<ChangePassword> {
                override fun onResponse(call: retrofit2.Call<ChangePassword>, response: Response<ChangePassword>) {
                    if(response.isSuccessful){
                        Toast.makeText(applicationContext,"성공", Toast.LENGTH_LONG).show()

                    }
                    else{
                        Toast.makeText(applicationContext,"실패", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: retrofit2.Call<ChangePassword>, t: Throwable) {
                    Toast.makeText(applicationContext,"실패실패", Toast.LENGTH_LONG).show()
                    Log.e("failure error", ""+t)
                }
            })
        }
    }
}