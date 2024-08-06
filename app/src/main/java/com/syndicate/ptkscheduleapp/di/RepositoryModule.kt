package com.syndicate.ptkscheduleapp.di

import com.syndicate.ptkscheduleapp.data.repository.ReworkScheduleRepositoryImpl
import com.syndicate.ptkscheduleapp.domain.repository.ReworkScheduleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindReworkScheduleRepository(
        reworkScheduleRepositoryImpl: ReworkScheduleRepositoryImpl
    ): ReworkScheduleRepository
}