package arun.pkg.vehicletrackingapp.compose

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import arun.pkg.map_directions_domain.model.VehicleTrackingData
import arun.pkg.vehicletrackingapp.carAnimator
import arun.pkg.vehicletrackingapp.getCarBitmap
import arun.pkg.vehicletrackingapp.getRotation
import arun.pkg.vehicletrackingapp.navigation.Navigation.NavArguments.ARG_SYNC_DURATION
import arun.pkg.vehicletrackingapp.viewmodel.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.widgets.ScaleBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.time.Duration.Companion.seconds


var syncDuration = 5

@Composable
fun MapScreen(
    navController: NavController,
    viewModel: MapViewModel = hiltViewModel(),
) {
    syncDuration = navController.currentBackStackEntry?.arguments?.getInt(ARG_SYNC_DURATION) ?: 5
    Log.d("MapScreen -- ", "syncDuration : $syncDuration")
    val mapDirectionsList = viewModel.mapDirectionsList.collectAsState()
    val vehicleTrackingData = viewModel.vehicleTrackingData.collectAsState()

     if (mapDirectionsList.value.isNotEmpty()) {
        VehicleTrackingMap(mapDirectionsList.value, vehicleTrackingData.value) {
            navController.popBackStack()
        }
     }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun VehicleTrackingMap(
    mapDirectionsList: List<LatLng>,
    vehicleTrackingData: VehicleTrackingData,
    onStopTrip: () -> Unit = {}
) {
    var isMapLoaded by remember { mutableStateOf(false) }
    var isTripStarted by remember { mutableStateOf(false) }
    var isCarMovement by remember { mutableStateOf(false) }
    var previousLatLng by remember { mutableStateOf(mapDirectionsList[0]) }
    var currentLatLng by remember { mutableStateOf(mapDirectionsList[0]) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var endAnimationLatLng by remember { mutableStateOf(mapDirectionsList[0]) }
    var syncTimeLag by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    Column {
        Row {
            Button(
                onClick = {
                    if (isTripStarted) {
                        onStopTrip()
                    } else {
                        isTripStarted = true
                    }
                }) {
                if (isTripStarted) {
                    Text(text = "Stop Trip")
                } else {
                    Text(text = "Start Trip")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Text(text = "Total Distance : ${vehicleTrackingData.totalDistance}")
                Text(text = "Total Duration : ${vehicleTrackingData.totalDuration}")
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Add GoogleMap here
            val currentLocation = LatLng(30.633740000000003, 76.81812000000001)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(currentLocation, 15f)
            }

            GoogleMap(
                cameraPositionState = cameraPositionState,
                onMapLoaded = { isMapLoaded = true }
            ) {
                // Log.d("MapScreen -- ", "mapDirectionsList : $mapDirectionsList")
                // Add your map-related content here
                val builder = LatLngBounds.Builder()
                for (latLng in mapDirectionsList) {
                    builder.include(latLng)
                }
                // this is used to set the bound of the Map
                val bounds = builder.build()
                LaunchedEffect(key1 = true) {
                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newLatLngBounds(bounds, 200),
                        durationMs = 1000
                    )
                }

                Polyline(
                    points = mapDirectionsList, color = Color.Gray
                )

                Marker(
                    state = MarkerState(position = mapDirectionsList[0]),
                    title = "Start",
                    snippet = "Marker in start location",
                    icon = BitmapDescriptorFactory.fromBitmap(getDestinationBitmap())
                )

                Marker(
                    state = MarkerState(position = mapDirectionsList[mapDirectionsList.size - 1]),
                    title = "Destination",
                    snippet = "Marker in end location",
                    icon = BitmapDescriptorFactory.fromBitmap(getDestinationBitmap())
                )

                if (isTripStarted) {
                    LaunchedEffect(Unit) {
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f),
                            durationMs = 1200
                        )
                        delay(2000L)
                        isCarMovement = true
                    }
                    if (isCarMovement) {
                        GetNextLocation(mapDirectionsList = mapDirectionsList,
                            onNextLatLng = { nextLatLng ->
                                endAnimationLatLng = nextLatLng
                                previousLatLng = currentLatLng

                                rotation = getRotation(previousLatLng, nextLatLng)
                                val valueAnimator = carAnimator(syncDuration)
                                valueAnimator.addUpdateListener { va ->
                                    val multiplier = va.animatedFraction
                                    val nextLocation = LatLng(
                                        multiplier * nextLatLng.latitude + (1 - multiplier) * previousLatLng.latitude,
                                        multiplier * nextLatLng.longitude + (1 - multiplier) * previousLatLng.longitude
                                    )
                                    currentLatLng = nextLocation
                                }
                                valueAnimator.start()
                            },
                            onReachedDestination = {
                                Toast.makeText(context, "Destination Reached", Toast.LENGTH_LONG)
                                    .show()
                            })
                        LaunchedEffect(key1 = endAnimationLatLng) {
                            var startTime = 0
                            while (true) {
                                delay(1.seconds)
                                startTime++
                                syncTimeLag = startTime
                            }
                        }
                        Marker(
                            state = MarkerState(position = currentLatLng),
                            flat = true,
                            title = "Truck Location",
                            snippet = "Marker in current truck location",
                            rotation = rotation,
                            anchor = Offset(0.5f, 0.5f),
                            icon = BitmapDescriptorFactory.fromBitmap(getCarBitmap(context))
                        )

                        LaunchedEffect(key1 = endAnimationLatLng) {
                            snapshotFlow { endAnimationLatLng }
                                .distinctUntilChanged()
                                .collectLatest {
                                    val carMovementBounds = LatLngBounds.Builder()
                                        .include(previousLatLng)
                                        .include(endAnimationLatLng)
                                        .build()
                                    cameraPositionState.animate(
                                        update = CameraUpdateFactory.newLatLngBounds(
                                            carMovementBounds,
                                            400
                                        ),
                                        durationMs = 500
                                    )
                                }
                        }
                    }
                }
            }
            ScaleBar(
                modifier = Modifier
                    .padding(top = 5.dp, end = 15.dp)
                    .align(Alignment.TopEnd),
                cameraPositionState = cameraPositionState
            )

            if (isTripStarted) {
                Text(
                    text = "Last Sync: $syncTimeLag sec ago",
                    modifier = Modifier
                        .padding(4.dp)
                        .background(Color.LightGray, RoundedCornerShape(4.dp))
                        .padding(4.dp)
                )
            }
        }
    }
}

