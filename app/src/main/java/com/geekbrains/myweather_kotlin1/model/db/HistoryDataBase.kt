package com.geekbrains.myweather_kotlin1.model.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 3, exportSchema = false)
abstract class HistoryDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}