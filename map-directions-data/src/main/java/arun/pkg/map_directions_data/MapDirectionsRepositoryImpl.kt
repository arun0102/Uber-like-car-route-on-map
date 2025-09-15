package arun.pkg.map_directions_data

import arun.pkg.map_directions_data.service.MapDirectionsServices
import arun.pkg.map_directions_domain.MapDirectionsRepository
import arun.pkg.map_directions_domain.model.VehicleTrackingData

class MapDirectionsRepositoryImpl(
    private val mapDirectionsServices: MapDirectionsServices
) : MapDirectionsRepository {
    override suspend fun getDirections(
        currentLatitude: Double,
        currentLongitude: Double,
        endDestination: String
    ): VehicleTrackingData {
        val result = mapDirectionsServices.directionsRequest()
        return VehicleTrackingData(
            polyline = result.routes[0].overviewPolyline?.points ?: "",
            totalDistance = result.routes[0].legs[0].distance?.text ?: "",
            totalDuration = result.routes[0].legs[0].duration?.text ?: ""
        )


//        val locationList = ArrayList<LatLong>().apply {
//            add(LatLong(28.436970000000002, 77.11272000000001))
//            add(LatLong(28.43635, 77.11289000000001))
//            add(LatLong(28.4353, 77.11317000000001))
//            add(LatLong(28.435280000000002, 77.11332))
//            add(LatLong(28.435350000000003, 77.11368))
//            add(LatLong(28.4356, 77.11498))
//            add(LatLong(28.435660000000002, 77.11519000000001))
//            add(LatLong(28.43568, 77.11521))
//            add(LatLong(28.436580000000003, 77.11499))
//            add(LatLong(28.436590000000002, 77.11507))
//        }
//        return locationList
    }
}