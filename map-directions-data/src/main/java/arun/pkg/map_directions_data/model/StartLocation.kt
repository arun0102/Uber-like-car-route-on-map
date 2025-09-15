package arun.pkg.map_directions_data.model

import com.google.gson.annotations.SerializedName


data class StartLocation(

    @SerializedName("lat") var lat: Double? = null,
    @SerializedName("lng") var lng: Double? = null

)