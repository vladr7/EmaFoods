package com.example.emafoods.feature.addfood.presentation.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.emafoods.R
import com.example.emafoods.feature.addfood.presentation.common.NewStepTitle
import com.example.emafoods.feature.addfood.presentation.common.StepIndicator
import com.example.emafoods.feature.addfood.presentation.image.navigation.ImageArguments

@Composable
fun CategoryRoute(
    modifier: Modifier = Modifier,
    onNextClicked: (ImageArguments?) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    CategoryScreen(
        modifier = modifier,
        onNextClicked = onNextClicked,
    )
}

@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    onNextClicked: (ImageArguments?) -> Unit
) {
    CategoryScreenBackground()
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = modifier
                .align(Alignment.TopStart)
        ) {
            StepIndicator( // todo sterg asta si pun Adauga o reteta noua ca titlu
                modifier = modifier,
                step = 1,
            )
            NewStepTitle(text = stringResource(R.string.choose_a_category)) // probabil sterg si asta
        }
        ChooseCategoryContent(
            modifier = modifier
                .align(Alignment.Center),
            onNextClicked = onNextClicked,
        )
    }
}

@Composable
fun ChooseCategoryContent(
    modifier: Modifier = Modifier,
    onNextClicked: (ImageArguments?) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        CategoryItem(
            modifier = modifier,
            image = R.drawable.chefgirlcircle,
            categoryType = CategoryType.BREAKFAST,
            onClick = {
                onNextClicked(
                    ImageArguments(
                        category = CategoryType.BREAKFAST.string
                    )
                )
            }
        )
        Column(
            modifier = modifier
                .padding(start = 40.dp, end = 40.dp)
        ) {
            CategoryItem(
                modifier = modifier
                    .padding(bottom = 60.dp),
                image = R.drawable.chefgirlcircle,
                categoryType = CategoryType.MAIN_DISH,
                onClick = {
                    onNextClicked(
                        ImageArguments(
                            category = CategoryType.MAIN_DISH.string
                        )
                    )
                }
            )
            CategoryItem(
                modifier = modifier
                    .padding(top = 60.dp),
                image = R.drawable.chefgirlcircle,
                categoryType = CategoryType.SOUP,
                onClick = {
                    onNextClicked(
                        ImageArguments(
                            category = CategoryType.SOUP.string
                        )
                    )
                }
            )
        }
        CategoryItem(
            modifier = modifier,
            image = R.drawable.chefgirlcircle,
            categoryType = CategoryType.DESSERT,
            onClick = {
                onNextClicked(
                    ImageArguments(
                        category = CategoryType.DESSERT.string
                    )
                )
            }
        )
    }
}

@Composable
fun CategoryItem(
    modifier: Modifier,
    categoryType: CategoryType,
    image: Int,
    onClick: () -> Unit
) {
    val text = when (categoryType) {
        CategoryType.BREAKFAST -> stringResource(R.string.breakfast)
        CategoryType.MAIN_DISH -> stringResource(R.string.main_dish)
        CategoryType.SOUP -> stringResource(R.string.soup)
        CategoryType.DESSERT -> stringResource(R.string.dessert)
    }
    Column {
        Text(
            text = text
        )
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(70.dp)
                .clickable { onClick() }
        )
    }
}

@Composable
fun CategoryScreenBackground(
    modifier: Modifier = Modifier
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color.Transparent,
            MaterialTheme.colorScheme.secondary
        ),
//        start = Offset(0f, 0f),
//        end = Offset(sizeImage.width.toFloat(), sizeImage.height.toFloat() / 2),
    )

    Box() {
        Image(
            painter = painterResource(id = R.drawable.imagefoodbackgr),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = modifier
                .onGloballyPositioned {
                    sizeImage = it.size
                }
                .fillMaxSize(),
            alpha = 0.35f
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(gradient)
        )
    }
}