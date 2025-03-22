package com.zero_one.martha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.zero_one.martha.navigation.RootNavigationHost
import com.zero_one.martha.ui.theme.MarthaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
