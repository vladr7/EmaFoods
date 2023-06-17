package com.example.emafoods

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.emafoods.navigation.home.EmaFoodsNavigation
import com.example.emafoods.navigation.signin.SignInNavigation
import com.example.emafoods.ui.theme.EmaTheme
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.IMMEDIATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        checkForAppUpdate()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val state = viewModel.state.collectAsState()
            EmaTheme {
                window?.statusBarColor =
                    MaterialTheme.colorScheme.secondary.toArgb()
                window?.navigationBarColor =
                    MaterialTheme.colorScheme.secondary.toArgb()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (state.value.userSignInState) {
                        UserSignInState.LOADING -> {}

                        UserSignInState.SIGNED_IN -> {
                            EmaFoodsNavigation(
                                isAdmin = state.value.isAdmin
                            )
                        }

                        UserSignInState.NOT_SIGNED_IN -> {
                            SignInNavigation()
                        }
                    }
                }
            }
        }
    }

    private fun checkForAppUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            val isUpdateAvailable =
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            if (isUpdateAvailable) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    updateType,
                    this,
                    APP_UPDATE_REQUEST_CODE
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability()
                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    updateType,
                    this,
                    APP_UPDATE_REQUEST_CODE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_UPDATE_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                println("Update flow failed! Result code: $resultCode")
            } else {
                println("Update flow complete! Result code: $resultCode")
            }
        }
    }

    companion object {
        private const val APP_UPDATE_REQUEST_CODE = 9001
    }
}

