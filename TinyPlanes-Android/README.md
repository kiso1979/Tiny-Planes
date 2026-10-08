# Tiny Planes (Android)

A thin native Android shell around the HTML game in `app/src/main/assets/index.html`.

Tuned for the Galaxy S24: locked to landscape (either way round), true full screen with
no status or navigation bar (swipe from the edge to peek at them), drawn edge to edge
including the camera punch-hole side, 120 Hz requested, screen kept awake while playing,
vibration on hits, and the game pauses when you leave the app.

To update the game later, replace `app/src/main/assets/index.html` and rebuild.

## Build option A: GitHub (nothing to install)

1. Create a new **private** repository on github.com.
2. Upload the contents of this folder (including the hidden `.github` folder) and commit to `main`.
3. Open the **Actions** tab. The "Build APK" workflow runs automatically (about 3-5 minutes).
4. When it's green, open **Releases** (right-hand side of the repo page) on the S24
   and tap `TinyPlanes-buildN.apk` to download it.

## Build option B: Android Studio

1. Open this folder in Android Studio and let it sync.
2. Build > Build App Bundle(s) / APK(s) > Build APK(s).
3. The APK lands in `app/build/outputs/apk/debug/app-debug.apk`.

## Installing on the S24

Open the downloaded APK. The first time, Android asks to allow installs from that app
(for example Chrome or My Files): tap Settings, allow it, then go back and tap Install.
This is a debug build signed with a test key; that's normal for play-testing.

App id: `com.kim.tinyplanes` (change it before any Play Store release if you like).
