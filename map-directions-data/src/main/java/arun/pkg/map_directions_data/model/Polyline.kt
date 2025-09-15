package arun.pkg.map_directions_data.model

import com.google.gson.annotations.SerializedName


data class Polyline(

    @SerializedName("points") var points: String? = null

)