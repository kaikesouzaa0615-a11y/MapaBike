plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.mapabike"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mapabike"
        minSdk = 26
        targetSdk = 35
        versionCode = 3
        versionName = "0.3"
    }
}
