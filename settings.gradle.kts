pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
        }
    }
}

rootProject.name = "My Farmer"
include(":app")
include(":feature:shop")
include(":feature:core")
include(":feature:location")
include(":feature:auth")
include(":feature:image")
include(":feature:user")
include(":feature:shopcategory")
include(":feature:common")
include(":feature:review")
include(":feature:shopfilters")
