package com.sachin.population

sealed class NavRoute(
  val route: String,
) {

  object Dashboard : NavRoute("dashboard")
  object StatePopulationDetails : NavRoute("state_population_details")
}