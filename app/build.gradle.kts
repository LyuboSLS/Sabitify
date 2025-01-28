plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "hr.algebra.sabitify"
    compileSdk = 35

    defaultConfig {
        applicationId = "hr.algebra.sabitify"
        minSdk = 26
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // preferences
    implementation(libs.androidx.preference.ktx)
// worker
    implementation(libs.androidx.work.runtime.ktx)

    // navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
// retrofit
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.converter.gson)

    // picasso
    implementation(libs.picasso)
    implementation(libs.picasso.transformations)
    implementation(libs.firebase.auth)

    // Import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom))

    // Declare the dependencies for the desired Firebase products without specifying versions
    // For example, declare the dependencies for Firebase Authentication and Cloud Firestore
    implementation(libs.google.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}