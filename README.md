# OmniCode Mobile v0.5.0-alpha.2

OmniCode Mobile is an early-development Android coding workspace. This is an **alpha source build**, not a stable release.

## Runtime status in this build

| Language | Status | Where code runs |
|---|---|---|
| JavaScript | Available offline | Android WebView |
| Python 3.12 | Available offline | Embedded Chaquopy runtime |
| C | Experimental local compiler path | Requires separate ARM64 toolchain bundle asset |
| C++ | Experimental local compiler path | Requires separate ARM64 toolchain bundle asset |
| Other languages | Not implemented | No runtime bundled |

**Important:** This source adds an experimental local compiler integration and a first-run installer, but the compiler bundle is built separately by CI and must be attached to a public GitHub Release as `omnicode-clang-arm64.zip`. The toolchain extraction, compiler linking, and runtime behavior have not been device-tested in this environment. Do not call offline compilation production-ready until C and C++ are compiled and executed on a real ARM64 Android device in airplane mode.

## v0.5.0-alpha.2 changes

- Version metadata advanced to `0.5.0-alpha.1` (`versionCode` 5).
- Added an in-app GitHub Releases update checker. It looks for a newer release with an APK asset, asks before downloading, and hands the APK to Android's package installer. Android still requires user confirmation.
- Added Android FileProvider support for safely passing the downloaded APK to the installer.
- Added a **Check updates** control in the app UI.
- Added optional release-signing configuration for GitHub Actions.
- Added an experimental local C/C++ compiler bridge and first-run toolchain installer.
- Added a CI script to build an ARM64 compiler bundle separately.
- Preserved import/export and local Python/JavaScript execution.

## Update behavior and signing — read before distributing

Android can update an installed app only when the application ID is unchanged, the new `versionCode` is higher, and the APK is signed with the same signing certificate.

- Application ID: `com.omnicodemobile.app` (do not change it for updates).
- Increment `versionCode` for every new APK.
- Use one persistent signing key for all releases. Never generate a new release key for each build, and keep a secure backup.
- Create a persistent keystore once and keep a secure backup. For example, on a computer with Java installed, run `keytool -genkeypair -v -keystore omnicode-release.jks -alias omnicode -keyalg RSA -keysize 2048 -validity 10000`. Do not lose this file or its passwords.
- Base64-encode the keystore file and add these repository secrets in GitHub Settings → Secrets and variables → Actions:
  - `OMNICODE_KEYSTORE_BASE64`: base64-encoded `.jks` keystore
  - `OMNICODE_KEYSTORE_PASSWORD`
  - `OMNICODE_KEY_ALIAS`
  - `OMNICODE_KEY_PASSWORD`
- On Linux, base64-encode with `base64 -w 0 omnicode-release.jks`; on Windows PowerShell, use `[Convert]::ToBase64String([IO.File]::ReadAllBytes('omnicode-release.jks'))` and copy the result into the secret.
- If these secrets are not configured, the workflow builds a debug APK. Debug APKs made on different clean GitHub runners may have different debug signing keys and may not update over each other.
- The signing key used for the already-published v0.4.1 APK may be different or unavailable. If it differs, Android cannot update that existing install; a one-time uninstall may still be required. No app code can bypass Android's signature check.
- The updater checks public GitHub Releases for this repository and expects an APK file attached to a release. GitHub Actions workflow artifacts alone are not release assets.
- The direct-APK updater is intended for GitHub-distributed builds. For a future Play Store release, use Google Play's update flow instead of downloading APKs from GitHub.

## GitHub Actions build

The workflow in `.github/workflows/android.yml` expects `OmniCodeMobile-source.zip` in the repository root. Upload the ZIP under that exact filename and run the workflow. It builds the APK and a separate `omnicode-clang-arm64.zip` compiler artifact. Download the compiler artifact from Actions and attach it to a public GitHub Release with that exact filename. The app installer looks for this asset. The bundle and runtime integration are experimental and require on-device testing.

## Build environment

- Android Gradle Plugin 8.9.1
- Gradle 8.11.1
- Kotlin 2.0.21
- Java 17
- Android compile SDK 35
- Chaquopy 16.1.0 with Python 3.12
- Android ABIs configured for `arm64-v8a` and `x86_64`

## Import and export

- **Import file** opens Android's document picker.
- **Export file** opens Android's save dialog.
- Common `.c`, `.cpp`, `.cc`, `.cxx`, `.h`, `.hpp`, `.js`, `.mjs`, and `.py` extensions select their matching language.

## Known limitations

- C/C++ local compilation is experimental. The separate compiler bundle must be attached to a GitHub Release and installed in the app; it is not embedded in the source ZIP or APK.
- The bundle builder dereferences symlinks for ZIP extraction. Do not treat this integration as confirmed working until tested on ARM64 Android.
- No interactive stdin panel yet; planned for v0.6.0.
- The update flow requires a public GitHub release with an APK asset and the same signing certificate as the installed app.
- The first update from an old APK signed with a different debug key may require uninstalling that old build.
- The JavaScript runner uses WebView's JavaScript engine; it is not a security sandbox. Run only code you trust.
- The app has not been fully built or device-tested as part of this source preparation. Treat this as an alpha source package until GitHub Actions succeeds and manual tests pass.

## Release stages

- `v0.1.0`–`v0.5.0`: alpha
- `v0.5.1`–`v0.9.9`: beta
- `v1.0.0`: intended first stable release
