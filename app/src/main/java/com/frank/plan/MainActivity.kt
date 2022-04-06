package com.frank.plan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.frank.plan.ui.theme.PlanTheme
import com.frank.plan.ui.views.MainView

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentTheme by remember {
                mutableStateOf(PlanTheme.Theme.Light)
            }
            PlanTheme(theme = currentTheme) {
                MainView {
                    currentTheme = when (currentTheme) {
                        PlanTheme.Theme.Light -> {
                            PlanTheme.Theme.Dark
                        }
                        else -> {
                            PlanTheme.Theme.Light
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}

