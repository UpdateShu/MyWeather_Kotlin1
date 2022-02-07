package com.geekbrains.myweather_kotlin1.view

import android.os.Build
import com.geekbrains.myweather_kotlin1.presentation.ThreadsFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.databinding.MainActivityBinding
import com.geekbrains.myweather_kotlin1.presentation.history.HistoryFragment
import com.geekbrains.myweather_kotlin1.presentation.weather.WeatherFragment

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: MainActivityBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherFragment().also { it.arguments = Bundle() })
                .commit()
        }
        binding.btnCities.setOnClickListener(this@MainActivity)
        binding.btnWeather.setOnClickListener(this@MainActivity)
        binding.btnHistory.setOnClickListener(this@MainActivity)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {

        val fragment = when (v?.id) {
            R.id.btnCities -> CitiesFragment()
            R.id.btnWeather -> WeatherFragment()
            R.id.btnHistory -> HistoryFragment()
            else -> { }
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment as Fragment, "")
            .commit()
    }
}