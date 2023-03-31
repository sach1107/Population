package com.sachin.population.presentation.viewmodel.dashboard

import CoroutineTestRule
import com.google.common.truth.Truth
import com.sachin.population.domain.model.Population
import com.sachin.population.domain.usecases.nation.GetNationPopulationDataUseCase
import com.sachin.population.presentation.state.UiScreenState
import com.sachin.population.utils.Result
import io.mockk.coEvery
import io.mockk.mockkClass
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
internal class DashboardViewModelTest {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var getNationPopulationDataUseCase: GetNationPopulationDataUseCase

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setUp() {
        getNationPopulationDataUseCase = mockkClass(GetNationPopulationDataUseCase::class, relaxed = true)
        viewModel = DashboardViewModel(coroutineTestRule.testDispatcher, getNationPopulationDataUseCase)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun retry_fetchGetNationDataSuccess() = runTest {
        val population = mockkClass(Population::class, relaxed = true)
        coEvery { getNationPopulationDataUseCase.execute() } returns flow {
            emit(Result.Success(population))
        }
        viewModel.retry()
        val job = async { viewModel.uiState.take(1).first() }

        Truth.assertThat(job.await()).isInstanceOf(UiScreenState.Success::class.java)
    }

    @Test
    fun retry_fetchGetNationDataFailure() = runTest {
        coEvery { getNationPopulationDataUseCase.execute() } returns flow {
            emit(Result.Failure("", Throwable()))
        }
        viewModel.retry()
        val job = async { viewModel.uiState.take(1).first() }

        Truth.assertThat(job.await()).isInstanceOf(UiScreenState.Error::class.java)
    }
}