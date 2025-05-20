package com.zero_one.martha

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.zero_one.martha.navigation.RootNavigationHost
import com.zero_one.martha.ui.theme.MarthaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        enableEdgeToEdge()
        setContent {
            MarthaTheme {
                Surface {
                    RootNavigationHost()
                }
            }
        }
    }
}
