package com.geekbrains.myweather_kotlin1.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Build
import android.provider.ContactsContract
import androidx.annotation.RequiresApi
import com.geekbrains.myweather_kotlin1.model.City
import com.geekbrains.myweather_kotlin1.model.PhoneContact
import com.geekbrains.myweather_kotlin1.model.db.HistoryDao
import com.geekbrains.myweather_kotlin1.model.db.HistoryEntity
import com.geekbrains.myweather_kotlin1.model.db.HistoryForecast
import com.geekbrains.myweather_kotlin1.model.forecast.WeekWeather
import com.geekbrains.myweather_kotlin1.utils.Constants
import com.geekbrains.myweather_kotlin1.utils.convertHistoryEntityToForecast
import com.geekbrains.myweather_kotlin1.utils.convertTodayForecastToEntities

class LocalRepository(private val localDataSource: HistoryDao? = null) : ILocalRepository {

    override fun saveCurrentCity(city: City, preferences: SharedPreferences) {
        if (preferences == null)
            return
        val editor = preferences.edit()
        editor?.putString(Constants.CURRENT_CITY, city.name)
        editor?.apply()
    }

    override fun getCurrentCity(preferences: SharedPreferences): City? {
        if (preferences == null || Repository.getCities().count() == 0)
            return null
        val city = preferences.getString(Constants.CURRENT_CITY, null).let {
            Repository.getCities().find { city -> city.name.equals(it) }
        }
        return city ?: Repository.getCities()[0]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun saveHistory(city: City, weekWeather: WeekWeather) {
        localDataSource?.let {
            convertTodayForecastToEntities(city, weekWeather).forEach({ localDataSource.insert(it) })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCityHistory(city: City): MutableList<HistoryForecast> {
        localDataSource?.let {
            return convertHistoryEntityToForecast(city, localDataSource.getDataByCity(city.name))
        }
        return mutableListOf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCitiesHistory(cities : ArrayList<City>): MutableList<HistoryForecast> {
        localDataSource?.let {
            val names: ArrayList<String> = arrayListOf()
            cities.forEach { city: City ->
                names.add(city.name)
            }
            val historyForecasts = mutableListOf<HistoryForecast>()
            val historyEntities = localDataSource.getDataByCities(names)
            cities.forEach { city ->
                run {
                    val cityHistoryEntities = historyEntities.filter { historyEntity -> historyEntity.city == city.name }
                    historyForecasts.addAll(convertHistoryEntityToForecast(city, cityHistoryEntities))
                }
            }
            return historyForecasts
        }
        return mutableListOf()
    }

    @SuppressLint("Range")
    override fun getContacts(contentResolver: ContentResolver): MutableList<PhoneContact> {

        val contacts = mutableListOf<PhoneContact>()
        // Отправляем запрос на получение контактов и получаем ответ в виде Cursor
        val cursorWithContacts: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
//                HISTORY_URI,
            null,
            null,
            null,
//                null
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )
        cursorWithContacts?.let { cursor ->
            for (i in 0..cursor.count) {
                val contactNumbers = mutableListOf<String>()
                // Переходим на позицию в Cursor
                if (cursor.moveToPosition(i)) {
                    val contact = PhoneContact(
                        ID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
                        name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                        numbers = contactNumbers
                    )
                    if (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) == "1") {
                        val phones = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ${contact.ID}",
                            null,
                            null
                        )
                        phones?.let { cursor ->
                            while (phones.moveToNext()) {
                                contactNumbers.add(
                                    phones.getString(
                                        phones.getColumnIndex(
                                            ContactsContract.CommonDataKinds.Phone.NUMBER
                                        )
                                    )
                                )
                            }
                        }
                    }
                    contacts.add(contact)
                }
            }
        }
        cursorWithContacts?.close()
        return contacts
    }

    @SuppressLint("Range")
    private fun getContacts() {
//        val HISTORY_URI: Uri =
//            Uri.parse("content://com.als.l2.providers/history")

    }
}
