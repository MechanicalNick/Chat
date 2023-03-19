package com.tinkoff.homework.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        setContentView(binding.root)
    }
}