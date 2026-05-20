package com.example.whatspoke.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whatspoke.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _userName = MutableStateFlow("Trainer")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _themeMode = MutableStateFlow("system")
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.userNameFlow.collect { name ->
                _userName.value = name
            }
        }
        viewModelScope.launch {
            settingsRepository.themeModeFlow.collect { mode ->
                _themeMode.value = mode
            }
        }
    }

    fun saveUserName(name: String) {
        viewModelScope.launch {
            settingsRepository.saveUserName(name)
        }
    }

    fun saveThemeMode(mode: String) {
        viewModelScope.launch {
            settingsRepository.saveThemeMode(mode)
        }
    }
}
