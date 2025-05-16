plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    id("kotlin-parcelize")
}

android {
    namespace = "com.zero_one.martha"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.zero_one.martha"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_URL", "\"http://192.168.173.94:8080/api/\"")
            buildConfigField("String", "STORAGE_URL", "\"http://192.168.173.94:8081/\"")
        }
        release {
            buildConfigField("String", "API_URL", "\"http://192.168.173.94:8080/api/\"")
            buildConfigField("String", "STORAGE_URL", "\"http://192.168.173.94:8081/\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)

    // coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // exoplayer
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)

    // datastore
    implementation(libs.androidx.datastore)

    // icons
    implementation(libs.androidx.material.icons.extended)

    // hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)

    // network
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.retrofit.gson.converter)

    // test and debug
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
