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
    implementation(project(":feature:user"))
    implementation(project(":feature:review"))
    implementation(project(":feature:image"))

    /** Testing **/
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)

    /** Hilt **/
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
