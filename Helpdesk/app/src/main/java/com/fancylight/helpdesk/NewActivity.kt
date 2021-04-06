

package com.fancylight.helpdesk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        // 챗봇 프래그먼트 삽입
        supportFragmentManager.beginTransaction()
            .add(R.id.frag_container, ChatBotFragment())
            .commit()
        
    }
}