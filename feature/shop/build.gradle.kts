plugins {
    alias(libs.plugins.android.library)

    alias(libs.plugins.ksp)
}

android {
    namespace = "com.tondracek.myfarmer.shop"
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
    implementation(project(":feature:common"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:user"))
    implementation(project(":feature:image"))
    implementation(project(":feature:location"))
    implementation(project(":feature:shopcategory"))

    /** Firebase **/
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)

    /** Kotlin  **/
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)

    /** Hilt **/
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
