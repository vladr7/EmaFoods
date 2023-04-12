package com.example.emafoods.navigation.home

import androidx.annotation.StringRes
import com.example.emafoods.R

sealed class HomeBottomDestination(val route: String, @StringRes val resourceId: Int) {
    object AddFood : HomeBottomDestination("add_food", R.string.add_food)
    object ListFood : HomeBottomDestination("list_food", R.string.foods_list)
}

