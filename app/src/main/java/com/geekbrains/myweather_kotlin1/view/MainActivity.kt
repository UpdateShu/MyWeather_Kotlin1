package com.geekbrains.myweather_kotlin1.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.databinding.MainActivityBinding
import com.geekbrains.myweather_kotlin1.presentation.ThreadsFragment
import com.geekbrains.myweather_kotlin1.presentation.contacts.ContactsFragment
import com.geekbrains.myweather_kotlin1.presentation.history.FilterHistoryFragment
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
        binding.btnSettings.setOnClickListener(this@MainActivity)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {

        val fragment = when (v?.id) {
            R.id.btnCities -> CitiesFragment()
            R.id.btnWeather -> WeatherFragment()
            R.id.btnSettings -> ThreadsFragment()
            else -> { }
        } as Fragment
        showFragment(fragment)
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, "")
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                showFragment(FilterHistoryFragment())
                true
            }
            R.id.menu_contacts -> {
                showFragment(ContactsFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}