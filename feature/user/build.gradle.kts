plugins {
    alias(libs.plugins.android.library)

    alias(libs.plugins.ksp)
}

android {
    namespace = "com.tondracek.myfarmer.user"
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
    /** Project modules **/
    implementation(project(":feature:core"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:image"))

    /** Hilt **/
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    /** Firebase **/
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)

    /** Kotlin **/
    implementation(libs.kotlinx.serialization.json)
}