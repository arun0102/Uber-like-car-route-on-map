package arun.pkg.vehicletrackingapp.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import arun.pkg.vehicletrackingapp.compose.MapScreen
import arun.pkg.vehicletrackingapp.compose.MainScreen
import arun.pkg.vehicletrackingapp.navigation.Navigation
import arun.pkg.vehicletrackingapp.navigation.Navigation.NavArguments.ARG_SYNC_DURATION

fun NavGraphBuilder.vehicleTrackingGraph(navController: NavController) {
    navigation(
        route = Navigation.Route.ROUTE_VEHICLE_TRACKING,
        startDestination = Navigation.Path.PATH_MAIN_SCREEN,
    ) {
        mainScreen(navController)
        mapScreen(navController)
    }
}
fun NavGraphBuilder.mainScreen(navController: NavController) {
    composable(
        route = Navigation.Path.PATH_MAIN_SCREEN
    ) {
        MainScreen(navController)
    }
}
fun NavGraphBuilder.mapScreen(navController: NavController) {
    composable(
        route = "${Navigation.Path.PATH_MAP_SCREEN} + /{${ARG_SYNC_DURATION}}",
        arguments = listOf(
            navArgument(name = ARG_SYNC_DURATION) {
                type = NavType.IntType
                defaultValue = 10
            },
        ),
    ) {
        MapScreen(navController)
    }
}