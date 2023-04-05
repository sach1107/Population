package com.sachin.population.presentation.viewmodel.statedetails

import CoroutineTestRule
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth
import com.sachin.population.domain.model.Population
import com.sachin.population.domain.usecases.state.GetStatePopulationDetailsUseCase
import com.sachin.population.presentation.state.UiScreenState
import com.sachin.population.utils.Result
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
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

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val getStatePopulationDetailsUseCase =
        mockk<GetStatePopulationDetailsUseCase>(relaxed = true)
    private val stateHandle = mockk<SavedStateHandle>()
    private lateinit var viewModel: StateDetailsViewModel

    @Before
    fun setUp() {
        every { stateHandle.get<String>(KEY_YEAR) } returns VALUE_YEAR
        viewModel = StateDetailsViewModel(
            ioDispatcher = coroutineTestRule.testDispatcher,
            savedStateHandle = stateHandle,
            getStatePopulationDetailsUseCase = getStatePopulationDetailsUseCase
        )
    }

    @Test
    fun `GIVEN usecase object not null WHEN fetch state data THEN success with state data` () = runTest {
        val population = mockk<Population>()
        coEvery { getStatePopulationDetailsUseCase.execute(any()) } returns flow {
            emit(Result.Success(population))
        }
        viewModel.retry()
        val job = async { viewModel.uiState.take(1).first() }

        Truth.assertThat(job.await()).isInstanceOf(UiScreenState.Success::class.java)
    }

    @Test
    fun `GIVEN usecase object not null WHEN fetch state data THEN Failure with message` () = runTest {
        coEvery { getStatePopulationDetailsUseCase.execute(any()) } returns flow {
            emit(Result.Failure(FAILURE_MESSAGE, Throwable()))
        }
        viewModel.retry()
        val job = async { viewModel.uiState.take(1).first() }

        Truth.assertThat(job.await()).isInstanceOf(UiScreenState.Error::class.java)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    private companion object {
        private const val KEY_YEAR = "year"
        private const val VALUE_YEAR = "2020"
        private const val FAILURE_MESSAGE = "failure"
    }
}