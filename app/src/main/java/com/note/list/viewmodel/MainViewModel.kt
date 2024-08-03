package com.note.list.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var isLoading by mutableStateOf(true)
        private set

    init {
        viewModelScope.launch {
          delay(150)
            isLoading = false
        }
    }
}