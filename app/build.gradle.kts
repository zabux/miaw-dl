plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.gms.google-services")
}

android {
    namespace = "dl.miaw"
    compileSdk = 36

    defaultConfig {
        applicationId = "dl.miaw"
        minSdk = 26
        targetSdk = 36
        versionCode = 23
        versionName = "1.8.0"
    }
    buildFeatures {
        compose = true
    }
    
    signingConfigs {
        create("release") {
            storeFile = file("keystore.jks")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {
    implementation(project(":lib"))
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.serialization.json)
    
    // AdMob
    implementation("com.google.android.gms:play-services-ads:23.1.0")

    // Firebase Analytics
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation("com.google.firebase:firebase-analytics")

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.tooling.preview)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.icons.fontawesome)
    implementation(libs.activity.compose)
    implementation(libs.core.ktx)

    debugImplementation(libs.compose.tooling)
}
