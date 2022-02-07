package com.geekbrains.myweather_kotlin1.presentation.cities


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.view.OnCityItemViewClickListener
import org.w3c.dom.Text

class CitiesFragmentAdapter(private var onItemViewClickListener: OnCityItemViewClickListener?): RecyclerView.Adapter<CitiesFragmentAdapter.CitiesViewHolder>() {

    public var editor: Boolean = false
        private set(value) {
            field = value
        }
        get() = field

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
                setOnClickListener {
                    if (!editor) { onItemViewClickListener?.onCityItemViewClick(city) }
                }
                setOnLongClickListener({
                    editor = !editor;
                    notifyDataSetChanged()
                    true })
            }
            changeCityStyle(itemView, city)
            itemView.findViewById<CheckBox>(R.id.check_city).apply {
                visibility = if (editor) View.VISIBLE else View.INVISIBLE
                isChecked = city.isChecked
                text = city.name
                setOnCheckedChangeListener { chb, isChecked ->
                    city.isChecked = chb.isChecked
                    changeCityStyle(itemView, city)
                    Toast.makeText(context, "${city.name}, ${chb.text}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun changeCityStyle(view: View, city : City) {
        view.findViewById<TextView>(R.id.city_name).apply {
            text = city.name
            setTextAppearance(if (city.isChecked) {R.style.CheckedCityStyle} else {R.style.CityStyle})
        }
    }

    fun removeListener() {
        onItemViewClickListener = null
    }
}