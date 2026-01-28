plugins {
    alias(libs.plugins.android.library)

    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.tondracek.myfarmer.location"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 27
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    /** Kotlin  **/
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.kotlinx.serialization.json)

    /** Hilt **/
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    /** Geolocation **/
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.geofire.android.common)
}