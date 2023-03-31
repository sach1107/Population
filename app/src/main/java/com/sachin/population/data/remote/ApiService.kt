package com.sachin.population.data.remote

import com.sachin.population.data.remote.dto.nation.NationPopulationDto
import com.sachin.population.data.remote.dto.state.StatePopulationDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/api/data?drilldowns=Nation&measures=Population")
    suspend fun getNationalPopulation(): NationPopulationDto

    @GET("/api/data?drilldowns=State&measures=Population")
    suspend fun getStatePopulationByYear(@Query("year") year: String): StatePopulationDto
}