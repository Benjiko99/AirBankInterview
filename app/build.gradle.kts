plugins {
    id("com.android.application")
    id("kotlin-allopen")
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}
apply(plugin = "org.jmailen.kotlinter")
apply(plugin = "app.cash.exhaustive")

android {
    compileSdkVersion(Application.compileSdk)

    defaultConfig {
        applicationId = Application.id
        minSdkVersion(Application.minSdk)
        targetSdkVersion(Application.targetSdk)
        versionCode = Application.versionCode
        versionName = Application.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = Application.sourceCompat
        targetCompatibility = Application.targetCompat
    }

    kotlinOptions {
        jvmTarget = Application.targetCompat.toString()
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

allOpen {
    // Makes annotated classes `open` in debug builds, used for mocking in unit tests.
    annotation("com.spiraclesoftware.core.testing.OpenClass")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":core"))

    implementation("co.zsmb:rainbow-cake-core:${Dependencies.rainbowCake}")
    implementation("co.zsmb:rainbow-cake-koin:${Dependencies.rainbowCake}")
    implementation("co.zsmb:rainbow-cake-timber:${Dependencies.rainbowCake}")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Dependencies.leakCanary}")
    implementation("com.facebook.stetho:stetho:${Dependencies.stetho}")

    implementation("com.jakewharton.threetenabp:threetenabp:${Dependencies.threetenabp}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Dependencies.coroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Dependencies.coroutines}")

    implementation("org.koin:koin-android:${Dependencies.koin}")
    implementation("org.koin:koin-androidx-viewmodel:${Dependencies.koin}")

    implementation("com.squareup.moshi:moshi:${Dependencies.moshi}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Dependencies.moshi}")

    implementation("com.squareup.okhttp3:okhttp:${Dependencies.okhttp}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Dependencies.okhttp}")

    implementation("com.squareup.retrofit2:retrofit:${Dependencies.retrofit}")
    implementation("com.squareup.retrofit2:converter-moshi:${Dependencies.retrofit}")

    implementation("androidx.navigation:navigation-fragment-ktx:${Dependencies.navigation}")
    implementation("androidx.navigation:navigation-ui-ktx:${Dependencies.navigation}")

    kapt("androidx.room:room-compiler:${Dependencies.room}")
    implementation("androidx.room:room-runtime:${Dependencies.room}")
    implementation("androidx.room:room-ktx:${Dependencies.room}")
    testImplementation("androidx.room:room-testing:${Dependencies.room}")

    implementation("androidx.fragment:fragment-ktx:${Dependencies.androidxFragment}")
    implementation("androidx.recyclerview:recyclerview:${Dependencies.androidx}")
    implementation("androidx.constraintlayout:constraintlayout:${Dependencies.constraintLayout}")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${Dependencies.swipeRefreshLayout}")

    implementation("com.mikepenz:fastadapter:${Dependencies.fastAdapter}")
    implementation("com.mikepenz:fastadapter-extensions-binding:${Dependencies.fastAdapter}")
    implementation("com.mikepenz:fastadapter-extensions-diff:${Dependencies.fastAdapter}")
    implementation("io.cabriole:decorator:${Dependencies.decorator}")

    implementation("io.coil-kt:coil:${Dependencies.coil}")
    implementation("com.github.stfalcon:stfalcon-imageviewer:${Dependencies.imageViewer}")

    testImplementation("junit:junit:${Dependencies.junit}")
    testImplementation("androidx.arch.core:core-testing:${Dependencies.lifecycle}")
    testImplementation("androidx.fragment:fragment-testing:${Dependencies.androidxFragment}")
    testImplementation("co.zsmb:rainbow-cake-test:${Dependencies.rainbowCake}")
    testImplementation("org.koin:koin-test:${Dependencies.koin}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Dependencies.coroutines}")
    testImplementation("org.mockito:mockito-inline:${Dependencies.mockito}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Dependencies.mockitoKotlin}")
    testImplementation("org.threeten:threetenbp:${Dependencies.threetenabp}"){
        exclude("com.jakewharton.threetenabp", "threetenabp")
    }
    androidTestImplementation("androidx.test:runner:${Dependencies.androidxTestRunner}")
    androidTestImplementation("androidx.test.ext:junit:${Dependencies.androidxTestJUnit}")
    androidTestImplementation("org.mockito:mockito-android:${Dependencies.mockito}")
}
