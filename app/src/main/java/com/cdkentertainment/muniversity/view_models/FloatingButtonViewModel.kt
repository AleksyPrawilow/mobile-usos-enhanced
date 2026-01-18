package com.cdkentertainment.muniversity.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class FloatingButtonViewModel(): ViewModel() {
    var expanded: Boolean by mutableStateOf(false)
        private set

    fun changeExpanded(value: Boolean) {
        expanded = value
    }
}