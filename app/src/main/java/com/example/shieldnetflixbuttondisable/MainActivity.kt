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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
    private var lastKeyLabel by mutableStateOf("Press a remote button here to show its keycode.")

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
                        lastKeyLabel = lastKeyLabel,
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
            "Service disabled. Enable it in Android Accessibility settings."
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        lastKeyLabel = "Last key: $keyCode (${KeyEvent.keyCodeToString(keyCode)})"
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
                "Open Device Preferences > Accessibility.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        serviceStatus = "Open Shield Settings manually, then go to Device Preferences > Accessibility."
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
    lastKeyLabel: String,
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
            modifier = Modifier.size(180.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Shield Netflix Button Disable",
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = serviceStatus,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = lastKeyLabel,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(28.dp))
        Button(onClick = onOpenAccessibilitySettings) {
            Text(text = "Open Accessibility Settings")
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "If Shield cannot open that screen directly, use Settings > Device Preferences > Accessibility.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}
