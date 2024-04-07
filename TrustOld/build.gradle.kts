buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }

    dependencies {
        classpath(Android.Classpaths.gradle)
        classpath(Android.Classpaths.kotlin)
        classpath(Android.Classpaths.navigation)
        classpath(Android.Classpaths.protobuf)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}

tasks.register("clean", Delete::class) {
    rootProject.buildDir
}
