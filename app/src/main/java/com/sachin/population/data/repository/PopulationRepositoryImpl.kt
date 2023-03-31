package com.sachin.population.data.repository

import com.sachin.population.data.remote.ApiService
import com.sachin.population.data.remote.dto.nation.NationPopulationDto
import com.sachin.population.data.remote.dto.state.StatePopulationDto
import com.sachin.population.domain.repository.PopulationRepository
import javax.inject.Inject

class PopulationRepositoryImpl @Inject constructor(
    private val api: ApiService
) : PopulationRepository {

    override suspend fun getNationPopulationData(): NationPopulationDto {
        return api.getNationalPopulation()
    }

    override suspend fun getStatePopulationByYear(year: String): StatePopulationDto {
        return api.getStatePopulationByYear(year)
    }
}