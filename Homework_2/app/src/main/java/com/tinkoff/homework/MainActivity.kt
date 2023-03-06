package com.tinkoff.homework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tinkoff.homework.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val flexboxFactory = FlexboxFactory(33, this)
        flexboxFactory.create().forEach { view ->
            binding.flexbox.addView(view)
            view.setOnClickListener {
                it.isSelected = !it.isSelected
            }
        }
    }
}