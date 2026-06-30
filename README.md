# MP3King APK

Native Android app for [mp3king.vercel.app](https://mp3king.vercel.app), built with Capacitor.

Unlike a raw WebView, this is a real native shell: native splash screen, natively managed status bar, JS bridge for native plugins (push notifications, etc.), while the content loads live from the site (`server.url` in `capacitor.config.json`). Same approach used by apps like the official ChatGPT app.

## How it works

- The app loads `https://mp3king.vercel.app` **live** on every launch: zero rebuilds needed when you update the site.
- Capacitor plugins (`@capacitor/push-notifications`, `@capacitor/splash-screen`, `@capacitor/status-bar`, `@capacitor/app`) are automatically available on the page via `window.Capacitor`, even though the site is remote, because the native WebView injects the JS bridge on every page load.

## Local build

You need Android Studio (or just the Android SDK + JDK 17) installed on your machine.

```bash
npm install
npx cap sync android
npx cap open android   # opens Android Studio
```

From Android Studio: Build > Build Bundle(s)/APK(s) > Build APK(s).

## Automated build (GitHub Actions)

On every push to `main`, `.github/workflows/build-apk.yml` runs, compiling a debug APK and uploading it as a downloadable artifact in the repo's Actions tab.

## Icon / Splash

Already generated from the MP3King logo, applied across all densities (mdpi to xxxhdpi, round + adaptive icon, landscape/portrait splash screens). To regenerate from a new source image, drop it at `assets/icon-source.jpg` and re-run the resize script (ask Claude, or use any Capacitor asset generator once `sharp`'s binary download isn't blocked on your network).

## Push Notifications

Requires a Firebase project:

1. Create a project at [Firebase Console](https://console.firebase.google.com), add an Android app with package name `app.mp3king.android`.
2. Download `google-services.json` and place it at `android/app/google-services.json` (local build), or:
3. For GitHub Actions builds: base64-encode the file (`base64 -w0 google-services.json`) and save it as a repo secret named `GOOGLE_SERVICES_JSON`.

Without `google-services.json` the app still builds fine, but push won't work (the plugin detects this and disables itself — see Gradle logs).

## Package name

`app.mp3king.android` — change it in `capacitor.config.json` (`appId`) before the first `cap add android` (if already added, it also needs changing inside `android/`, which is more involved).
