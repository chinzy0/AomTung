plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}


android {
    namespace = "com.money.moneyx"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.money.moneyx"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }
    dataBinding {
        enable = true
    }



}



dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // viewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    //Okhttp
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    //SplashScreen
    implementation("androidx.core:core-splashscreen:1.0.0")
    //PinView
    implementation ("io.github.chaosleung:pinview:1.4.4")
    //chart
    implementation ("com.diogobernardino:williamchart:3.10.1")
    implementation ("androidx.biometric:biometric-ktx:1.2.0-alpha05")
    implementation ("com.androidplot:androidplot-core:1.5.10")
    implementation ("ir.mahozad.android:pie-chart:0.7.0")
    //circle
    implementation ("de.hdodenhof:circleimageview:3.1.0")
}