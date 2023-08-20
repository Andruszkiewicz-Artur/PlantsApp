package com.example.plantsapp.core.unit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.plantsapp.core.unit.navigation.plants.plantsNavGraph

@Composable
fun RootNavHost(
    navHostController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = Graph.PLANTS
    ) {
        plantsNavGraph(navHostController = navHostController)
    }

}