@file:Suppress("UnstableApiUsage")
plugins { alias(libs.plugins.android.application) }

val major = libs.versions.major.get().toInt()
val minor = libs.versions.minor.get().toInt()
val patch = libs.versions.patch.get().toInt()
val gitOwner: String = providers.gradleProperty("github.owner").get()
val gitPackage: String = providers.gradleProperty("github.package").get()

android {
    namespace = "com.mikeeckels.obdash"

    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField(
            "String",
            "RELEASE_URL",
            "\"https://api.github.com/repos/${gitOwner}/${gitPackage}/releases/latest\""
        )

        applicationId = "com.mikeeckels.obdash"
        minSdk = 35
        targetSdk = 36
        versionCode = (major * 10000) + (minor * 100) + patch
        versionName = "$major.$minor.$patch"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true
            versionNameSuffix = "-debug"
            applicationIdSuffix = ".debug"
        }

        release {
            isDebuggable = false
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
}

androidComponents {
    onVariants { variant ->
        val buildType = variant.buildType ?: "release"

        variant.outputs.forEach { output ->
            output.outputFileName.set("$gitPackage-v$major.$minor.$patch-$buildType.apk")
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.okhttp)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.app.projected)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}