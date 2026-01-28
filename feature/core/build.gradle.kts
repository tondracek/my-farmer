plugins {
    alias(libs.plugins.android.library)

    alias(libs.plugins.ksp)
}

android {
    namespace = "com.tondracek.myfarmer.core"
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
    implementation(libs.kotlinx.coroutines.core)

    /** Hilt **/
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    /** Logging **/
    implementation(libs.timber)

    /** Firebase **/
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
}