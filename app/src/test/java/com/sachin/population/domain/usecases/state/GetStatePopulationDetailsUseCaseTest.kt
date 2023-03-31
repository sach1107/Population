package com.sachin.population.domain.usecases.state

import CoroutineTestRule
import com.google.common.truth.Truth
import com.sachin.population.data.remote.dto.state.StateDataDto
import com.sachin.population.data.remote.dto.state.StatePopulationDto
import com.sachin.population.domain.repository.PopulationRepository
import com.sachin.population.utils.Result
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetStatePopulationDetailsUseCaseTest {

    private lateinit var populationRepository: PopulationRepository
    private lateinit var statePopulationDetailsUseCase: GetStatePopulationDetailsUseCase

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setUp() {
        populationRepository = mockkClass(PopulationRepository::class, relaxed = true)
        statePopulationDetailsUseCase = GetStatePopulationDetailsUseCase(populationRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun execute_fetchStatePopulationData_emitSuccess() = runTest(coroutineTestRule.testDispatcher) {
        val stateDataDto1 = StateDataDto(
            stateId = "1",
            yearId = 2020,
            year = "2020",
            slugState = "n1",
            state = "state1",
            population = 12
        )
        val stateDataDto2 = StateDataDto(
            stateId = "2",
            yearId = 2019,
            year = "2019",
            slugState = "n2",
            state = "state2",
            population = 10
        )
        val list = listOf(stateDataDto1, stateDataDto2)
        val statePopulationDto = StatePopulationDto(data = list, source = emptyList())
        coEvery { populationRepository.getStatePopulationByYear(any()) } returns statePopulationDto

        val result = statePopulationDetailsUseCase.execute("2019")
        val job = async { result.take(2).last() }

        Truth.assertThat(job.await()).isInstanceOf(Result.Success::class.java)
        coVerify {
            populationRepository.getStatePopulationByYear("2019")
        }
    }

    @Test
    fun execute_fetchStatePopulationDataIOException_emitFailure() = runTest(coroutineTestRule.testDispatcher) {
        val exception = mockkClass(IOException::class)
        coEvery { populationRepository.getStatePopulationByYear(any()) } throws exception

        val result = statePopulationDetailsUseCase.execute("2019")
        val job = async { result.take(2).last() }

        Truth.assertThat(job.await()).isInstanceOf(Result.Failure::class.java)
        coVerify {
            populationRepository.getStatePopulationByYear("2019")
        }
    }

    @Test
    fun execute_fetchStatePopulationDataConnectionException_emitFailure() = runTest(coroutineTestRule.testDispatcher) {
        val exception = mockkClass(HttpException::class, relaxed = true)
        coEvery { populationRepository.getStatePopulationByYear(any()) } throws exception

        val result = statePopulationDetailsUseCase.execute("2019")
        val job = async { result.take(2).last() }

        Truth.assertThat(job.await()).isInstanceOf(Result.Failure::class.java)
        coVerify {
            populationRepository.getStatePopulationByYear("2019")
        }
    }
}