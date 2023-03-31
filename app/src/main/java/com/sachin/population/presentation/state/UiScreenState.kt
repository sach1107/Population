package com.sachin.population.presentation.state

import com.sachin.population.domain.model.Population

sealed class UiScreenState {
    object Progress : UiScreenState()
    data class Error(val message: String) : UiScreenState()
    data class Success(val population: Population) : UiScreenState()
}