package com.example.emafoods

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.emafoods.ui.EmaFoodsNavigation
import com.example.emafoods.ui.SignInNavigation
import com.example.emafoods.ui.theme.EmaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EmaFoodsNavigation()

//                    val randomNumber = (0..1).random()
//                    if (randomNumber == 0) {
//                        SignInNavigation()
//                    } else {
//                        EmaFoodsNavigation()
//                    }
                }
            }
        }
    }
}

