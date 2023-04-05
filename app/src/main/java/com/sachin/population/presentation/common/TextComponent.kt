package com.sachin.population.presentation.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sachin.core_design.theme.PopulationTheme

@Composable
fun TextComponent(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = PopulationTheme.typography.bodyMedium,
        modifier = modifier,
    )
}