plugins {
    id(Plugins.Android.application)
    kotlin(Plugins.Kotlin.android)
    kotlin(Plugins.Kotlin.kapt)
    id(Plugins.serialization)
    id(Plugins.navigationSafeArgs)
    id(Plugins.protobuf)
}

android {
    compileSdk = Android.compileSdk

    defaultConfig {
        applicationId = Android.DefaultConfig.applicationId
        minSdk = Android.DefaultConfig.minSdk
        targetSdk = Android.DefaultConfig.targetSdk
        versionCode = Android.DefaultConfig.versionCode
        versionName = Android.DefaultConfig.versionName
        testInstrumentationRunner = Android.DefaultConfig.instrumentationRunner
    }

    buildTypes {
        all {
            proguardFiles(
                getDefaultProguardFile(Android.Proguard.androidOptimizedRules),
                Android.Proguard.projectRules
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = Android.KotlinOptions.jvmTargetVersion
    }

    kapt {
        correctErrorTypes = true
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    addFeatureModules()
    addNavigation()
    addGoogleServices()
    addCommonAndroid()
    addLifecycle()
    addDI()
    addData()
    addUI()
    addCoil()
    addCoroutines()
    addRoom()
    addMath()
    addTests()
}
