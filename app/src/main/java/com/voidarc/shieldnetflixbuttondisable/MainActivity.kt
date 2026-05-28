package com.voidarc.shieldnetflixbuttondisable

import android.content.ComponentName
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.voidarc.shieldnetflixbuttondisable.ui.theme.ShieldNetflixButtonDisableTheme

class MainActivity : ComponentActivity() {

    private var serviceEnabled by mutableStateOf(false)

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ShieldNetflixButtonDisableTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    MainScreen(
                        serviceEnabled = serviceEnabled
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        serviceEnabled = isBlockerServiceEnabled()
    }

    private fun isBlockerServiceEnabled(): Boolean {
        val expectedService = ComponentName(
            this,
            NetflixButtonBlockerService::class.java
        ).flattenToString()

        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        return enabledServices
            .split(':')
            .any { it.equals(expectedService, ignoreCase = true) }
    }

}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun MainScreen(
    serviceEnabled: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 48.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = null,
            modifier = Modifier.size(118.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Shield Netflix Button Disable",
            fontSize = 26.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(14.dp))
        StatusPanel(
            enabled = serviceEnabled
        )
        Spacer(modifier = Modifier.height(14.dp))
        InfoPanel(
            title = "Setup",
            body = "Open Settings > Device Preferences > Accessibility > Shield Netflix Button Disable, then switch it On."
        )
        Spacer(modifier = Modifier.height(14.dp))
        InfoPanel(
            title = "Troubleshooting",
            body = "If Netflix still opens, turn the service Off and On again. If it still happens, restart the Shield."
        )
    }
}

@Composable
private fun StatusPanel(enabled: Boolean) {
    InfoPanel(
        title = if (enabled) "Status: ON" else "Status: OFF",
        body = if (enabled) {
            "The accessibility service is enabled. The Netflix button should now be blocked."
        } else {
            "Turn it on in Shield Accessibility settings."
        }
    )
}

@Composable
private fun InfoPanel(
    title: String,
    body: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(horizontal = 12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = body,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
