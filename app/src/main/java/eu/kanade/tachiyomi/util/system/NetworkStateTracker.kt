package eu.kanade.tachiyomi.util.system

import android.content.Context
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import nl.adaptivity.xmlutil.serialization.structure.PolymorphicMode.TAG
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


data class NetworkState(
    val isConnected: Boolean,
    val isValidated: Boolean,
    val isWifi: Boolean,
) {
    val isOnline = isConnected && isValidated
}

/*fun internetAccessible(): Boolean {
    try {
        val p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com")
        val returnVal = p1.waitFor()
        val reachable = (returnVal == 0)
        return reachable
    } catch (e: Exception) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    }
    return false
}*/

fun Context.internetAccessible(): Boolean {
    if (connectivityManager.activeNetworkInfo?.isConnected == true) {
        try {
            val urlc = (URL("http://clients3.google.com/generate_204")
                .openConnection()) as HttpURLConnection
            urlc.setRequestProperty("User-Agent", "Android")
            urlc.setRequestProperty("Connection", "close")
            urlc.setConnectTimeout(1500)
            urlc.connect()
            return (urlc.getResponseCode() == 204 &&
                urlc.getContentLength() == 0)
        } catch (e: IOException) {
            Log.e("gihon download", "Error checking internet connection:")
        }
    } else {
        Log.d("gihon download", "No network available!")
    }
    Log.d("gihon download", "Not connected to network.")
    return false
}

@Suppress("DEPRECATION")
fun Context.activeNetworkState(): NetworkState {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return NetworkState(
            isConnected = connectivityManager.activeNetworkInfo?.isConnected ?: false,
            isValidated = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false,
            isWifi = wifiManager.isWifiEnabled && capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false,
        )
    }
    else
    {
        val type = connectivityManager.activeNetworkInfo?.type
        return NetworkState(
            isConnected = connectivityManager.activeNetworkInfo?.isConnected ?: false,
            isValidated = internetAccessible(),
            isWifi = if (type == 1) true else false,
        )
    }

}

fun Context.networkStateFlow() = callbackFlow {
    val networkCallback = object : NetworkCallback() {
        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            trySend(activeNetworkState())
        }
        override fun onLost(network: Network) {
            trySend(activeNetworkState())
        }
    }

    connectivityManager.registerDefaultNetworkCallback(networkCallback)
    awaitClose {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
