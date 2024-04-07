object Android {

    object Classpaths {
        object Versions {
            const val kotlin = "1.6.10"
            const val gradle = "7.2.1"
            const val navigation = "2.4.2"
            const val protobuf = "0.8.19"
        }

        const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val navigation = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
        const val protobuf = "com.google.protobuf:protobuf-gradle-plugin:${Versions.protobuf}"
    }

    const val compileSdk = 32

    object DefaultConfig {
        const val applicationId = "hardcoder.dev.trustme"

        const val minSdk = 23
        const val targetSdk = 32

        const val instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        const val versionCode = 1
        const val versionName = "1.0"
    }

    object KotlinOptions {
        const val jvmTargetVersion = "11"
    }

    object Proguard {
        const val androidOptimizedRules = "proguard-android-optimize.txt"
        const val projectRules = "proguard-rules.pro"
    }
}