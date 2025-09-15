package arun.pkg.map_directions_data.model

import com.google.gson.annotations.SerializedName


data class MapDirectionsResponse(
    @SerializedName("geocoded_waypoints") var geocodedWaypoints: ArrayList<GeocodedWaypoints> = arrayListOf(),
    @SerializedName("routes") var routes: ArrayList<Routes> = arrayListOf(),
    @SerializedName("status") var status: String? = null
)