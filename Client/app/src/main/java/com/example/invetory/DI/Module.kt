package com.example.invetory.DI

import com.example.invetory.Network.KtorClient
import com.example.invetory.Network.ServiceAPIs.AuthApiService
import com.example.invetory.Network.ServiceAPIs.DashboardApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideHttpClient() : HttpClient = KtorClient.client

    @Provides
    @Singleton
    fun provideAuthApiService(client: HttpClient) : AuthApiService{
        return AuthApiService(client)
    }

    @Provides
    @Singleton
    fun provideDashboardApiService(client: HttpClient) : DashboardApiService{
        return DashboardApiService(client)
    }
}