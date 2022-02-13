package com.geekbrains.myweather_kotlin1.presentation.cities


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.model.City
import kotlinx.android.synthetic.main.threads_fragment.view.*

class CitiesFragmentAdapter(private var editMode: Boolean, private var itemResourceId: Int,
                            private var onItemViewClickListener: OnCityItemViewClickListener?
): RecyclerView.Adapter<CitiesFragmentAdapter.CitiesViewHolder>() {

    private var cities: List<City> = listOf()

    fun setCities(data: List<City>) {
        cities = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int) = CitiesViewHolder(LayoutInflater.from(parent.context)
                .inflate(itemResourceId, parent, false) as View)

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    override fun getItemCount(): Int = cities.size

    inner class CitiesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(city: City) {
            if (!editMode) {
                itemView.apply {
                    setOnClickListener {
                        onItemViewClickListener?.onCityItemViewClick(city)
                        Toast.makeText(context, "${city.name} ${it.editText}", Toast.LENGTH_SHORT).show()
                    }
                    setOnLongClickListener {
                        editMode = !editMode
                        onItemViewClickListener?.onLongCityItemViewClick(city)
                        true }
                }
            }
            changeCityStyle(itemView, city, editMode)
            itemView.findViewById<CheckBox>(R.id.check_city).apply {
                visibility = if (editMode) View.VISIBLE else View.INVISIBLE
                isChecked = city.isChecked
                //text = city.name
                setOnCheckedChangeListener { chb, isChecked ->
                    city.isChecked = isChecked
                    changeCityStyle(itemView, city, editMode)
                    onItemViewClickListener?.onCityCheckedChanged(ArrayList(cities.filter { x -> x.isChecked }))
                        Toast.makeText(context, "${city.name}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun changeCityStyle(view: View, city : City, isEditMode : Boolean) {
        view.findViewById<TextView>(R.id.city_name).apply {
            text = city.name
            if (!isEditMode) {
                setTextAppearance(if (city.isChecked) {R.style.CheckedCityStyle} else {R.style.CityStyle})
            }
        }
    }

    fun removeListener() {
        onItemViewClickListener = null
    }
}