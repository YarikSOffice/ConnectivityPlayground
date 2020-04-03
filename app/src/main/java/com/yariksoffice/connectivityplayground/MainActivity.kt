package com.yariksoffice.connectivityplayground

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.yariksoffice.connectivityplayground.connectivity.base.ConnectivityProvider
import com.yariksoffice.connectivityplayground.connectivity.base.ConnectivityProvider.ConnectivityStateListener
import com.yariksoffice.connectivityplayground.connectivity.base.ConnectivityProvider.NetworkState
import com.yariksoffice.connectivityplayground.connectivity.base.ConnectivityProvider.NetworkState.ConnectedState

class MainActivity : AppCompatActivity(), ConnectivityStateListener {

    private lateinit var tv: TextView
    private val provider: ConnectivityProvider by lazy { ConnectivityProvider.createProvider(this) }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.connectivity_state)
        val button = findViewById<View>(R.id.button)
        val currentState = findViewById<TextView>(R.id.current_state)

        button.setOnClickListener {
            val hasInternet = provider.getNetworkState().hasInternet()
            currentState.text = "Connectivity (synchronously): $hasInternet"
        }
    }

    override fun onStart() {
        super.onStart()
        provider.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        provider.removeListener(this)
    }

    override fun onStateChange(state: NetworkState) {
        val hasInternet = state.hasInternet()
        tv.text = "Connectivity (via callback): $hasInternet"
    }

    private fun NetworkState.hasInternet(): Boolean {
        return (this as? ConnectedState)?.hasInternet == true
    }
}