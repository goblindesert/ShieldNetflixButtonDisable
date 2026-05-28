package com.example.shieldnetflixbuttondisable

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent

class NetflixButtonBlockerService : AccessibilityService() {

    override fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode in BLOCKED_KEY_CODES) {
            if (event.action == KeyEvent.ACTION_DOWN) {
                Log.i(TAG, "Blocked key ${event.keyCode} (${KeyEvent.keyCodeToString(event.keyCode)})")
            }
            return true
        }

        Log.d(TAG, "Allowed key ${event.keyCode} (${KeyEvent.keyCodeToString(event.keyCode)})")
        return false
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) = Unit

    override fun onInterrupt() = Unit

    private companion object {
        private const val TAG = "NetflixButtonBlocker"

        private val BLOCKED_KEY_CODES = setOf(
            KeyEvent.KEYCODE_BUTTON_12
        )
    }
}
