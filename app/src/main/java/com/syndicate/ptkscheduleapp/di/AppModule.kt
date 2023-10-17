package com.syndicate.ptkscheduleapp.di

import com.syndicate.ptkscheduleapp.core.AppConstants
import com.syndicate.ptkscheduleapp.data.remote.ScheduleApi
import com.syndicate.ptkscheduleapp.data.repository.ScheduleRepositoryImpl
import com.syndicate.ptkscheduleapp.domain.repository.ScheduleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideScheduleApi(): ScheduleApi {
        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ScheduleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideScheduleRepository(scheduleApi: ScheduleApi): ScheduleRepository {
        return ScheduleRepositoryImpl(scheduleApi)
    }
}