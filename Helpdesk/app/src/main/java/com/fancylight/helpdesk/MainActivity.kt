
package com.fancylight.helpdesk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fancylight.helpdesk.databinding.ActivityMainBinding
import com.fancylight.helpdesk.network.Login
import com.fancylight.helpdesk.network.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 앱이 첫 실행될 때 표지를 잠깐 띄웠다가 fade out 한다.
        if (savedInstanceState == null) {
            fadeOutCover()
        }

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

       UserApi.service.testPost(Input_id,Input_password).enqueue(object : Callback<Login> {
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                if(response.isSuccessful){
                    //Toast.makeText(applicationContext,"성공"+response.body()!!.resultCode, Toast.LENGTH_SHORT).show()
                    val result=response.body()!!.resultCode
                    if(result == 0){
                        startHomeActivity()
                    }
                    else {
                        Toast.makeText(applicationContext,"등록된 정보와 일치하지 않습니다.",Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    //Toast.makeText(applicationContext,"실패", Toast.LENGTH_SHORT).show()
                }
            }
           override fun onFailure(call: Call<Login>, t: Throwable) {
               //Toast.makeText(applicationContext,"실패실패", Toast.LENGTH_SHORT).show()
           }
        })

    }

}