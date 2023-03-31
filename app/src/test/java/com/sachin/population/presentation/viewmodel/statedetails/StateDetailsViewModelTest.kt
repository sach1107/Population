package com.sachin.population.presentation.viewmodel.statedetails

import CoroutineTestRule
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth
import com.sachin.population.domain.model.Population
import com.sachin.population.domain.usecases.state.GetStatePopulationDetailsUseCase
import com.sachin.population.presentation.state.UiScreenState
import com.sachin.population.utils.Result
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class StateDetailsViewModelTest {

    private lateinit var stateHandle: SavedStateHandle
    private lateinit var viewModel: StateDetailsViewModel
    private lateinit var getStatePopulationDetailsUseCase: GetStatePopulationDetailsUseCase

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setUp() {
        getStatePopulationDetailsUseCase =
            mockkClass(GetStatePopulationDetailsUseCase::class, relaxed = true)
        stateHandle = mockkClass(SavedStateHandle::class)
        every { stateHandle.get<String>("year") } returns "2020"
        viewModel = StateDetailsViewModel(
            ioDispatcher = coroutineTestRule.testDispatcher,
            savedStateHandle = stateHandle,
            getStatePopulationDetailsUseCase = getStatePopulationDetailsUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun retry_fetchGetStateDetailsDataSuccess() = runTest {
        val population = mockkClass(Population::class, relaxed = true)
        coEvery { getStatePopulationDetailsUseCase.execute(any()) } returns flow {
            emit(Result.Success(population))
        }
        viewModel.retry()
        val job = async { viewModel.uiState.take(1).first() }

        Truth.assertThat(job.await()).isInstanceOf(UiScreenState.Success::class.java)
    }

    @Test
    fun retry_fetchGetStateDetailsDataFailure() = runTest {
        coEvery { getStatePopulationDetailsUseCase.execute(any()) } returns flow {
            emit(Result.Failure("", Throwable()))
        }
        viewModel.retry()
        val job = async { viewModel.uiState.take(1).first() }

        Truth.assertThat(job.await()).isInstanceOf(UiScreenState.Error::class.java)
    }
}