plugins {
    alias(libs.plugins.android.library)

    alias(libs.plugins.ksp)
}

android {
    namespace = "com.tondracek.myfarmer.shopcategory"
    compileSdk = 36
}

dependencies {
    /** Project modules **/
    implementation(project(":feature:core"))
    implementation(project(":feature:common"))

    /** Hilt **/
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    /** Firebase **/
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)

    /** Kotlin **/
    implementation(libs.kotlinx.serialization.json)
}