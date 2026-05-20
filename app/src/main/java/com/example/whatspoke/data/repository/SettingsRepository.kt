package com.example.whatspoke.data.repository

import com.example.whatspoke.data.local.datastore.SettingsDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) {

    val userNameFlow: Flow<String> = settingsDataStore.userNameFlow
    val themeModeFlow: Flow<String> = settingsDataStore.themeModeFlow

    suspend fun saveUserName(name: String) {
        settingsDataStore.saveUserName(name)
    }

    suspend fun saveThemeMode(mode: String) {
        settingsDataStore.saveThemeMode(mode)
    }
}
