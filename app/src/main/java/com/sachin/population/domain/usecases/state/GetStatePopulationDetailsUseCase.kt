package com.sachin.population.domain.usecases.state

import com.sachin.population.data.remote.dto.state.toStatePopulation
import com.sachin.population.domain.model.Population
import com.sachin.population.domain.repository.PopulationRepository
import com.sachin.population.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetStatePopulationDetailsUseCase @Inject constructor(
    private val repository: PopulationRepository
) {
    fun execute(year: String): Flow<Result<Population>> = flow {
        try {
            val stateDetails = repository.getStatePopulationByYear(year).toStatePopulation()
            emit(Result.Success(stateDetails))
        } catch(e: HttpException) {
            emit(Result.Failure(e.localizedMessage ?: "Unexpected error occurred", e))
        } catch(e: IOException) {
            emit(Result.Failure("Something went wrong.", e))
        }
    }
}