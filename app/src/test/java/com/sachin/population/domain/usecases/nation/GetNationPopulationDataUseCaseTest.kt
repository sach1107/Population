package com.sachin.population.domain.usecases.nation

import CoroutineTestRule
import com.google.common.truth.Truth.assertThat
import com.sachin.population.data.remote.dto.nation.NationDataDto
import com.sachin.population.data.remote.dto.nation.NationPopulationDto
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
internal class GetNationPopulationDataUseCaseTest {

    private val populationRepository = mockk<PopulationRepository>()
    private val populationDataUseCase = GetNationPopulationDataUseCase(populationRepository)

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Test
    fun execute_fetchNationPopulationData_returnSuccessWithData() =
        runTest(coroutineTestRule.testDispatcher) {
            val nationDataDto1 = NationDataDto(
                nationId = "1",
                yearId = 2020,
                year = "2020",
                slugNation = "n1",
                nation = "USA",
                population = 123
            )
            val nationDataDto2 = NationDataDto(
                nationId = "2",
                yearId = 2019,
                year = "2019",
                slugNation = "n2",
                nation = "USA",
                population = 120
            )
            val list = listOf(nationDataDto1, nationDataDto2)
            val nationPopulationDto = NationPopulationDto(nationData = list, source = emptyList())
            coEvery { populationRepository.getNationPopulationData() } returns nationPopulationDto

            val result = populationDataUseCase.execute()
            val job = async { result.take(2).last() }

            assertThat(job.await()).isInstanceOf(Result.Success::class.java)
            coVerify {
                populationRepository.getNationPopulationData()
            }
        }

    @Test
    fun execute_fetchNationPopulationDataWithNoData_returnSuccessWithEmptyList() =
        runTest(coroutineTestRule.testDispatcher) {
            val nationPopulationDto =
                NationPopulationDto(nationData = emptyList(), source = emptyList())
            coEvery { populationRepository.getNationPopulationData() } returns nationPopulationDto

            val result = populationDataUseCase.execute()
            val job = async { result.take(2).last() }

            assertThat(job.await()).isInstanceOf(Result.Success::class.java)
            coVerify {
                populationRepository.getNationPopulationData()
            }
        }

    @Test
    fun execute_fetchNationPopulationDataWithIOException_returnFailureWithMsg() =
        runTest(coroutineTestRule.testDispatcher) {
            val exception = mockk<IOException>()
            coEvery { populationRepository.getNationPopulationData() } throws exception

            val result = populationDataUseCase.execute()
            val job = async { result.take(2).last() }

            assertThat(job.await()).isInstanceOf(Result.Failure::class.java)
            coVerify {
                populationRepository.getNationPopulationData()
            }
        }

    @Test
    fun execute_fetchNationPopulationWithHttpException_returnFailureWithMsg() =
        runTest(coroutineTestRule.testDispatcher) {
            val exception = mockk<HttpException>()
            every { exception.localizedMessage } returns FAILURE_MESSAGE
            coEvery { populationRepository.getNationPopulationData() } throws exception

            val result = populationDataUseCase.execute()
            val job = async { result.take(2).last() }

            assertThat(job.await()).isInstanceOf(Result.Failure::class.java)
            coVerify {
                populationRepository.getNationPopulationData()
            }
        }

    @Test
    fun execute_fetchNationPopulationWithHttpExceptionWithNoLocalizedMsg_returnFailureWithMsg() =
        runTest(coroutineTestRule.testDispatcher) {
            val exception = mockk<HttpException>()
            every { exception.localizedMessage } returns null
            coEvery { populationRepository.getNationPopulationData() } throws exception

            val result = populationDataUseCase.execute()
            val job = async { result.take(2).last() }

            assertThat(job.await()).isInstanceOf(Result.Failure::class.java)
            coVerify {
                populationRepository.getNationPopulationData()
            }
        }

    @After
    fun tearDown() {
        unmockkAll()
    }

    private companion object {
        private const val FAILURE_MESSAGE = "http exception"
    }
}