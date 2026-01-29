plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.tondracek.myfarmer.common"
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
    /** Testing **/
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
}