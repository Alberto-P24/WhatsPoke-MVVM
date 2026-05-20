package com.example.whatspoke.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

@Singleton
class SettingsDataStore @Inject constructor(
    private val context: Context
) {

    companion object {
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        const val DEFAULT_USER_NAME = "Trainer"
        const val DEFAULT_THEME_MODE = "system"
    }

    val userNameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME_KEY] ?: DEFAULT_USER_NAME
    }

    val themeModeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_MODE_KEY] ?: DEFAULT_THEME_MODE
    }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    suspend fun saveThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode
        }
    }
}
