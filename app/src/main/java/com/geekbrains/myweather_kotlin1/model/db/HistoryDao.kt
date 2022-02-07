package com.geekbrains.myweather_kotlin1.model.db

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE

@Dao
interface HistoryDao {

    @Query("SELECT * FROM HistoryEntity WHERE city LIKE :city")
    fun  getDataByCity(city: String): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: HistoryEntity)

    @Update
    fun update(entity: HistoryEntity)

    @Delete
    fun delete(entity: HistoryEntity)
}