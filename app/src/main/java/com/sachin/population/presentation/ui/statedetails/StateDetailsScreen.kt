package com.sachin.population.presentation.ui.statedetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sachin.population.R
import com.sachin.population.domain.model.Data
import com.sachin.population.domain.model.Population
import com.sachin.population.presentation.common.*
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
    BaseComponent(
        title = stringResource(id = R.string.state_details),
        onBackIconClick = { navController.navigateUp() }
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
                    AlertDialogComponent(
                        title = uiScreenState.message,
                        confirmButtonText = stringResource(id = R.string.retry),
                        onConfirmButtonClick = {
                            errorDialogState.value = false
                            retryClick.invoke()
                        },
                    ) { errorDialogState.value = false }
                }
            }
        }
    }
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
                    ImageComponent(
                        resId = R.drawable.population,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        TextComponent(
                            text = stringResource(id = R.string.state_name, item.name),
                            modifier = Modifier.padding(top = 16.dp),
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        TextComponent(
                            text = stringResource(id = R.string.population, item.population),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
            }
            Divider()
        }
    }
}

@Preview
@Composable
private fun StateDetailsMainComponentPreview() {
    StateDetailsMainComponent(
        navController = rememberNavController(),
        uiScreenState = UiScreenState.Success(Population(provideDummyList())),
        errorDialogState = remember { mutableStateOf(false) },
        retryClick = { },
    )
}

@Preview
@Composable
private fun StatePopulationDetailsListComponentPreview() {
    StatePopulationDetailsListComponent(
        population = Population(provideDummyList())
    )
}

private fun provideDummyList(): List<Data> {
    return listOf(
        Data(
            id = "1",
            yearId = 2020,
            year = "2020",
            slugName = "s1",
            name = "state1",
            population = 1234
        ),
        Data(
            id = "2",
            yearId = 2020,
            year = "2020",
            slugName = "s2",
            name = "state2",
            population = 12234
        ),
        Data(
            id = "3",
            yearId = 2020,
            year = "2020",
            slugName = "s3",
            name = "state3",
            population = 1233
        ),
        Data(
            id = "4",
            yearId = 2020,
            year = "2020",
            slugName = "s4",
            name = "state4",
            population = 768
        )
    )
}