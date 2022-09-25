plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 26
        targetSdk = 32

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        named("release") {
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
}

dependencies {
    implementation(kotlin("stdlib-jdk7"))
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.browser:browser:1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")

    api("com.jakewharton.timber:timber:5.0.1")
    api("com.jraska:console:1.2.0")
    api("com.jraska:console-timber-tree:1.2.0")

    api("io.insert-koin:koin-android:3.1.5")

    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.google.android.material:material:1.6.0")
}

//repositories {
//    mavenCentral()
//    google()
//}