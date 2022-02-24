package com.geekbrains.myweather_kotlin1.presentation.contacts

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.model.PhoneContact
import com.geekbrains.myweather_kotlin1.model.db.HistoryForecast
import com.geekbrains.myweather_kotlin1.presentation.history.HistoryAdapter
import com.google.android.material.button.MaterialButton

class ContactsAdapter(private var onItemViewClickListener: OnContactItemViewClickListener?)
    : RecyclerView.Adapter<ContactsAdapter.RecyclerItemViewHolder>() {

    private var data: MutableList<PhoneContact> = mutableListOf()

    fun setData(data: MutableList<PhoneContact>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsAdapter.RecyclerItemViewHolder {
        return RecyclerItemViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: ContactsAdapter.RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: PhoneContact) {
            itemView.findViewById<AppCompatTextView>(R.id.contact_name).text = data.name
            for (number in data.numbers) {
                itemView.findViewById<LinearLayout>(R.id.phone_numbers).addView(
                    Button(itemView.context).apply {
                        text = number
                        setOnClickListener {
                            onItemViewClickListener?.onContactItemViewClick(number) }
                    })
            }
        }
    }
}