package com.example.emafoods.navigation.home

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.emafoods.R

sealed class HomeBottomDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
    @StringRes val resourceId: Int
) {
    object AddFood : HomeBottomDestination(
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Filled.Close,
        route = "add_food",
        resourceId = R.string.add_food
    )

    object ListFood : HomeBottomDestination(
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Filled.Close,
        "list_food",
        R.string.foods_list
    )
    object GenerateFood : HomeBottomDestination(
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Filled.Close,
        "generate_food",
        R.string.generate_food
    )

    object PendingFood : HomeBottomDestination(
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Filled.Close,
        "pending_food",
        R.string.pending_food
    )

}

