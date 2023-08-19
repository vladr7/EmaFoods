package com.example.emafoods.navigation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.PendingActions
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.QuestionMark
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
        selectedIcon = Icons.Filled.QuestionMark,
        unselectedIcon = Icons.Outlined.QuestionMark,
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

    object AllFoods : HomeBottomDestination(
        selectedIcon = Icons.Filled.MenuBook,
        unselectedIcon = Icons.Outlined.MenuBook,
        "all_foods",
    )
}

