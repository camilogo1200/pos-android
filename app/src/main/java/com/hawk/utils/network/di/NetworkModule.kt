package com.hawk.utils.network.di

import com.hawk.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val APPLICATION_JSON_MEDIA_TYPE = "application/json"

    @Provides
    fun providesRetrofitClient(
        okHttpClient: OkHttpClient
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .client(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addNetworkInterceptor(interceptor)
        return builder.build()
    }


    private val jsonProperties = Json {
        isLenient = true
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun providesKotlinxConverterFactory(): Converter.Factory {
        val contentType = APPLICATION_JSON_MEDIA_TYPE.toMediaType()
        return jsonProperties.asConverterFactory(contentType)
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        val logLevel = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else HttpLoggingInterceptor.Level.NONE
        interceptor.setLevel(logLevel)
        return interceptor
    }
}
