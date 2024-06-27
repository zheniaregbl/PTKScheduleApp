package com.syndicate.ptkscheduleapp.di

import android.content.Context
import android.content.SharedPreferences
import com.syndicate.ptkscheduleapp.BuildConfig
import com.syndicate.ptkscheduleapp.data.remote.ScheduleApi
import com.syndicate.ptkscheduleapp.data.repository.ScheduleRepositoryImpl
import com.syndicate.ptkscheduleapp.domain.repository.ScheduleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ScheduleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideScheduleRepository(scheduleApi: ScheduleApi): ScheduleRepository {
        return ScheduleRepositoryImpl(scheduleApi)
    }

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_configuration", Context.MODE_PRIVATE)
    }
}