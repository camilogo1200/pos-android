package com.hawk.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class NetworkObserver(context: Context) {

    private val connectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkStatus: Flow<NetworkStatus> = callbackFlow {

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // When network is available, we query capabilities to get the type
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                launch { send(mapCapabilitiesToStatus(capabilities)) }
            }

            override fun onLost(network: Network) {
                launch { send(NetworkStatus(isConnected = false, ConnectionType.UNKNOWN)) }
            }

            override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) {
                launch { send(mapCapabilitiesToStatus(caps)) }
            }
        }

        // 1. Define the request (looking for internet)
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        // 2. Register the callback
        connectivityManager.registerNetworkCallback(request, callback)

        // 3. Emit the initial state immediately
        val initialCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        trySend(mapCapabilitiesToStatus(initialCapabilities))

        // 4. CRITICAL: This keeps the flow active and unregisters the listener when finished
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged() // Only notify the UI if the status actually changes

    private fun mapCapabilitiesToStatus(capabilities: NetworkCapabilities?): NetworkStatus {
        if (capabilities == null) return NetworkStatus(false, ConnectionType.UNKNOWN)

        val hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        val isValidated = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        val type = when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectionType.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectionType.CELLULAR
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> ConnectionType.ETHERNET
            else -> ConnectionType.UNKNOWN
        }

        return NetworkStatus(isConnected = hasInternet && isValidated, connectionType = type)
    }
}