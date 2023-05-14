package com.example.ixltask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.ixltask.R
import com.example.ixltask.data.local.IxlDatabase
import com.example.ixltask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val database = IxlDatabase.getInstance(this.applicationContext)
        viewModel = ViewModelProvider(
            this,
            MainViewModel.getViewModelFactory(database)
        )[MainViewModel::class.java]
    }
}