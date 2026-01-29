plugins {
    alias(libs.plugins.android.library)

    alias(libs.plugins.ksp)
}

android {
    namespace = "com.tondracek.myfarmer.image"
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

    /** Hilt **/
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    /** Firebase **/
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)

    /** Logging **/
    implementation(libs.timber)

    /** Media **/
    implementation(libs.androidx.exifinterface)
}