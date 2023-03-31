package com.sachin.population.data.remote.dto.state

import com.google.gson.annotations.SerializedName
import com.sachin.population.data.remote.dto.Source
import com.sachin.population.domain.model.Population

data class StatePopulationDto(
    @SerializedName("data") var data: List<StateDataDto>,
    @SerializedName("source") var source: List<Source>,
)

fun StatePopulationDto.toStatePopulation(): Population {
    return Population(data = data.map { it.toData() })
}