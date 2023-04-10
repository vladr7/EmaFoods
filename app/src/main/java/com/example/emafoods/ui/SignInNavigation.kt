package com.example.emafoods.ui

import androidx.compose.runtime.Composable
import com.example.emafoods.navigation.signin.SignInNavHost


@Composable
fun SignInNavigation(
    signInState: SignInState = rememberSignInState()
) {
    val navController = signInState.navController
    SignInNavHost(navController = navController)
}


