# MP3King APK

App nativa Android per [mp3king.vercel.app](https://mp3king.vercel.app), basata su Capacitor.

A differenza di una WebView "nuda", questa è un involucro nativo vero: splash screen nativa, status bar gestita nativamente, bridge JS per plugin nativi (push notifications, ecc.), tutto mentre il contenuto carica live dal sito (`server.url` in `capacitor.config.json`). Stesso identico approccio usato da app come quella di ChatGPT.

## Come funziona

- L'app carica **live** `https://mp3king.vercel.app` ad ogni avvio: zero rebuild quando aggiorni il sito.
- I plugin Capacitor (`@capacitor/push-notifications`, `@capacitor/splash-screen`, `@capacitor/status-bar`, `@capacitor/app`) sono disponibili automaticamente nella pagina via `window.Capacitor`, anche se il sito è remoto, perché il bridge JS viene iniettato dalla WebView nativa ad ogni caricamento.

## Build locale

Serve Android Studio (o solo Android SDK + JDK 17) installato sulla tua macchina.

```bash
npm install
npx cap sync android
npx cap open android   # apre Android Studio
```

Da Android Studio: Build > Build Bundle(s)/APK(s) > Build APK(s).

## Build automatica (GitHub Actions)

Ad ogni push su `main` parte `.github/workflows/build-apk.yml`, che compila un APK debug e lo carica come artifact scaricabile dalla tab Actions della repo.

## Icona / Splash

Sostituisci le immagini in `android/app/src/main/res/mipmap-*/ic_launcher*.png` con il logo MP3King (preso dalla repo principale `mp3king`, file logo jpk). Per generare automaticamente tutte le risoluzioni puoi usare [Capacitor Assets](https://github.com/ionic-team/capacitor-assets):

```bash
npm install @capacitor/assets --save-dev
npx capacitor-assets generate --android
```
mettendo `assets/icon.png` (1024x1024) e `assets/splash.png` (2732x2732) prima di lanciare il comando.

## Push Notifications

Richiede un progetto Firebase:

1. Crea un progetto su [Firebase Console](https://console.firebase.google.com), aggiungi un'app Android con package name `app.mp3king.android`.
2. Scarica `google-services.json` e mettilo in `android/app/google-services.json` (build locale), oppure:
3. Per la build via GitHub Actions: codifica il file in base64 (`base64 -w0 google-services.json`) e salvalo come secret della repo chiamato `GOOGLE_SERVICES_JSON`.

Senza `google-services.json` l'app si builda comunque, ma le push non funzionano (il plugin lo rileva e si disattiva da solo, vedi log Gradle).

## Package name

`app.mp3king.android` — modificabile in `capacitor.config.json` (`appId`) prima del primo `cap add android` (se già aggiunto, va cambiato anche dentro `android/`, più complesso).
