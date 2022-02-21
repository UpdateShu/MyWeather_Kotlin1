package com.geekbrains.myweather_kotlin1.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.geekbrains.myweather_kotlin1.model.forecast.DaysTime

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city: String,
    val date: String,
    val daysTime: Int,
    val temp: Int,
    val conditional: String,
) {
    override fun toString(): String {
        return "${conditional} ${temp}"
    }
}