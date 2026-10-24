# OmniCode Mobile 0.4.0

OmniCode Mobile is an Android coding workspace with offline execution for the runtimes actually bundled in the APK.

## Runtime status

| Language | Status | Runtime |
|---|---|---|
| JavaScript | Available offline | Android System WebView JavaScript engine |
| Python | Available offline | Embedded Python 3.12 via Chaquopy |
| C | Not available yet | Native Android compiler not bundled |
| C++ | Not available yet | Native Android compiler not bundled |
| Java, C#, Go, Rust, PHP, Ruby, Kotlin, Bash | Not available | Toolchains not bundled |

**Important:** C/C++ are not falsely advertised as working. A proper offline compiler requires Android-compatible arm64 binaries, headers, libraries, process integration, packaging, and license notices. This source version does not include that toolchain.

## v0.4.0 changes

- Reworked the interface with a new hero panel, redesigned language controls, editor header, terminal output, action buttons, runtime status cards, and support pills.
- Fixed JavaScript runner newline escaping that caused generated code to contain a literal `\n` before the user's source and trigger syntax errors.
- JavaScript logs are captured locally; `console.log`, `console.info`, `console.warn`, and `console.error` are supported.
- Kept Python execution on Chaquopy and clear unsupported-language messages.
- Version metadata updated to 0.4.0 / versionCode 3.

## Test JavaScript

```javascript
console.log("Hello from JavaScript");
console.log(2 + 3);
```

Expected output:

```text
Hello from JavaScript
5
```

## Known limitations

- The editor is a simple text area, not a syntax-highlighting IDE.
- JavaScript runs in the WebView and should be used for trusted code only; this is not a security sandbox.
- Interactive stdin, file system access, project folders, and file export are not implemented.
- C/C++ and other missing toolchains cannot execute offline in this version.
