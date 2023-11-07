plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "seamonster.kraken.androidep7"
    compileSdk = 34

    defaultConfig {
        applicationId = "seamonster.kraken.androidep7"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        versionNameSuffix = "-AndroidEp7_$versionName-$versionCode"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true

//        javaCompileOptions {
//            annotationProcessorOptions {
//                arguments += ("room.schemaLocation" to "$projectDir/schemas")
//            }
//        }
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

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
//        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    val daggerVersion = "2.48.1"
    val retrofitVersion = "2.9.0"
    val lifecycleVersion = "2.6.2"

    // Retrofit2 & okhttp3
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.11.0")) // define a BOM for okhttp
    implementation("com.squareup.okhttp3:logging-interceptor")
    implementation("com.squareup.okhttp3:okhttp-urlconnection")

    // Dependency Injection
    implementation("com.google.dagger:dagger:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
    implementation("com.google.dagger:dagger-android:$daggerVersion")
    implementation("com.google.dagger:dagger-android-support:$daggerVersion")
    kapt("com.google.dagger:dagger-android-processor:$daggerVersion")

    // Android lifecycle
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")

    // store local user data
//    implementation("androidx.preference:preference-ktx:1.2.1") // use DataStore Preferences instead
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-preferences-rxjava2:1.0.0") // RxJava2 support

    implementation("androidx.paging:paging-runtime-ktx:3.2.1")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // MvRx
    implementation("com.airbnb.android:mavericks:3.0.7")
    implementation("com.airbnb.android:mavericks-rxjava2:3.0.7")

    // kotlin coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    // use Glide for retrieving image from a remote source/URL
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    // dot indicator
    implementation("com.tbuonomo:dotsindicator:4.2")

    // RxJava2
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    implementation("com.jakewharton.rxrelay2:rxrelay:2.1.1")

    // Work Manager
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.work:work-rxjava2:2.8.1") // RxJava2 support

    // default dependencies
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}