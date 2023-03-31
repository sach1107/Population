package com.sachin.population.di

import com.sachin.population.data.remote.ApiService
import com.sachin.population.data.repository.PopulationRepositoryImpl
import com.sachin.population.domain.repository.PopulationRepository
import com.sachin.population.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePopulationRepository(api: ApiService): PopulationRepository {
        return PopulationRepositoryImpl(api)
    }

    @Provides
    @DispatchersIO
    fun providesCoroutineDispatcherIo(): CoroutineDispatcher = Dispatchers.IO
}