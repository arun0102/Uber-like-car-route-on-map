package arun.pkg.vehicletrackingapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arun.pkg.map_directions_domain.MapDirectionsRepository
import arun.pkg.map_directions_domain.model.LatLong
import arun.pkg.map_directions_domain.model.VehicleTrackingData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapDirectionsRepository: MapDirectionsRepository
) : ViewModel() {

    private var _mapDirectionsList: MutableStateFlow<List<LatLng>> = MutableStateFlow(emptyList())
    val mapDirectionsList: StateFlow<List<LatLng>>
        get() = _mapDirectionsList

    private var _vehicleTrackingData: MutableStateFlow<VehicleTrackingData> = MutableStateFlow(VehicleTrackingData("", "", ""))
    val vehicleTrackingData: StateFlow<VehicleTrackingData>
        get() = _vehicleTrackingData

    init {
        getMapDirections()
    }

    private fun getMapDirections() {
        viewModelScope.launch {
            val results =
                mapDirectionsRepository.getDirections(40.56679946908994, -74.13508765939723, "Oakwood Heights, Staten Island, NY 10306, USA")
            _vehicleTrackingData.value = results
            val latLngs = PolyUtil.decode(results.polyline)
            _mapDirectionsList.value = latLngs
        }
    }
}

private fun List<LatLong>.toLatLng(): List<LatLng> {
    return map { LatLng(it.latitude, it.longitude) }
}
