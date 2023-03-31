package com.sachin.population.presentation.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sachin.population.NavRoute
import com.sachin.population.R
import com.sachin.population.domain.model.Population
import com.sachin.population.presentation.common.ProgressComponent
import com.sachin.population.presentation.state.UiScreenState
import com.sachin.population.presentation.viewmodel.dashboard.DashboardViewModel

@Composable
fun DashboardScreen(
    navController: NavHostController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value
    val errorDialogState = remember { mutableStateOf(false) }

    DashboardMainComponent(
        state = state,
        errorDialogState = errorDialogState,
        retryClick = viewModel::retry
    ) { year ->
        navController.navigate(NavRoute.StatePopulationDetails.route + "/${year}")
    }
}

@Composable
private fun DashboardMainComponent(
    state: UiScreenState,
    errorDialogState: MutableState<Boolean>,
    retryClick: () -> Unit,
    itemClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.usa_population))
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
                elevation = 10.dp
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(vertical = 12.dp),
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    when (state) {
                        is UiScreenState.Progress -> ProgressComponent()
                        is UiScreenState.Success -> {
                            PopulationListComponent(
                                population = state.population,
                                itemClick = itemClick
                            )
                        }
                        is UiScreenState.Error -> {
                            errorDialogState.value = true
                            ShowErrorDialogComponent(errorDialogState, state.message, retryClick)
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun PopulationListComponent(
    population: Population,
    itemClick: (String) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(population.data) { item ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { itemClick.invoke(item.year) },
            ) {
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.population),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.align(Alignment.CenterVertically),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = stringResource(id = R.string.year, item.year),
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(top = 16.dp),
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(id = R.string.population, item.population),
                            style = MaterialTheme.typography.body1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
            }
            Divider()
        }
    }
}

@Composable
private fun ShowErrorDialogComponent(
    errorDialogState: MutableState<Boolean>,
    message: String,
    retryClick: () -> Unit
) {
    if (errorDialogState.value) {
        AlertDialog(
            onDismissRequest = { errorDialogState.value = false },

            title = { Text(text = message) },

            confirmButton = {
                TextButton(
                    onClick = {
                        errorDialogState.value = false
                        retryClick.invoke()
                    }
                ) {
                    Text("Retry")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        errorDialogState.value = false
                    }
                ) {
                    Text("Cancel")
                }
            },
        )
    }
}