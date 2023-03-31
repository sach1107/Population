package com.sachin.population.data.repository

import CoroutineTestRule
import com.google.common.truth.Truth.assertThat
import com.sachin.population.data.remote.ApiService
import com.sachin.population.data.remote.dto.nation.NationDataDto
import com.sachin.population.data.remote.dto.nation.NationPopulationDto
import com.sachin.population.data.remote.dto.state.StateDataDto
import com.sachin.population.data.remote.dto.state.StatePopulationDto
import com.sachin.population.domain.repository.PopulationRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class PopulationRepositoryImplTest {

    private lateinit var populationRepositoryImpl: PopulationRepository

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val api: ApiService = mockk(relaxed = true)

    @Before
    fun setUp() {
        populationRepositoryImpl = PopulationRepositoryImpl(api)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun getNationPopulationData_fetchNationPopulationSuccess_returnPopulationData() =
        runTest(coroutineTestRule.testDispatcher) {
            val data1 = NationDataDto(
                nationId = "1",
                yearId = 2020,
                year = "2020",
                slugNation = "n1",
                nation = "USA",
                population = 123
            )
            val data2 = NationDataDto(
                nationId = "2",
                yearId = 2019,
                year = "2019",
                slugNation = "n2",
                nation = "USA",
                population = 120
            )
            val list = listOf(data1, data2)
            val nationPopulationDto = NationPopulationDto(nationData = list, source = emptyList())
            coEvery { api.getNationalPopulation() } returns nationPopulationDto
            val nationList = populationRepositoryImpl.getNationPopulationData()

            assertThat(nationList.nationData).hasSize(2)

            coVerify {
                api.getNationalPopulation()
            }
        }

    @Test
    fun getNationPopulationData_fetchNationPopulationEmpty_returnEmptyData() =
        runTest(coroutineTestRule.testDispatcher) {
            val list = emptyList<NationDataDto>()
            val nationPopulationDto = NationPopulationDto(nationData = list, source = emptyList())
            coEvery { api.getNationalPopulation() } returns nationPopulationDto
            val nationList = populationRepositoryImpl.getNationPopulationData()

            assertThat(nationList.nationData).hasSize(0)

            coVerify {
                api.getNationalPopulation()
            }
        }

    @Test
    fun getStatePopulationByYear_fetchStatePopulationSuccess_returnStateDetailsPopulationData() =
        runTest(coroutineTestRule.testDispatcher) {
            val data1 = StateDataDto(
                stateId = "1",
                yearId = 2020,
                year = "2020",
                slugState = "n1",
                state = "Alabama",
                population = 12
            )
            val data2 = StateDataDto(
                stateId = "2",
                yearId = 2019,
                year = "2019",
                slugState = "n2",
                state = "Alaska",
                population = 10
            )
            val list = listOf(data1, data2)
            val statePopulationDto = StatePopulationDto(data = list, source = emptyList())
            coEvery { api.getStatePopulationByYear(any()) } returns statePopulationDto
            val stateList = populationRepositoryImpl.getStatePopulationByYear("2020")

            assertThat(stateList.data).hasSize(2)

            coVerify {
                api.getStatePopulationByYear(any())
            }
        }

    @Test
    fun getStatePopulationByYear_fetchStatePopulationEmpty_returnEmptyData() =
        runTest(coroutineTestRule.testDispatcher) {
            val list = emptyList<StateDataDto>()
            val statePopulationDto = StatePopulationDto(data = list, source = emptyList())
            coEvery { api.getStatePopulationByYear(any()) } returns statePopulationDto
            val stateList = populationRepositoryImpl.getStatePopulationByYear("2020")

            assertThat(stateList.data).hasSize(0)

            coVerify {
                api.getStatePopulationByYear(any())
            }
        }
}