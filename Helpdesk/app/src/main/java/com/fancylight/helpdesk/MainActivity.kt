
package com.fancylight.helpdesk

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fancylight.helpdesk.SharedPref.MyApplication
import com.fancylight.helpdesk.`object`.MemberInfo
import com.fancylight.helpdesk.databinding.ActivityMainBinding
import com.fancylight.helpdesk.network.Fcm
import com.fancylight.helpdesk.network.Login
import com.fancylight.helpdesk.network.UserApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("fcmtokenfail", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("fcmtoken : ", msg)
            UserApi.fcmToken = msg
        })

        // 앱이 첫 실행될 때 표지를 잠깐 띄웠다가 fade out 한다.
        if (savedInstanceState == null) {
            fadeOutCover()
        }

        binding.editId.text=Editable.Factory.getInstance().newEditable(MyApplication.prefs.getString("id",""))
        binding.editPassword.text=Editable.Factory.getInstance().newEditable(MyApplication.prefs.getString("pw",""))

        // 버튼 리스너 설정
        val loginButton : Button? = findViewById<Button>(R.id.btn_login);
        loginButton?.setOnClickListener(this)
    }

    // 표지 레이아웃에 fade out 애니메이션 적용
    private fun fadeOutCover() {

        val cover : View? = findViewById<View>(R.id.cover)
        cover?.visibility = View.VISIBLE

        // fade out 애니메이션 로드 : 자체 정의한 fade out 리소스를 사용한다.
        // 리스너를 설정하여 애니메이션이 끝날 때 표지 레이아웃을 GONE 상태로 만든다.
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                cover?.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        // 3초간 보여주다가, fade out 애니메이션 적용
        cover?.postDelayed({ cover.startAnimation(fadeOut) }, 3000)
    }

    // 버튼 클릭 처리
    override fun onClick(v: View?) {

        if (v?.id == R.id.btn_login) {
            startLogin(binding.editId.text.toString(), binding.editPassword.text.toString())
        }
}
    // HomeActivity 를 시작한다
    private fun startHomeActivity() {

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    public fun startLogin(Input_id:String, Input_password :String) {

       UserApi.service.loginPost(Input_id,Input_password).enqueue(object : Callback<Login> {
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                if(response.isSuccessful){
                val result=response.body()!!.resultCode
                    if(result == 0){
                        MyApplication.prefs.setString("id",Input_id)
                        MyApplication.prefs.setString("pw",Input_password)

                        UserApi.ttt = response.body()!!.token
                        Log.d("ttt",UserApi.extractJwt(UserApi.ttt))
                        MemberInfo.infoSet(UserApi.extractJwt(UserApi.ttt))

                        UserApi.service.FcmPost("Bearer " +UserApi.ttt,UserApi.fcmToken).enqueue(object : Callback<Fcm> {
                            override fun onResponse(call: Call<Fcm>, response: Response<Fcm>) {
                                if(response.isSuccessful){
                                    val result=response.body()!!.resultCode
                                    Toast.makeText(applicationContext,"성공"+UserApi.fcmToken, Toast.LENGTH_SHORT).show()
                                    startHomeActivity()
                                }
                                else{
                                    Toast.makeText(applicationContext,"실패" +UserApi.fcmToken, Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onFailure(call: Call<Fcm>, t: Throwable) {
                                Toast.makeText(applicationContext,"실패실패", Toast.LENGTH_LONG).show()
                                Log.e("failure errorrr", ""+t)
                            }
                        })
                    }
                    else {
                        Toast.makeText(applicationContext,"등록된 정보와 일치하지 않습니다.",Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(applicationContext,"실패zz", Toast.LENGTH_SHORT).show()
                }
            }
           override fun onFailure(call: Call<Login>, t: Throwable) {
               Toast.makeText(applicationContext,"실패실패zz", Toast.LENGTH_LONG).show()
               Log.e("failure error", ""+t)
           }
        })

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "HT_Helpdesk"
            val channelId = "${applicationContext.packageName}-${name}"
            val descriptionText = "HT_Helpdesk_Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}