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
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetStatePopulationDetailsUseCaseTest {

    private val populationRepository = mockk<PopulationRepository>()
    private val statePopulationDetailsUseCase = GetStatePopulationDetailsUseCase(populationRepository)

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Test
    fun execute_fetchStatePopulationData_returnSuccessWithData() = runTest(coroutineTestRule.testDispatcher) {
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

        val result = statePopulationDetailsUseCase.execute(YEAR)
        val job = async { result.take(2).last() }

        Truth.assertThat(job.await()).isInstanceOf(Result.Success::class.java)
        coVerify {
            populationRepository.getStatePopulationByYear(YEAR)
        }
    }

    @Test
    fun execute_fetchStatePopulationDataWithNoData_returnSuccessWithEmptyList() =
        runTest(coroutineTestRule.testDispatcher) {
            val statePopulationDto = StatePopulationDto(data = emptyList(), source = emptyList())
            coEvery { populationRepository.getStatePopulationByYear(any()) } returns statePopulationDto

            val result = statePopulationDetailsUseCase.execute(YEAR)
            val job = async { result.take(2).last() }

            Truth.assertThat(job.await()).isInstanceOf(Result.Success::class.java)
            coVerify {
                populationRepository.getStatePopulationByYear(YEAR)
            }
        }

    @Test
    fun execute_fetchStatePopulationDataWithIOException_returnFailureWithMsg() =
        runTest(coroutineTestRule.testDispatcher) {
            val exception = mockk<IOException>()
            coEvery { populationRepository.getStatePopulationByYear(any()) } throws exception

            val result = statePopulationDetailsUseCase.execute(YEAR)
            val job = async { result.take(2).last() }

            Truth.assertThat(job.await()).isInstanceOf(Result.Failure::class.java)
            coVerify {
                populationRepository.getStatePopulationByYear(YEAR)
            }
        }

    @Test
    fun execute_fetchStatePopulationWithHttpException_returnFailureWithMsg() =
        runTest(coroutineTestRule.testDispatcher) {
            val httpException = mockk<HttpException>()
            every { httpException.localizedMessage } returns FAILURE_MESSAGE
            coEvery { populationRepository.getStatePopulationByYear(any()) } throws httpException

            val result = statePopulationDetailsUseCase.execute(YEAR)
            val job = async { result.take(2).last() }

            Truth.assertThat(job.await()).isInstanceOf(Result.Failure::class.java)
            coVerify {
                populationRepository.getStatePopulationByYear(YEAR)
            }
        }

    @Test
    fun execute_fetchStatePopulationWithHttpExceptionWithNoLocalizedMsg_returnFailureWithMsg() =
        runTest(coroutineTestRule.testDispatcher) {
            val httpException = mockk<HttpException>()
            every { httpException.localizedMessage } returns null
            coEvery { populationRepository.getStatePopulationByYear(any()) } throws httpException

            val result = statePopulationDetailsUseCase.execute(YEAR)
            val job = async { result.take(2).last() }

            Truth.assertThat(job.await()).isInstanceOf(Result.Failure::class.java)
            coVerify {
                populationRepository.getStatePopulationByYear(YEAR)
            }
        }
    @After
    fun tearDown() {
        unmockkAll()
    }

    private companion object {
        private const val YEAR = "2019"
        private const val FAILURE_MESSAGE = "http exception"
    }
}