//fun getMarkerIcon(context: Context, syncTimeLag: Int): BitmapDescriptor {
//    return BitmapDescriptorFactory.fromBitmap(getCarBitmap(context, syncTimeLag))
//}

//@Composable
//fun StartSyncLagTimer(
//    isRunning: Boolean,
//    onSyncLagTime: (Int) -> Unit = {},
//) {
//    LaunchedEffect(Unit) {
//        var startTime = 0
//        while (isRunning) {
//            delay(1.seconds)
//            startTime++
//            onSyncLagTime(startTime)
//        }
//    }
//}

@Composable
private fun GetNextLocation(
    mapDirectionsList: List<LatLng>,
    onNextLatLng: (LatLng) -> Unit = {},
    onReachedDestination: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        var index = 1
        repeat(mapDirectionsList.size - 1) {
            onNextLatLng(mapDirectionsList[index])
            index++
            if (index == mapDirectionsList.size) {
                onReachedDestination()
            }
            delay(syncDuration * 1000L)
        }
    }
}

fun getDestinationBitmap(): Bitmap {
    val height = 30
    val width = 30
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    val canvas = Canvas(bitmap)
    val paint = Paint()
    paint.color = android.graphics.Color.BLACK
    paint.style = Paint.Style.FILL
    paint.isAntiAlias = true
    canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paint)
    return bitmap
}

@Preview
@Composable
private fun PreviewMountainMap() {
    VehicleTrackingMap(emptyList(), VehicleTrackingData("", "", ""))
}
