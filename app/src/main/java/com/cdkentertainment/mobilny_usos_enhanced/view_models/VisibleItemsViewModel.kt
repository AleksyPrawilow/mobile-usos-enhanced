package com.cdkentertainment.mobilny_usos_enhanced.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VisibleItemsViewModel: ViewModel() {
    var visible: Boolean by mutableStateOf(false)
        private set
    private val _visibleStates = MutableStateFlow<List<Boolean>>(emptyList())
    val visibleStates: StateFlow<List<Boolean>> = _visibleStates

    init {
        _visibleStates.value = List(Screens.entries.size + 1) { false }
    }

    fun setVisibleState(index: Int, visible: Boolean) {
        _visibleStates.value = _visibleStates.value.toMutableList().also {
            it[index] = visible
        }
    }
}