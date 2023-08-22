package com.example.plantsapp.core.unit.navigation.plants

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.plantsapp.core.unit.navigation.Graph
import com.example.plantsapp.presentation.addEditAlarm.comp.AddEditPresentation
import com.example.plantsapp.presentation.home.comp.HomePresentation

fun NavGraphBuilder.plantsNavGraph(
    navHostController: NavHostController
) {

    navigation(
        route = Graph.PLANTS,
        startDestination = PlantsScreen.Home.route
    ) {

        composable(
            route = PlantsScreen.Home.route
        ) {
            HomePresentation(
                navHostController = navHostController
            )
        }

        composable(
            route = PlantsScreen.AddEdit.route + "?plantAlarmId={plantAlarmId}",
            arguments = listOf(
                navArgument("plantAlarmId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            AddEditPresentation(
                navHostController = navHostController
            )
        }

    }

}