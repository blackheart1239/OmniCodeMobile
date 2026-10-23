# OmniCode Mobile

![OmniCode Mobile logo](omnicode-logo.png)

**OmniCode Mobile** is an Android-first coding workspace designed to run supported languages locally, including when the phone has no internet connection.

> **Current verified scope:** JavaScript and Python are configured for offline execution. C and C++ are the next planned integration; they are **not executable offline in this build yet**.

## Language support

| Language | Status | Internet needed to run? |
|---|---|---|
| JavaScript | Local execution in Android WebView | No |
| Python 3.12 | Embedded CPython through Chaquopy | No |
| C | Offline compiler not included yet | Not available in this build |
| C++ | Offline compiler not included yet | Not available in this build |
| Java, C#, Go, Rust, PHP, Ruby, Kotlin, Bash | Runtime/compiler not included yet | Not available in this build |

The app does not call Piston or another online execution API. Unsupported language selections display a clear message rather than pretending the code compiled.

## What's in v0.3.0

- Refreshed dark mobile UI with the OmniCode launcher logo.
- Language selector groups available offline languages separately from planned toolchains.
- Updated editor, output panel, runtime status, and clearer unsupported-language messages.
- JavaScript execution locally in the WebView.
- Python 3.12 execution using Chaquopy.
- Fixed Python output newline handling.
- Updated README and version metadata.

## Build an APK from a phone using GitHub Actions

This project can be built in GitHub Actions; Android Studio is not required on your phone.

1. Download the source ZIP and upload it to your repository as `OmniCodeMobile-source.zip`.
2. Keep the project structure inside the ZIP. The ZIP should contain an `OmniCodeMobile/` directory with `settings.gradle.kts`, `build.gradle.kts`, `app/`, and `README.md`.
3. Keep the existing workflow at `.github/workflows/android.yml`. It should extract `OmniCodeMobile-source.zip`, find `settings.gradle.kts`, run `assembleDebug`, and upload `app/build/outputs/apk/debug/app-debug.apk`.
4. Open your repository's **Actions** tab and run **Build OmniCode Mobile APK**.
5. When the run succeeds, download the `OmniCodeMobile-debug-apk` artifact. Extract it if needed and install the APK.

For this project configuration, use Java 17, Android Gradle Plugin 8.9.1, Gradle 8.11.1, and Chaquopy 16.1.0. The workflow installs Android platform 35 and build tools 35.0.0.

## Offline C/C++ plan

C and C++ need a real Android-compatible native toolchain. This cannot be enabled by only adding language names or starter code to the UI. A compiler integration must:

1. Include or build an Android-compatible compiler for the device ABI (`arm64-v8a` for most modern phones).
2. Package the compiler, headers, runtime libraries, and required license notices.
3. Run compilation and execution in the app's private storage with suitable error handling.
4. Be tested on-device with basic programs, compiler errors, and offline mode.

This version deliberately does not claim C/C++ support until those pieces are integrated and verified. The UI is prepared to identify them as planned rather than misleading users.

## Known limitations

- C/C++ and the other planned languages are not executable in this build.
- No project/file browser, persistent workspace manager, export, syntax highlighting, autocomplete, stdin panel, debugger, or package manager yet.
- JavaScript execution uses a WebView JavaScript context and is intended for learning snippets, not as a hardened sandbox for untrusted code.
- The initial APK build requires internet access to download Gradle/Android/Chaquopy dependencies. After installation, supported JavaScript and Python execution is local.

## Project layout

```text
OmniCodeMobile/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── assets/
│       │   ├── index.html
│       │   └── omnicode-logo.png
│       ├── java/com/omnicodemobile/app/MainActivity.kt
│       └── python/omni_runner.py
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── omnicode-logo.png
└── README.md
```

## License

Add a project license before redistributing this app. Any future third-party compiler/runtime must retain its required license notices and comply with its redistribution terms.
