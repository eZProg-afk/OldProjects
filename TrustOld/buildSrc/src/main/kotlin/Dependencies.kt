import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {

    private object Versions {
        const val room = "2.4.2"
        const val coroutines = "1.5.0"
        const val lifecycleKtx = "2.2.0"
        const val fragmentKtx = "1.5.0"
        const val coreKtx = "1.8.0"
        const val appCompat = "1.4.2"
        const val material = "1.6.1"
        const val splashScreen = "1.0.0-beta02"
        const val mathSolver = "0.4.8"
        const val viewBindingPropertyDelegate = "1.4.2"
        const val coil = "2.1.0"
        const val roundedImageView = "2.3.0"
        const val lottie = "5.2.0"
        const val constraintlayout = "2.1.4"
        const val adapterDelegates = "4.3.1"
        const val preferences = "1.2.0"
        const val dataStore = "1.0.0"
        const val protobuf = "4.0.0-rc-2"
        const val dagger = "2.43.2"
        const val playCore = "1.10.3"
        const val playFeatures = "2.0.0"
        const val navigation = "2.5.1"
    }

    const val room = "androidx.room:room-runtime:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"

    const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    const val lifecycleViewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleKtx}"
    const val lifecycleRuntimeKtx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleKtx}"
    const val lifecycleExtensions =
        "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleKtx}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"

    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val splashScreen = "androidx.core:core-splashscreen:${Versions.splashScreen}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    const val viewBindingDelegate =
        "com.kirich1409.viewbindingpropertydelegate:viewbindingpropertydelegate:${Versions.viewBindingPropertyDelegate}"
    const val roundedImageView = "com.makeramen:roundedimageview:${Versions.roundedImageView}"
    const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"

    const val coilCore = "io.coil-kt:coil:${Versions.coil}"
    const val coilBase = "io.coil-kt:coil-base:${Versions.coil}"

    const val mathSolver = "net.objecthunter:exp4j:${Versions.mathSolver}"

    const val adapterDelegatesCore = "com.hannesdorfmann:adapterdelegates4-kotlin-dsl:${Versions.adapterDelegates}"
    const val adapterDelegatesViewBinding = "com.hannesdorfmann:adapterdelegates4-kotlin-dsl-viewbinding:${Versions.adapterDelegates}"

    const val preferences = "androidx.preference:preference-ktx:${Versions.preferences}"
    const val protobuf = "com.google.protobuf:protobuf-javalite:${Versions.protobuf}"
    const val dataStore = "androidx.datastore:datastore:${Versions.dataStore}"

    const val daggerCore = "com.google.dagger:dagger:${Versions.dagger}"
    const val daggerAndroid = "com.google.dagger:dagger-android:${Versions.dagger}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"

    const val playCore = "com.google.android.play:core:${Versions.playCore}"

    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val navigationDynamicFeature = "androidx.navigation:navigation-dynamic-features-fragment:${Versions.navigation}"
}

fun DependencyHandler.addCommonAndroid() {
    implementation(Dependencies.appCompat)
    implementation(Dependencies.material)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.fragmentKtx)
}

fun DependencyHandler.addUI() {
    implementation(Dependencies.constraintlayout)
    implementation(Dependencies.viewBindingDelegate)
    implementation(Dependencies.splashScreen)
    implementation(Dependencies.roundedImageView)
    implementation(Dependencies.adapterDelegatesCore)
    implementation(Dependencies.adapterDelegatesViewBinding)
    implementation(Dependencies.lottie)
}

fun DependencyHandler.addDI() {
    implementation(Dependencies.daggerCore)
    implementation(Dependencies.daggerAndroid)
    kapt(Dependencies.daggerCompiler)
}

fun DependencyHandler.addLifecycle() {
    implementation(Dependencies.lifecycleViewModelKtx)
    implementation(Dependencies.lifecycleRuntimeKtx)
    implementation(Dependencies.lifecycleExtensions)
}

fun DependencyHandler.addCoroutines() {
    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coroutinesAndroid)
}

fun DependencyHandler.addRoom() {
    implementation(Dependencies.room)
    implementation(Dependencies.roomKtx)
    kapt(Dependencies.roomCompiler)
}

fun DependencyHandler.addCoil() {
    implementation(Dependencies.coilCore)
    implementation(Dependencies.coilBase)
}

fun DependencyHandler.addMath() {
    implementation(Dependencies.mathSolver)
}

fun DependencyHandler.addGoogleServices() {
    implementation(Dependencies.playCore)
}

fun DependencyHandler.addNavigation() {
    implementation(Dependencies.navigationFragment)
    implementation(Dependencies.navigationUi)
    implementation(Dependencies.navigationDynamicFeature)
}

fun DependencyHandler.addData() {
    implementation(Dependencies.preferences)
    implementation(Dependencies.dataStore)
    implementation(Dependencies.protobuf)
}