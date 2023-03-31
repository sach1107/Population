package com.sachin.population.presentation.ui.statedetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.sachin.population.R
import com.sachin.population.domain.model.Population
import com.sachin.population.presentation.common.ProgressComponent
import com.sachin.population.presentation.state.UiScreenState
import com.sachin.population.presentation.viewmodel.statedetails.StateDetailsViewModel

@Composable
fun StateDetailsScreen(
    navController: NavHostController,
    viewModel: StateDetailsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val errorDialogState = remember { mutableStateOf(false) }

    StateDetailsMainComponent(
        navController = navController,
        uiScreenState = uiState,
        errorDialogState = errorDialogState,
        retryClick = viewModel::retry
    )
}

@Composable
private fun StateDetailsMainComponent(
    navController: NavHostController,
    uiScreenState: UiScreenState,
    errorDialogState: MutableState<Boolean>,
    retryClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.state_details))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
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
                    when (uiScreenState) {
                        is UiScreenState.Progress -> ProgressComponent()
                        is UiScreenState.Success -> {
                            StatePopulationDetailsListComponent(
                                population = uiScreenState.population,
                            )
                        }
                        is UiScreenState.Error -> {
                            errorDialogState.value = true
                            ShowErrorDialogComponent(
                                errorDialogState,
                                uiScreenState.message,
                                retryClick
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun StatePopulationDetailsListComponent(
    population: Population,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(population.data) { item ->
            Column(
                modifier = Modifier.fillMaxWidth()
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
                            text = stringResource(id = R.string.state_name, item.name),
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