package arun.pkg.map_directions_data.service

import arun.pkg.map_directions_data.BuildConfig
import arun.pkg.map_directions_data.model.MapDirectionsResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface MapDirectionsServices {
    @POST("/maps/api/directions/json")
    suspend fun directionsRequest(
        @Query("mode") mode: String = "driving",
        @Query("transit_routing_preference") transitRoutingPreference: String = "less_driving",
        @Query("origin") origin: String = "40.56679946908994,-74.13508765939723",
        @Query("destination") destination: String = "Oakwood Heights, Staten Island, NY 10306, USA",
        @Query("key") key: String = BuildConfig.MAPS_API_KEY
    ): MapDirectionsResponse
}