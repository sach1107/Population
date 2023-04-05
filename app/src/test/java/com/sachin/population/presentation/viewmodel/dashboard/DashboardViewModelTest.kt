package com.sachin.population.presentation.viewmodel.dashboard

import CoroutineTestRule
import com.google.common.truth.Truth
import com.sachin.population.domain.model.Population
import com.sachin.population.domain.usecases.nation.GetNationPopulationDataUseCase
import com.sachin.population.presentation.state.UiScreenState
import com.sachin.population.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class DashboardViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val getNationPopulationDataUseCase = mockk<GetNationPopulationDataUseCase>(relaxed = true)
    private val viewModel = DashboardViewModel(coroutineTestRule.testDispatcher, getNationPopulationDataUseCase)

    @Test
    fun `GIVEN usecase object not null WHEN fetch nation data from usecase THEN success with nation data` () = runTest {
        val population = mockk<Population>()
        coEvery { getNationPopulationDataUseCase.execute() } returns flow {
            emit(Result.Success(population))
        }
        viewModel.retry()
        val job = async { viewModel.uiState.take(1).first() }

        Truth.assertThat(job.await()).isInstanceOf(UiScreenState.Success::class.java)
    }

    @Test
    fun `GIVEN usecase object not null WHEN fetch nation data from usecase THEN Failure with message` () = runTest {
        coEvery { getNationPopulationDataUseCase.execute() } returns flow {
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
        private const val FAILURE_MESSAGE = "something went wrong"
    }
}