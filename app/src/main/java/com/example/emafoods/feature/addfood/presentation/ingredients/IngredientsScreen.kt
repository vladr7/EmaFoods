package com.example.emafoods.feature.addfood.presentation.ingredients

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emafoods.feature.addfood.presentation.description.navigation.DescriptionArguments

@Composable
fun IngredientsRoute(
    modifier: Modifier = Modifier,
    viewModel: IngredientsViewModel = hiltViewModel(),
    onConfirmedClick: (DescriptionArguments) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    IngredientsScreen(
        modifier = modifier,
        onConfirmedClick = {
            onConfirmedClick(
                DescriptionArguments(
                    uri = state.uriId,
                    category = state.categoryType.string,
                )
            )
        }
    )
}

@Composable
fun IngredientsScreen(
    modifier : Modifier = Modifier,
    onConfirmedClick: () -> Unit
) {

    Button(onClick = {
        onConfirmedClick()
    }) {
        Text(text = "Ingredients")
    }
}