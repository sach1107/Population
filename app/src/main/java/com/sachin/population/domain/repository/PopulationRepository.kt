package com.sachin.population.domain.repository

import com.sachin.population.data.remote.dto.nation.NationPopulationDto
import com.sachin.population.data.remote.dto.state.StatePopulationDto

interface PopulationRepository {

    suspend fun getNationPopulationData(): NationPopulationDto

    suspend fun getStatePopulationByYear(year: String): StatePopulationDto
}