package com.geekbrains.myweather_kotlin1.presentation.contacts

import android.content.ContentResolver
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.myweather_kotlin1.model.AppData
import com.geekbrains.myweather_kotlin1.model.AppState
import com.geekbrains.myweather_kotlin1.model.PhoneContact
import com.geekbrains.myweather_kotlin1.presentation.App
import com.geekbrains.myweather_kotlin1.repository.ILocalRepository
import com.geekbrains.myweather_kotlin1.repository.LocalRepository
import com.geekbrains.myweather_kotlin1.repository.Repository

class ContactsViewModel(
    val contactsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val localRepository: ILocalRepository = LocalRepository()
) : ViewModel() {

    fun getContacts(contentResolver: ContentResolver) {
        contactsLiveData.value = AppState.Loading
        contactsLiveData.value = AppState.Success(AppData().apply {
            contacts = localRepository.getContacts(contentResolver)
        })
    }
}