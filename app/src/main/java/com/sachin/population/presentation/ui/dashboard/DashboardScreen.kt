package com.sachin.population.presentation.ui.dashboard

import androidx.compose.foundation.clickable
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
import com.sachin.population.NavRoute
import com.sachin.population.R
import com.sachin.population.domain.model.Data
import com.sachin.population.domain.model.Population
import com.sachin.population.presentation.common.*
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
        retryClick = viewModel::retry,
    ) { route ->
        navController.navigate(route)
    }
}

@Composable
private fun DashboardMainComponent(
    state: UiScreenState,
    errorDialogState: MutableState<Boolean>,
    retryClick: () -> Unit,
    itemClick: (String) -> Unit,
) {
    BaseComponent(title = stringResource(id = R.string.usa_population)) {
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
                    AlertDialogComponent(
                        title = state.message,
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
private fun PopulationListComponent(
    population: Population,
    itemClick: (String) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(population.data) { item ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        itemClick.invoke(NavRoute.StatePopulationDetails.route + "/${item.year}")
                    },
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
                            text = stringResource(id = R.string.year, item.year),
                            modifier = Modifier.padding(top = 16.dp)
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
private fun DashboardMainComponentPreview() {
    DashboardMainComponent(
        state = UiScreenState.Success(Population(provideDummyList())),
        errorDialogState = remember { mutableStateOf(false) },
        retryClick = { },
        itemClick = {}
    )
}

@Preview
@Composable
private fun PopulationListComponentPreview() {
    PopulationListComponent(
        population = Population(provideDummyList())
    ) {}
}

private fun provideDummyList(): List<Data> {
    return listOf(
        Data(
            id = "1",
            yearId = 2020,
            year = "2020",
            slugName = "n1",
            name = "USA",
            population = 1239321
        ),
        Data(
            id = "2",
            yearId = 2019,
            year = "2019",
            slugName = "n2",
            name = "USA",
            population = 1203223
        ),
        Data(
            id = "3",
            yearId = 2018,
            year = "2018",
            slugName = "n3",
            name = "USA",
            population = 978222
        ),
        Data(
            id = "4",
            yearId = 2017,
            year = "2017",
            slugName = "n4",
            name = "USA",
            population = 833332
        )
    )
}