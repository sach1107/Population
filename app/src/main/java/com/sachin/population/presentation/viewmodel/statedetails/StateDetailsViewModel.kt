package com.sachin.population.presentation.viewmodel.statedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachin.population.di.DispatchersIO
import com.sachin.population.domain.usecases.state.GetStatePopulationDetailsUseCase
import com.sachin.population.presentation.state.UiScreenState
import com.sachin.population.utils.Constants
import com.sachin.population.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StateDetailsViewModel @Inject constructor(
    @DispatchersIO val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
    private val getStatePopulationDetailsUseCase: GetStatePopulationDetailsUseCase,
) : ViewModel() {

    private val year: String = checkNotNull(savedStateHandle[Constants.END_POINT_YEAR])

    private val _uiState =
        MutableStateFlow<UiScreenState>(UiScreenState.Progress)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            getStatePopulationDetails()
        }
    }

    fun retry() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.value = UiScreenState.Progress
            getStatePopulationDetails()
        }
    }

    private suspend fun getStatePopulationDetails() {
        getStatePopulationDetailsUseCase.execute(year).collect { result ->
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