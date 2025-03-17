package com.example.baith1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var helloText: TextView
    private lateinit var sayHiButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo các view
        helloText = findViewById(R.id.helloText)
        sayHiButton = findViewById(R.id.sayHiButton)

        // Khi nhấn nút Say Hi!
        sayHiButton.setOnClickListener {
            // Thay đổi nội dung của TextView helloText thành "I’m Tùng"
            helloText.text = "I’m Tùng"
        }
    }
}
