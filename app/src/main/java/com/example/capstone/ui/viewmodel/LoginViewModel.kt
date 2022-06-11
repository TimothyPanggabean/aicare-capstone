package com.example.capstone.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.local.UserSession
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserSession) : ViewModel() {
    fun saveToken(token: String, id: String, name: String, email: String, phone: String) : String{
        viewModelScope.launch {
            pref.saveUserToPreferencesStore(token, id, name, email, phone)
        }
        return token
    }
}