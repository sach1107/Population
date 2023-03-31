package com.sachin.population.data.remote.dto.state

import com.google.gson.annotations.SerializedName
import com.sachin.population.domain.model.Data

data class StateDataDto(
    @SerializedName("ID State") var stateId : String,
    @SerializedName("State") var state : String,
    @SerializedName("ID Year") var yearId : Int,
    @SerializedName("Year") var year : String,
    @SerializedName("Population") var population : Int,
    @SerializedName("Slug State") var slugState : String,
)

fun StateDataDto.toData(): Data {
    return Data(
        id = stateId,
        yearId = yearId,
        population = population,
        slugName = slugState,
        name = state,
        year = year,
    )
}
