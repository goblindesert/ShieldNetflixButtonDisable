# Shield Netflix Button Disable

Android TV app for NVIDIA Shield that blocks the Netflix remote button using an Accessibility Service.

## What It Does

- Registers an Android Accessibility Service.
- Requests remote key-event filtering.
- Blocks the common Shield Netflix button keycode, `KEYCODE_BUTTON_12`.
- Provides a simple TV-friendly screen for opening Accessibility settings and checking keycodes.

## Install

Build the debug APK from Android Studio or with:

```bash
./gradlew assembleDebug
```

The APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

After installing on the Shield, enable the service in:

```text
Settings > Device Preferences > Accessibility
```
