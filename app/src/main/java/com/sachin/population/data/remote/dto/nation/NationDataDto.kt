package com.sachin.population.data.remote.dto.nation

import com.google.gson.annotations.SerializedName
import com.sachin.population.domain.model.Data

data class NationDataDto(
    @SerializedName("ID Nation") var nationId: String,
    @SerializedName("Nation") var nation: String,
    @SerializedName("ID Year") var yearId: Int,
    @SerializedName("Year") var year: String,
    @SerializedName("Population") var population: Int,
    @SerializedName("Slug Nation") var slugNation: String
)

fun NationDataDto.toData(): Data {
    return Data(
        id = nationId,
        yearId = yearId,
        population = population,
        slugName = slugNation,
        name = nation,
        year = year,
    )
}
