# OmniCode Mobile — Android starter project

OmniCode Mobile is a buildable Android Studio starter for a hybrid mobile coding IDE.

## Implemented in v0.1.0
- Native Android shell with a local HTML/JavaScript editor UI.
- JavaScript execution inside the app's WebView (offline; basic `console.log/info/warn/error` capture).
- Online execution bridge for Python, C, C++, Java, C#, Go, Rust, PHP, Ruby, Kotlin and Bash using the public Piston API.
- Language-specific starter examples and output/error display.
- No account or API key is configured by default.

## Not implemented yet
- Offline Python, C/C++, Java or other compiler/interpreter bundles.
- Project/file browser, persistent source files, export, syntax highlighting, autocomplete, package manager, standard-input UI, debugger, or offline language packs.
- A guaranteed always-available remote execution backend. Piston is a third-party service and may impose limits or change availability.

**Privacy note:** non-JavaScript source code is sent to the Piston endpoint over HTTPS. Do not send secrets, credentials, proprietary code, or personal data.

## Build the APK

### Requirements
- Android Studio (current stable version recommended)
- Android SDK Platform 35 and Build Tools installed from SDK Manager
- JDK 17 (Android Studio's bundled JDK is suitable)
- Internet for Gradle dependency downloads and online execution

### Steps
1. Extract/open this `OmniCodeMobile` folder in Android Studio.
2. Let Gradle sync. If prompted, install Android SDK Platform 35.
3. Select **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
4. Android Studio should create the debug APK at `app/build/outputs/apk/debug/app-debug.apk`.
5. Transfer that APK to your Android phone and install it. If Android asks, allow installs from the app you used to open the APK.

Command-line alternative from this folder, with Android SDK configured and Gradle installed:

```bash
gradle assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`.

The project intentionally does not include a Gradle wrapper JAR. Opening it in Android Studio is the recommended path; Android Studio can use its bundled Gradle/JDK and sync the project.

## Roadmap for bundled offline runtimes

Add runtimes in separate milestones rather than pretending all languages already work offline:
1. Bundle a Python runtime suitable for Android (for example, a CPython distribution built for Android/Termux-compatible environment) and connect it through JNI/process integration.
2. Add a Clang/LLVM toolchain for C/C++ and verify NDK ABI compatibility, storage footprint and licenses.
3. Add Java support with a compatible Java runtime/compiler distribution; test Android restrictions and resource requirements.
4. Provide optional downloadable language packs for large toolchains, while keeping the base APK small enough to install.
5. Add an offline package registry/cache and explicit per-language runtime/version status.

Bundling toolchains requires checking each project's license, Android ABI support, security updates, and redistribution terms. An APK cannot realistically contain every language/runtime/toolchain ever made.
