package arun.pkg.vehicletrackingapp.navigation.host

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import arun.pkg.vehicletrackingapp.navigation.Navigation
import arun.pkg.vehicletrackingapp.navigation.graph.vehicleTrackingGraph

@Composable
fun VehicleTrackingHost(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Navigation.Route.ROUTE_VEHICLE_TRACKING
    ) {
        vehicleTrackingGraph(navHostController)
    }
}