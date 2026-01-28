plugins {
    alias(libs.plugins.android.library)

    alias(libs.plugins.ksp)
}

android {
    namespace = "com.tondracek.myfarmer.shopfilters"
    compileSdk = 36
}

dependencies {
    /** Project modules **/
    implementation(project(":feature:core"))
    implementation(project(":feature:common"))
    implementation(project(":feature:shop"))
    implementation(project(":feature:shopcategory"))
    implementation(project(":feature:location"))
    implementation(project(":feature:review"))

    /** Hilt **/
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
