package com.example.invetory

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_credentials")

object UserCredentialsStore {

    private val EMAIL_KEY = stringPreferencesKey("user_email")
    private val PASSWORD_KEY = stringPreferencesKey("user_password")
    private val ID_KEY = intPreferencesKey("user_id")

    suspend fun saveCredentials(context: Context, id: Int, email: String, password: String){
        context.dataStore.edit { prefs ->
            prefs[EMAIL_KEY] = email
            prefs[PASSWORD_KEY] = password
            prefs[ID_KEY] = id
        }
    }

    suspend fun useEmail(context: Context) : String?{
        return context.dataStore.data.map { it[EMAIL_KEY] }.first()
    }

    suspend fun getPassword(context: Context) : String?{
        return context.dataStore.data.map{ it[PASSWORD_KEY] }.first()
    }

    suspend fun getUserId(context: Context): Int? {
        return context.dataStore.data.map { it[ID_KEY] }.first()
    }

    suspend fun clearCredentials(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}