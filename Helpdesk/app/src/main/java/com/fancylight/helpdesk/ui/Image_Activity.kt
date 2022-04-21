package com.fancylight.helpdesk.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.fancylight.helpdesk.R

class Image_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_)

        val image : ImageView = findViewById(R.id.image)
        val intent = intent

        val imagepath = intent.getStringExtra("ImagePath")

        Glide.with(this).load(imagepath)
                                .error(R.drawable.error_image)
                                .into(image)


    }
}