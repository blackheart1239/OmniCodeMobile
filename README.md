# OmniCode Mobile 0.4.1

OmniCode Mobile is a small Android coding workspace with local Python/JavaScript execution and C/C++ compilation through a remote compiler.

## Language support

| Language | Availability | Notes |
|---|---|---|
| JavaScript | Offline | Runs in Android WebView |
| Python 3.12 | Offline | Embedded through Chaquopy |
| C | Online | Compiles/runs through the public Wandbox API; internet required |
| C++ | Online | Compiles/runs through the public Wandbox API; internet required |
| Java, C#, Go, Rust, PHP, Ruby, Kotlin, Bash | Not included | No compiler/runtime bundled |

**Privacy and offline note:** C/C++ source is sent to Wandbox for compilation and execution. Do not submit secrets, credentials, or private code. C/C++ does not work offline in this version. This release does not bundle a native compiler.

## v0.4.1 changes

- C and C++ compile/run integration using Wandbox's HTTPS API (GCC C and C++ compiler identifiers).
- Android system file picker for importing source files.
- Android system save dialog for exporting the current editor contents.
- Imported `.c`, `.h`, `.cc`, `.cpp`, `.cxx`, `.hpp`, `.js`, `.mjs`, and `.py` files select the corresponding editor language.
- UI labels distinguish local execution from online compilation.

## Import / export

- Tap **Import file** and choose a source file from Android's file picker.
- Tap **Export file** and choose where to save the current code. The suggested filename follows the selected language.
- Import/export uses Android's Storage Access Framework; no broad storage permission is needed.

## Build using GitHub Actions

The project uses Android Gradle Plugin 8.9.1, Kotlin 2.0.21, Chaquopy 16.1.0, Java 17, compile SDK 35, and Python 3.12. Keep your repository's existing workflow that extracts `OmniCodeMobile-source.zip` and runs `assembleDebug`. Upload this ZIP using the exact filename expected by your workflow, then run the workflow and download the APK artifact only if the build succeeds.

## Quick C example

```c
#include <stdio.h>
int main(void) {
    printf("Hello from C!\n");
    return 0;
}
```

## Quick C++ example

```cpp
#include <iostream>
int main() {
    std::cout << "Hello from C++!" << std::endl;
    return 0;
}
```

## Limitations

- C/C++ requires internet and the public Wandbox service to be available. Compiler availability and limits can change.
- This is not a full syntax-highlighting IDE and does not support interactive stdin, multi-file projects, or native offline C/C++ compilation.
- JavaScript executes in WebView and is not a security sandbox; run only code you trust.
