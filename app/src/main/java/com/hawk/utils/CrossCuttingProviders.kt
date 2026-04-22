package com.hawk.utils

import android.content.Context
import com.hawk.utils.network.NetworkManager
import com.hawk.utils.network.NetworkObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CrossCuttingProviders {

    @Provides
    fun providesNetworkManager(
        @ApplicationContext context: Context
    ): NetworkManager {
        return NetworkManager(context)
    }

    @Provides
    fun providesNetworkObserver(
        @ApplicationContext context: Context
    ): NetworkObserver {
        return NetworkObserver(context)
    }

}