package com.example.emafoods.navigation.signin

import androidx.compose.runtime.Composable


@Composable
fun SignInNavigation(
    signInState: SignInState = rememberSignInState()
) {
    val navController = signInState.navController
    SignInNavHost(navController = navController)
}


