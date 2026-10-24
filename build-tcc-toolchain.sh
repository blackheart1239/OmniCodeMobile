#!/usr/bin/env bash
set -euo pipefail

: "${ANDROID_NDK_HOME:?ANDROID_NDK_HOME must point to an installed Android NDK}"
HOST_TAG=linux-x86_64
TOOLCHAIN="$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/$HOST_TAG"
CC="$TOOLCHAIN/bin/aarch64-linux-android26-clang"
SYSROOT="$TOOLCHAIN/sysroot"
STAGE="$PWD/tcc-stage"
RUNTIME_PREFIX="/data/user/0/com.omnicodemobile.app/files/omni-toolchain"

rm -rf tcc-src tcc-build "$STAGE" toolchain-package
mkdir -p "$STAGE/bin" "$STAGE/include" "$STAGE/lib/tcc" toolchain-package

git clone --depth 1 https://github.com/TinyCC/tinycc.git tcc-src
cd tcc-src
# Build TCC itself as an Android ARM64 executable. Runtime search paths are
# intentionally app-private and stable for this package/application ID.
./configure --cpu=arm64 --targetos=Android --cc="$CC" \
  --prefix="$RUNTIME_PREFIX" --sysroot="$RUNTIME_PREFIX" \
  --extra-cflags="-O2 -fPIE" --extra-ldflags="-pie"
make -j2
make install DESTDIR="$PWD/../tcc-build"
cd ..

# Locate the staged install tree irrespective of DESTDIR path nesting.
TCC_BIN=$(find tcc-build -type f -path '*/bin/tcc' -print -quit)
if [[ -z "$TCC_BIN" ]]; then echo 'TCC executable not found after install' >&2; exit 1; fi
cp "$TCC_BIN" "$STAGE/bin/tcc"
chmod 755 "$STAGE/bin/tcc"
file "$STAGE/bin/tcc"
readelf -h "$STAGE/bin/tcc" | grep -q 'AArch64' || { echo 'TCC binary is not AArch64' >&2; exit 1; }
readelf -l "$STAGE/bin/tcc" | grep -q '/system/bin/linker64' || { echo 'TCC binary does not use Android linker64' >&2; exit 1; }

# TCC's own runtime helper library is needed for some generated C programs.
TCC_LIB=$(find tcc-build -type f -name 'libtcc1.a' -print -quit || true)
if [[ -n "$TCC_LIB" ]]; then cp "$TCC_LIB" "$STAGE/lib/tcc/libtcc1.a"; fi

# Bundle Android's C headers from the NDK sysroot; Android system libraries
# (libc, libm, libdl) remain provided by the Android OS itself.
cp -a "$SYSROOT/usr/include/." "$STAGE/include/"
# The TCC source tree also has small compiler-specific headers.
if [[ -d tcc-src/include ]]; then cp -an tcc-src/include/. "$STAGE/include/"; fi

# Strip irrelevant host metadata and create the exact layout expected by the app.
mkdir -p toolchain-package/bin toolchain-package/include toolchain-package/lib/tcc
cp "$STAGE/bin/tcc" toolchain-package/bin/tcc
cp -a "$STAGE/include/." toolchain-package/include/
if [[ -f "$STAGE/lib/tcc/libtcc1.a" ]]; then cp "$STAGE/lib/tcc/libtcc1.a" toolchain-package/lib/tcc/; fi
[[ -f toolchain-package/include/stdio.h ]] || { echo 'stdio.h missing from package' >&2; exit 1; }
zip -qr "$PWD/omnicode-tcc-arm64.zip" . -i 'toolchain-package/*'
# Rebuild archive with paths relative to the bundle root (bin/, include/, lib/).
rm -f omnicode-tcc-arm64.zip
(cd toolchain-package && zip -qr "$OLDPWD/omnicode-tcc-arm64.zip" .)

echo "Created $(pwd)/omnicode-tcc-arm64.zip"
ls -lh omnicode-tcc-arm64.zip
