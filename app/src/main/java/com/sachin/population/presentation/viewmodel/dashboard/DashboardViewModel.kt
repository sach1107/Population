package com.sachin.population.presentation.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin.population.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.sachin.population.di.DispatchersIO
import com.sachin.population.domain.usecases.nation.GetNationPopulationDataUseCase
import com.sachin.population.presentation.state.UiScreenState
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    @DispatchersIO val ioDispatcher: CoroutineDispatcher,
    private val getNationPopulationDataUseCase: GetNationPopulationDataUseCase,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiScreenState>(UiScreenState.Progress)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            getNationPopulation()
        }
    }

    fun retry() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.value = UiScreenState.Progress
            getNationPopulation()
        }
    }

    private suspend fun getNationPopulation() {
        getNationPopulationDataUseCase.execute().collect { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.value = UiScreenState.Success(result.value)
                }
                is Result.Failure -> {
                    _uiState.value = UiScreenState.Error(result.message)
                }
            }
        }
    }
}