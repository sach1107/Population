package com.sachin.population.data.remote.dto.nation

import com.google.gson.annotations.SerializedName
import com.sachin.population.data.remote.dto.Source
import com.sachin.population.domain.model.Population

data class NationPopulationDto(
    @SerializedName("data") var nationData: List<NationDataDto>,
    @SerializedName("source") var source: List<Source>,
)

fun NationPopulationDto.toPopulation(): Population {
    return Population(data = nationData.map { it.toData() })
}
