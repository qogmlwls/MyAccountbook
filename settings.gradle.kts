pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // 달력 라이브러리 추가하느라, 코드추가.
//        maven{ url = uri("https://dl.bintray.com/teadoglibrary/MPAndroidChartFix") }
        maven { url = uri("https://www.jitpack.io" ) }


    }
}

rootProject.name = "MyAccountbook"
include(":app")
 