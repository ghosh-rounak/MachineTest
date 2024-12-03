package com.rounak.machinetest.di
import com.rounak.machinetest.data.network.RemoteDataSource
import com.rounak.machinetest.data.network.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofitService(
        remoteDataSource: RemoteDataSource
    ): RetrofitService {
        return remoteDataSource.buildService(RetrofitService::class.java)
    }

}