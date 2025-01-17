plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.myaccountbook"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myaccountbook"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.github.prolificinteractive:material-calendarview:1.4.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.github.jayrambhia:Tooltip:0.1.7-1")
    implementation( "com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.github.AnyChart:AnyChart-Android:1.1.5")
//    implementation("com.teaanddogdog:mpandroidchartutil:1.0.3");


}