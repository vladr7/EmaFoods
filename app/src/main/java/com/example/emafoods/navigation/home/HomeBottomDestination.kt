package com.example.emafoods.navigation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.PendingActions
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector

sealed class HomeBottomDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
) {
    object AddFood : HomeBottomDestination(
        selectedIcon = Icons.Filled.AddCircle,
        unselectedIcon = Icons.Outlined.AddCircleOutline,
        route = "add_food",
    )

    object GenerateFood : HomeBottomDestination(
        selectedIcon = Icons.Filled.Restaurant,
        unselectedIcon = Icons.Outlined.Restaurant,
        "generate_food",
    )

    object PendingFood : HomeBottomDestination(
        selectedIcon = Icons.Filled.PendingActions,
        unselectedIcon = Icons.Outlined.PendingActions,
        "pending_food",
    )

    object Profile : HomeBottomDestination(
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        "home_profile",
    )
}

