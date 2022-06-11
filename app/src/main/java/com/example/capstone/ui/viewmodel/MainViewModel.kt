package com.example.capstone.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.capstone.data.local.UserSession
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserSession) : ViewModel() {
    var userToken : LiveData<String> = pref.getToken().asLiveData()

    fun saveToken(token: String, id: String, name: String, email: String, phone: String) : String{
        viewModelScope.launch {
            pref.saveUserToPreferencesStore(token, id, name, email, phone)
        }
        return token
    }
}