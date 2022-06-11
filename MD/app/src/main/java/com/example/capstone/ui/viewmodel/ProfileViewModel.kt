package com.example.capstone.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.capstone.data.local.UserSession

class ProfileViewModel(private val pref: UserSession) : ViewModel() {
    var userToken : LiveData<String> = pref.getToken().asLiveData()
    var userName : LiveData<String> = pref.getName().asLiveData()
    var userEmail : LiveData<String> = pref.getEmail().asLiveData()
    var userPhone : LiveData<String> = pref.getPhone().asLiveData()
}