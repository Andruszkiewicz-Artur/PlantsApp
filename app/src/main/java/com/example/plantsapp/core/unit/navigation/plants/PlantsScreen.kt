package com.example.plantsapp.core.unit.navigation.plants

sealed class PlantsScreen(
    val route: String
) {

    object Home: PlantsScreen(
        route = "home_screen"
    )

    object AddEdit: PlantsScreen(
        route = "add_edit_screen"
    ) {
        fun sendPlantAlarmId(
            plantAlarmId: Int
        ): String {
            return this.route + "?plantAlarmId=$plantAlarmId"
        }
    }

}
