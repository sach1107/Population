package com.sachin.population.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun ImageComponent(resId: Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = resId),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
}