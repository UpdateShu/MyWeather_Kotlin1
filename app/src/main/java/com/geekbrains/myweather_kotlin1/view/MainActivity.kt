package com.geekbrains.myweather_kotlin1.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.databinding.MainActivityBinding
import com.geekbrains.myweather_kotlin1.model.City

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherFragment.newInstance(Bundle()))
                .commit()
        }
        binding.btnCities.setOnClickListener(this@MainActivity)
        binding.btnWeather.setOnClickListener(this@MainActivity)
        binding.btnSettings.setOnClickListener(this@MainActivity)
    }

    override fun onClick(v: View?) {

        val fragment = when (v?.id) {
            R.id.btnCities -> CitiesFragment()
            R.id.btnWeather -> WeatherFragment()
            R.id.btnSettings -> SettingsFragment()
            else -> { }
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment as Fragment, "")
            .commit()
    }
}