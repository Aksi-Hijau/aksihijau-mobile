package com.aksihijau.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class TokenPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val ISLOGIN_KEY = booleanPreferencesKey("islogin_setting")
    private val TOKEN_KEY = stringPreferencesKey("token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")

    fun getLoginSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[ISLOGIN_KEY] ?: false
        }
    }

    fun getToken() : Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    fun getRefreshToken() : Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY] ?: ""
        }
    }

    suspend fun saveLoginSetting(isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[ISLOGIN_KEY] = isLogin
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun saveRefreshToken(refreshToken: String){
        dataStore.edit {preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TokenPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): TokenPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = TokenPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}