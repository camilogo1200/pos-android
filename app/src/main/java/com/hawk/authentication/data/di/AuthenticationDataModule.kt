package com.hawk.authentication.data.di

import android.content.Context
import androidx.room.Room
import com.hawk.BuildConfig
import com.hawk.authentication.data.datasources.local.AuthenticationSessionDao
import com.hawk.authentication.data.datasources.local.RoomAuthenticationLocalDataSource
import com.hawk.authentication.data.datasources.local.interfaces.AuthenticationLocalDataSource
import com.hawk.authentication.data.datasources.remote.AuthenticationApi
import com.hawk.authentication.data.datasources.remote.KeycloakAuthenticationRemoteDataSource
import com.hawk.authentication.data.datasources.remote.interfaces.AuthenticationRemoteDataSource
import com.hawk.authentication.data.repository.AuthenticationRepositoryImpl
import com.hawk.authentication.data.repository.interfaces.AuthenticationRepository
import com.hawk.common.database.HawkDatabase
import com.hawk.common.environment.AppEnvironment
import dagger.Module
import dagger.Provides
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthenticationDataModule {

    @Binds
    abstract fun bindAuthenticationRepository(
        impl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    abstract fun bindAuthenticationRemoteDataSource(
        impl: KeycloakAuthenticationRemoteDataSource
    ): AuthenticationRemoteDataSource

    @Binds
    abstract fun bindAuthenticationLocalDataSource(
        impl: RoomAuthenticationLocalDataSource
    ): AuthenticationLocalDataSource

    companion object {
        @Provides
        @Singleton
        fun provideJson(): Json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        @Provides
        @Singleton
        fun provideLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        @Provides
        @Singleton
        fun provideOkHttpClient(
            loggingInterceptor: Interceptor
        ): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        @Provides
        @Singleton
        fun provideRetrofit(
            okHttpClient: OkHttpClient
        ): Retrofit = Retrofit.Builder()
            .baseUrl(AppEnvironment.keycloakBaseUrl)
            .client(okHttpClient)
            .build()

        @Provides
        @Singleton
        fun provideAuthenticationApi(
            retrofit: Retrofit
        ): AuthenticationApi = retrofit.create(AuthenticationApi::class.java)

        @Provides
        @Singleton
        fun provideHawkDatabase(
            @ApplicationContext context: Context
        ): HawkDatabase = Room.databaseBuilder(
            context,
            HawkDatabase::class.java,
            AppEnvironment.databaseName
        ).build()

        @Provides
        @Singleton
        fun provideAuthenticationSessionDao(
            hawkDatabase: HawkDatabase
        ): AuthenticationSessionDao = hawkDatabase.authenticationSessionDao()
    }
}
