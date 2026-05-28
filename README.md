# Shield Netflix Button Disable

Android TV app for NVIDIA Shield that blocks the Netflix remote button using an Accessibility Service.

## What It Does

- Registers an Android Accessibility Service.
- Requests remote key-event filtering.
- Blocks the common Shield Netflix button keycode, `KEYCODE_BUTTON_12`.
- Provides a simple TV-friendly screen for opening Accessibility settings and checking keycodes.

## Install

### Downloader

On the NVIDIA Shield, install and open Downloader by AFTVnews, then enter:

```text
8432913
```

Short URL:

```text
https://aftv.news/8432913
```

Direct APK URL:

```text
https://github.com/goblindesert/ShieldNetflixButtonDisable/releases/download/v1.0.1/ShieldNetflixButtonDisable-v1.0.1.apk
```

### Build Locally

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
