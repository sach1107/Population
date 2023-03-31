package com.sachin.population.domain.usecases.nation

import com.sachin.population.data.remote.dto.nation.toPopulation
import com.sachin.population.domain.model.Population
import com.sachin.population.domain.repository.PopulationRepository
import com.sachin.population.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetNationPopulationDataUseCase @Inject constructor(
    private val repository: PopulationRepository
) {
    fun execute(): Flow<Result<Population>> = flow {
        try {
            val population = repository.getNationPopulationData().toPopulation()
            emit(Result.Success(population))
        } catch(e: HttpException) {
            emit(Result.Failure(e.localizedMessage ?: "Unexpected error occurred", e))
        } catch(e: IOException) {
            emit(Result.Failure("Something went wrong", e))
        }
    }
}