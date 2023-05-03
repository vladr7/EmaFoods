package com.example.emafoods.navigation.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun EmaFoodsNavigation(
    emaFoodsAppState: EmaFoodsAppState = rememberEmaFoodsAppState()
) {
    val items = listOf(
        HomeBottomDestination.AddFood,
        HomeBottomDestination.ListFood,
        HomeBottomDestination.GenerateFood,
    )
    val navController = emaFoodsAppState.navController
    Scaffold(
        bottomBar = {
            BottomNavigation {
                items.forEach { destination ->
                    val selected =
                        emaFoodsAppState.currentDestination?.isTopLevelDestinationInHierarchy(destination) ?: false
                    BottomNavigationItem(
                        icon = {
                            val imageVector = if (selected) {
                                destination.selectedIcon
                            } else {
                                destination.unselectedIcon
                            }
                            Icon(imageVector = imageVector, contentDescription = null)
                        },
                        label = { Text(stringResource(destination.resourceId)) },
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
    ) { innerPadding ->
        HomeNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: HomeBottomDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.route, true) ?: false
    } ?: false
