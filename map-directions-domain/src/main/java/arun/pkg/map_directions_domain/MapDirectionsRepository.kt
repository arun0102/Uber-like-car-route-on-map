package arun.pkg.map_directions_domain

import arun.pkg.map_directions_domain.model.VehicleTrackingData

interface MapDirectionsRepository {
    suspend fun getDirections(
        currentLatitude: Double,
        currentLongitude: Double,
        endDestination: String
    ): VehicleTrackingData
}