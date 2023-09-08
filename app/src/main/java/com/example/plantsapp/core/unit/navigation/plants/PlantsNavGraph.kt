package com.example.plantsapp.core.unit.navigation.plants

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.example.plantsapp.core.Static
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
            route = PlantsScreen.Home.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${Static.DEEP_LINK_URI}/${Static.PRESENT_WATERING}={${Static.PRESENT_WATERING}}"
                }
            )
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