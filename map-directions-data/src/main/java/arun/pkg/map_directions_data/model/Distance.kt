package arun.pkg.map_directions_data.model

import com.google.gson.annotations.SerializedName


data class Distance(

    @SerializedName("text") var text: String? = null,
    @SerializedName("value") var value: Int? = null

)