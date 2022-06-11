package com.example.capstone.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserSession private constructor(private val dataStore: DataStore<Preferences>) {

    private val USER_TOKEN = stringPreferencesKey("user_token")
    private val USER_ID = stringPreferencesKey("user_id")
    private val USER_NAME = stringPreferencesKey("user_name")
    private val USER_EMAIL = stringPreferencesKey("user_email")
    private val USER_PHONE = stringPreferencesKey("user_phone")

    suspend fun saveUserToPreferencesStore(token: String, id: String, name: String, email: String, phone: String) {
        dataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
            preferences[USER_ID] = id
            preferences[USER_NAME] = name
            preferences[USER_EMAIL] = email
            preferences[USER_PHONE] = phone
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USER_TOKEN] ?: ""
        }
    }

    fun getId(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USER_ID] ?: ""
        }
    }

    fun getName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USER_NAME] ?: ""
        }
    }

    fun getPhone(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USER_PHONE] ?: ""
        }
    }

    fun getEmail(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USER_EMAIL] ?: ""
        }
    }

//    suspend fun saveToken(token: String) {
//        dataStore.edit { preferences ->
//            preferences[key] = token
//        }
//    }

    companion object {
        @Volatile
        private var INSTANCE: UserSession? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserSession {
            return INSTANCE ?: synchronized(this) {
                val instance = UserSession(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}