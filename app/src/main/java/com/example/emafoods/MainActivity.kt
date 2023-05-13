package com.example.emafoods

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.emafoods.navigation.home.EmaFoodsNavigation
import com.example.emafoods.navigation.signin.SignInNavigation
import com.example.emafoods.ui.theme.EmaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val state = viewModel.state.collectAsState()
            EmaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (state.value.isUserSignedIn) {
                        EmaFoodsNavigation()
                    } else {
                        SignInNavigation()
                    }
                }
            }
        }
    }
}

