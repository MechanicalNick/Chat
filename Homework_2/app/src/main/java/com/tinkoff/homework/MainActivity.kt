package com.tinkoff.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tinkoff.homework.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reactionView.setOnClickListener {
            it.isSelected = !it.isSelected
        }
    }
}