package com.sachin.population.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sachin.core_design.theme.PopulationTheme
import com.sachin.population.NavRoute
import com.sachin.population.presentation.ui.dashboard.DashboardScreen
import com.sachin.population.presentation.ui.statedetails.StateDetailsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PopulationTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                viewModel.startDestination?.let {
                    NavHost(navController = navController, startDestination = it.route) {
                        composable(NavRoute.Dashboard.route) {
                            DashboardScreen(navController = navController)
                        }
                        composable(NavRoute.StatePopulationDetails.route + "/{year}") {
                            StateDetailsScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}