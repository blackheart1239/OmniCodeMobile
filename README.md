# OmniCode Mobile

**OmniCode Mobile** is an Android coding workspace with a dark editor interface and on-device execution. This project is an early offline-runtime build, not yet a complete all-language compiler suite.

## Offline execution status

| Language | Status in this build | Internet required to run? |
|---|---|---|
| JavaScript | Runs locally in the Android WebView | No |
| Python 3.12 | Bundled CPython runtime via Chaquopy | No |
| C | Runtime/compiler not bundled yet | Not executable in this build |
| C++ | Runtime/compiler not bundled yet | Not executable in this build |
| Java | Compiler/runtime not bundled yet | Not executable in this build |
| C# | Compiler/runtime not bundled yet | Not executable in this build |
| Go, Rust, PHP, Ruby, Kotlin, Bash | Runtimes not bundled yet | Not executable in this build |

The language menu keeps the requested languages visible, but only JavaScript and Python execute offline in this version. Unsupported languages show an explanatory message. There is **no Piston API or remote code execution fallback** in this build.

### Why aren't all languages offline yet?

Each language needs a compatible compiler/runtime packaged for Android, correct CPU architecture support, license checks, and testing. C/C++ needs a native toolchain, Java needs a compiler and runtime suitable for Android, and C# needs a compatible .NET runtime/compiler. These components can make the APK substantially larger. It would be misleading to label those languages as working offline before their toolchains are included and tested.

## Features

- Dark mobile-first editor UI
- JavaScript execution offline
- Python execution offline using an embedded Python runtime
- Language-specific starter examples
- Output and error display
- App launcher icon and in-app OmniCode branding
- No account, API key, or network connection needed to execute the supported languages

## Build an APK

### Build with GitHub Actions from a phone

This project can be built in GitHub Actions, so you do not need Android Studio on your phone.

1. Upload the contents of this project to your GitHub repository, keeping the folder structure.
2. Ensure your repository contains a workflow at `.github/workflows/android.yml` which builds the Android app and uploads `app/build/outputs/apk/debug/app-debug.apk` as an artifact.
3. Open the repository on GitHub and select **Actions**.
4. Open the Android build workflow and tap **Run workflow** (or push a commit to trigger it).
5. When it succeeds, open that workflow run, download the APK artifact, extract it, and install the APK on your Android phone.

If you are replacing an older version, keep your existing repository and workflow. Upload the updated project files with their original paths rather than flattening the folder structure.

### Build with Android Studio

Requirements:
- JDK 17
- Android SDK Platform 35
- Gradle compatible with Android Gradle Plugin 8.7.3
- Internet for the initial Gradle and Chaquopy dependency/runtime downloads

Open the `OmniCodeMobile` folder in Android Studio, allow Gradle sync, then choose **Build → Build Bundle(s) / APK(s) → Build APK(s)**. The debug APK should be at:

`app/build/outputs/apk/debug/app-debug.apk`

The initial build needs internet to download dependencies and the embedded Python runtime. After installation, JavaScript and Python code execution works offline. The project does not currently include a Gradle wrapper JAR.

## Current limitations

- Offline execution currently supports JavaScript and Python only.
- C, C++, Java, C#, Go, Rust, PHP, Ruby, Kotlin and Bash are listed for the planned multi-language interface but their compilers/runtimes are not included in this build.
- No file/project browser, persistent project files, export, syntax highlighting, autocomplete, package manager, stdin input panel, debugger, or optional runtime packs yet.
- Arbitrary user code is executed locally. Only run code you understand and trust.

## Roadmap

1. Verify offline JavaScript and Python execution across supported Android devices.
2. Add a compatible C/C++ toolchain and measure its APK/storage footprint.
3. Evaluate offline Java and C# runtime/compiler options for Android.
4. Add optional runtime packs for larger languages where legally redistributable.
5. Add file management, input support, syntax highlighting, and project export.

## License

Add a project license before redistributing this app. Third-party runtimes and toolchains must retain their own license notices.
