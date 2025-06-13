package com.example.caffeine_in

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.example.caffeine_in.ui.caffeinetracker.CaffeineTrackerScreen
import com.example.caffeine_in.ui.theme.CaffeineinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CaffeineinTheme {
                Surface {
                    CaffeineTrackerScreen()
                }
            }
        }
    }
}