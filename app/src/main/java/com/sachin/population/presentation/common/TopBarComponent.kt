package com.sachin.population.presentation.common

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sachin.core_design.theme.PopulationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppComponent(
    title: String,
    onBackIconClick: (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = PopulationTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            onBackIconClick?.let {
                IconButton(onClick = {
                    onBackIconClick.invoke()
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
                }
            }
        },
        colors = centerAlignedTopAppBarColors(
            containerColor = PopulationTheme.colorScheme.primary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}