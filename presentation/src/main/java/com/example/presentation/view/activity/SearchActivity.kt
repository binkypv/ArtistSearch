package com.example.presentation.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.presentation.R
import com.example.presentation.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private var binding: ActivitySearchBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_search
        )
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}