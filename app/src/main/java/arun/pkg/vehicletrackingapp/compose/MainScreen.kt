package arun.pkg.vehicletrackingapp.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import arun.pkg.vehicletrackingapp.navigation.Navigation
import arun.pkg.vehicletrackingapp.navigation.Navigation.NavArguments.ARG_SYNC_DURATION

@Composable
@Preview
private fun PreviewMainScreen() {
    MainScreenView()
}

@Composable
fun MainScreen(navController: NavController) {
    MainScreenView(
        onDurationSelected = { duration ->
            navController.navigate("${Navigation.Path.PATH_MAP_SCREEN} + /${duration}")
        },
    )
}

@Composable
private fun MainScreenView(
    onDurationSelected: (Int) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                Color.White
            )
            .padding(20.dp)
    ) {
        Button(onClick = {
            onDurationSelected(5)
        }) {
            Text(text = "5 Second Delay")
        }
        Spacer(modifier = Modifier.padding(16.dp))

        Button(onClick = {
            onDurationSelected(10)
        }) {
            Text(text = "10 Seconds Delay")
        }
        Spacer(modifier = Modifier.padding(16.dp))

        Button(onClick = {
            onDurationSelected(15)
        }) {
            Text(text = "15 Seconds Delay")
        }
        Spacer(modifier = Modifier.padding(16.dp))

        Button(onClick = {
            onDurationSelected(20)
        }) {
            Text(text = "20 Seconds Delay")
        }
        Spacer(modifier = Modifier.padding(16.dp))
    }
}



