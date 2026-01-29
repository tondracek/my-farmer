plugins {
    alias(libs.plugins.android.application)

    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)

    /* has to be last */
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.tondracek.myfarmer"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.tondracek.myfarmer"
        minSdk = 27
        targetSdk = 36
        versionCode = 11
        versionName = "1.4.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // --- Project modules ---
    implementation(project(":feature:core"))
    implementation(project(":feature:common"))
    implementation(project(":feature:shop"))
    implementation(project(":feature:shopcategory"))
    implementation(project(":feature:shopfilters"))
    implementation(project(":feature:location"))
    implementation(project(":feature:user"))
    implementation(project(":feature:review"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:image"))

    // --- Core Android ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat.resources)

    // --- Hilt ---
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // --- Compose ---
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.constraintlayout.compose)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // --- Paging ---
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // --- Location & Maps ---
    implementation(libs.play.services.location)
    implementation(libs.maps.compose)
    implementation(libs.mapbox.android.ndk27)

    // --- Navigation ---
    implementation(libs.androidx.navigation.compose)

    // --- Networking / permissions ---
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.accompanist.permissions)

    // --- Firebase ---
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    // --- Auth/Credentials ---
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // --- Logging ---
    implementation(libs.timber)
}
