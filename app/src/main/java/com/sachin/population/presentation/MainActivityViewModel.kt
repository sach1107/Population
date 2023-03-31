package com.sachin.population.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin.population.NavRoute
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

  var startDestination by mutableStateOf<NavRoute?>(null)
    private set

  init {
    viewModelScope.launch() {
      //To set the start destination based on conditions
      startDestination = NavRoute.Dashboard
    }
  }
}