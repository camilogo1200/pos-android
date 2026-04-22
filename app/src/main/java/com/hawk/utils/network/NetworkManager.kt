package com.hawk.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import android.net.NetworkCapabilities.TRANSPORT_BLUETOOTH
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_LOWPAN
import android.net.NetworkCapabilities.TRANSPORT_USB
import android.net.NetworkCapabilities.TRANSPORT_VPN
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkCapabilities.TRANSPORT_WIFI_AWARE
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow


class NetworkManager(private val context: Context) {

    companion object {
        private const val DELAY_NETWORK_STATUS_REPORT = 5000L
    }

    private val tag = NetworkManager::class.java.name
    private var onLostListener: (() -> Unit)? = null
    private var onAvailableListener: (() -> Unit)? = null

    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    var connectionType: ConnectionType = ConnectionType.UNKNOWN
        private set
    var hasConnectivity: Boolean = false
        private set

    val isCellular: Boolean
        get() = connectionType == ConnectionType.CELLULAR

    val isWifi: Boolean
        get() = connectionType == ConnectionType.WIFI
                || connectionType == ConnectionType.WIFI_AWARE

    fun startObserver() {
        setUpNetworkProperties()
        setUpNetworkListeners()
    }

    fun stopObserver() {
        removeListeners()
    }

    fun setOnAvailableNetworkListener(onAvailableListener: () -> Unit) {
        this.onAvailableListener = onAvailableListener
    }

    fun setOnLostNetworkListener(onLostListener: () -> Unit) {
        this.onLostListener = onLostListener
    }

    fun removeListeners() {
        unregister()
        networkCallback = null
        onLostListener = null
        onAvailableListener = null
    }

    fun unregister() {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        if (connectivityManager != null && networkCallback != null) {
            networkCallback?.let {
                connectivityManager.unregisterNetworkCallback(it)
            }
        }
    }

    val networkStatus = flow {
        while (true) {
            emit(NetworkStatus(isNetworkAvailable(), connectionType))
            delay(DELAY_NETWORK_STATUS_REPORT)
        }
    }

    fun isNetworkAvailable(): Boolean { //inject context
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            isConnectivityAvailable(context)
        else isConnectivityAvailableLegacy(context)
    }

    @Suppress("DEPRECATION")
    private fun isConnectivityAvailableLegacy(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun isConnectivityAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        val network = connectivityManager.activeNetwork
        return connectivityManager.getNetworkCapabilities(network)?.let {
            val internetConnection = it.hasCapability(NET_CAPABILITY_INTERNET)
            val validatedNetwork = it.hasCapability(NET_CAPABILITY_VALIDATED)
            internetConnection && validatedNetwork
        } ?: false
    }

    private fun setUpNetworkListeners() {
        if (onLostListener == null || onAvailableListener == null) {
            return;
        }

        networkCallback = NetworkCallback(onLostListener, onAvailableListener)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
            networkCallback?.let { connectivityManager.registerDefaultNetworkCallback(it) }
        } else {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val request = NetworkRequest.Builder()
                .addCapability(NET_CAPABILITY_INTERNET).build()
            networkCallback?.let {
                connectivityManager.registerNetworkCallback(request, it)
            }
        }
    }

    inner class NetworkCallback(
        private var onLostListener: (() -> Unit)? = null,
        private var onAvailableListener: (() -> Unit)? = null
    ) : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            onAvailableListener?.invoke()
        }

        override fun onLost(network: Network) {
            onLostListener?.invoke()
            hasConnectivity = false
            connectionType = ConnectionType.UNKNOWN
        }

        override fun onCapabilitiesChanged(
            network: Network, networkCapabilities: NetworkCapabilities
        ) {
            setNetworkType(networkCapabilities)
            setConnectivity(networkCapabilities)
        }
    }

    private fun setConnectivity(networkCapabilities: NetworkCapabilities) {
        val internet = networkCapabilities.hasCapability(NET_CAPABILITY_INTERNET)
        val validated = networkCapabilities.hasCapability(NET_CAPABILITY_VALIDATED)
        hasConnectivity = internet && validated
    }

    private fun setNetworkType(networkCapabilities: NetworkCapabilities) {
        if (networkCapabilities.hasTransport(TRANSPORT_ETHERNET)) {
            connectionType = ConnectionType.ETHERNET
            return
        }
        if (networkCapabilities.hasTransport(TRANSPORT_CELLULAR)) {
            connectionType = ConnectionType.CELLULAR
            return
        }
        if (networkCapabilities.hasTransport(TRANSPORT_WIFI) ||
            networkCapabilities.hasTransport(TRANSPORT_WIFI_AWARE)
        ) {
            connectionType = ConnectionType.WIFI
            return
        }
        if (networkCapabilities.hasTransport(TRANSPORT_USB)) {
            connectionType = ConnectionType.USB
            return
        }
        if (networkCapabilities.hasTransport(TRANSPORT_BLUETOOTH)) {
            connectionType = ConnectionType.BLUETOOTH
            return
        }
        if (networkCapabilities.hasTransport(TRANSPORT_VPN)) {
            connectionType = ConnectionType.VPN
            return
        }
        if (networkCapabilities.hasTransport(TRANSPORT_LOWPAN)) {
            connectionType = ConnectionType.LOWPAN
            return
        }
        connectionType = ConnectionType.UNKNOWN
    }

    private fun setUpNetworkProperties() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
            val network = connectivityManager.activeNetwork
            connectivityManager.getNetworkCapabilities(network)?.let {
                setNetworkType(it)
                setConnectivity(it)
            }
        } else {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val request = NetworkRequest.Builder()
                .addCapability(NET_CAPABILITY_INTERNET).build()
        }
    }
}

data class NetworkStatus(
    val isConnected: Boolean,
    val connectionType: ConnectionType
)
