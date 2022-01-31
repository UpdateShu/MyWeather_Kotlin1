package com.geekbrains.myweather_kotlin1.view


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.model.City

class CitiesFragmentAdapter(private var onItemViewClickListener: OnCityItemViewClickListener?): RecyclerView.Adapter<CitiesFragmentAdapter.CitiesViewHolder>() {

    private var cities: List<City> = listOf()

    fun setCities(data: List<City>) {
        cities = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int) = CitiesViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_city, parent, false) as View)

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    override fun getItemCount(): Int = cities.size

    inner class CitiesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(city: City) {
            itemView.apply {
                findViewById<TextView>(R.id.city_name).text = city.name
                setOnClickListener {
                    onItemViewClickListener?.onCityItemViewClick(city)
                }
            }
        }
    }

    fun removeListener() {
        onItemViewClickListener = null
    }
}