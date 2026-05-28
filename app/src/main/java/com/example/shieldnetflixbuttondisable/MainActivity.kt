package com.example.shieldnetflixbuttondisable

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.example.shieldnetflixbuttondisable.ui.theme.ShieldNetflixButtonDisableTheme

class MainActivity : ComponentActivity() {

    private var serviceStatus by mutableStateOf("Checking accessibility service...")
    private var buttonTestLabel by mutableStateOf("After enabling the service, press the Netflix button. If Netflix does not open, the blocker is working.")

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
                        serviceStatus = serviceStatus,
                        buttonTestLabel = buttonTestLabel,
                        onOpenAccessibilitySettings = ::openAccessibilitySettings
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        serviceStatus = if (isBlockerServiceEnabled()) {
            "Service enabled. The Netflix button should now be blocked."
        } else {
            "Service disabled. Turn it on in Shield Accessibility settings."
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        buttonTestLabel = if (keyCode == KeyEvent.KEYCODE_BUTTON_12) {
            "Netflix button detected. Enable the service, then press it again to confirm Netflix stays closed."
        } else {
            "Remote button detected. To test blocking, press the Netflix button after the service is enabled."
        }
        return super.onKeyDown(keyCode, event)
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

    private fun openAccessibilitySettings() {
        if (tryOpenSettingsAction(Settings.ACTION_ACCESSIBILITY_SETTINGS)) {
            return
        }

        if (tryOpenSettingsAction(Settings.ACTION_SETTINGS)) {
            Toast.makeText(
                this,
                "Turn on Shield Netflix Button Disable in Accessibility.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        serviceStatus = "Open Shield Settings manually and turn the accessibility service on."
    }

    private fun tryOpenSettingsAction(action: String): Boolean {
        return try {
            startActivity(Intent(action))
            true
        } catch (_: ActivityNotFoundException) {
            false
        } catch (_: SecurityException) {
            false
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun MainScreen(
    serviceStatus: String,
    buttonTestLabel: String,
    onOpenAccessibilitySettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = null,
            modifier = Modifier.size(160.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Shield Netflix Button Disable",
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = serviceStatus,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(22.dp))
        InfoPanel(
            title = "Setup",
            body = "Open Settings > Device Preferences > Accessibility > Shield Netflix Button Disable, then switch it On."
        )
        Spacer(modifier = Modifier.height(14.dp))
        InfoPanel(
            title = "Button Test",
            body = buttonTestLabel
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onOpenAccessibilitySettings) {
            Text(text = "Try Opening Settings")
        }
    }
}

@Composable
private fun InfoPanel(
    title: String,
    body: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.82f)
            .padding(horizontal = 12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = body,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
