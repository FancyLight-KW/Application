package com.fancylight.helpdesk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class Image_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_)

        val image : ImageView = findViewById(R.id.image)

        val intent = intent
        val imagepath = intent.getStringExtra("ImagePath")


    }
}