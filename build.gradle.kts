plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.omnicodemobile.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.omnicodemobile.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }
}
