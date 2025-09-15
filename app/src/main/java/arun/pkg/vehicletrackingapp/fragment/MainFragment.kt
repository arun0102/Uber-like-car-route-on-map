package arun.pkg.vehicletrackingapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.compose.rememberNavController
import arun.pkg.vehicletrackingapp.navigation.host.VehicleTrackingHost
import arun.pkg.vehicletrackingapp.ui.theme.VehicleTrackingAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = ComposeView(
        context = requireContext()
    ).apply {
        setContent {
            VehicleTrackingAppTheme {
                VehicleTrackingHost(navHostController = rememberNavController())
            }
        }
    }
}