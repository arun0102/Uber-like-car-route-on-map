package arun.pkg.vehicletrackingapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.model.LatLng
import kotlin.math.abs
import kotlin.math.atan

fun getCarBitmap(context: Context): Bitmap {
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_truck)
    return Bitmap.createScaledBitmap(bitmap, 100, 100, false)
}

//fun getCarBitmap(context: Context, syncTimeLag: Int): Bitmap {
//    val bitmap =
//        when (syncTimeLag) {
//            in 0..1 -> BitmapFactory.decodeResource(context.resources, R.drawable.ic_truck_grey)
//            in 2..3 -> BitmapFactory.decodeResource(context.resources, R.drawable.ic_truck_yellow)
//            in 3..4 -> BitmapFactory.decodeResource(context.resources, R.drawable.ic_truck_orange)
//            else -> BitmapFactory.decodeResource(context.resources, R.drawable.ic_truck_red)
//        }
//    return Bitmap.createScaledBitmap(bitmap, 100, 100, false)
//}

fun carAnimator(syncDuration: Int): ValueAnimator {
    val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
    valueAnimator.duration = syncDuration * 1000L
    valueAnimator.interpolator = LinearInterpolator()
    return valueAnimator
}

fun getRotation(start: LatLng, end: LatLng): Float {
    val latDifference: Double = abs(start.latitude - end.latitude)
    val lngDifference: Double = abs(start.longitude - end.longitude)
    var rotation = -1F
    when {
        start.latitude < end.latitude && start.longitude < end.longitude -> {
            rotation = Math.toDegrees(atan(lngDifference / latDifference)).toFloat()
        }

        start.latitude >= end.latitude && start.longitude < end.longitude -> {
            rotation = (90 - Math.toDegrees(atan(lngDifference / latDifference)) + 90).toFloat()
        }

        start.latitude >= end.latitude && start.longitude >= end.longitude -> {
            rotation = (Math.toDegrees(atan(lngDifference / latDifference)) + 180).toFloat()
        }

        start.latitude < end.latitude && start.longitude >= end.longitude -> {
            rotation =
                (90 - Math.toDegrees(atan(lngDifference / latDifference)) + 270).toFloat()
        }
    }
    return rotation
}