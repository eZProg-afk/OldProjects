import org.gradle.api.artifacts.dsl.DependencyHandler

object TestDependencies {

    private object Versions {
        const val junit = "4.13.2"
        const val turbine = "0.8.0"
        const val assertjCore = "3.18.1"
        const val mockitoCore = "3.6.28"
        const val mockitoKotlin = "2.1.0"
        const val robolectric = "4.8.1"
        const val hamcrest = "1.3"
        const val testRunner = "1.3.0-rc03"
        const val testRules = "1.3.0-rc03"
        const val coroutinesTest = "1.4.2"
        const val room = "2.4.2"
        const val junitExt = "1.1.3"
        const val truth = "1.0.1"
        const val navigation = "2.4.2"
        const val arch = "2.1.0"
        const val kakao = "3.0.6"
        const val fragments = "1.4.1"
        const val leakCanary = "2.9.1"
    }

    const val junit = "junit:junit:${Versions.junit}"
    const val junitExt = "androidx.test.ext:junit:${Versions.junitExt}"
    const val turbine = "app.cash.turbine:turbine:${Versions.turbine}"
    const val hamcrest = "org.hamcrest:hamcrest-all:${Versions.hamcrest}"
    const val assertjCore = "org.assertj:assertj-core:${Versions.assertjCore}"
    const val mockitoCore = "org.mockito:mockito-core:${Versions.mockitoCore}"
    const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val testRunner = "androidx.test:runner:${Versions.testRunner}"
    const val testRules = "androidx.test:rules:${Versions.testRules}"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTest}"
    const val roomTest = "androidx.room:room-testing:${Versions.room}"
    const val truth = "com.google.truth:truth:${Versions.truth}"
    const val navigation = "androidx.navigation:navigation-testing:${Versions.navigation}"
    const val arch = "androidx.arch.core:core-testing:${Versions.arch}"
    const val kakao = "io.github.kakaocup:kakao:${Versions.kakao}"
    const val fragments = "androidx.fragment:fragment-testing:${Versions.fragments}"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
}

fun DependencyHandler.addTests() {
    addTestRunner()
    addUnitTests()
    addIntegrationTests()
    addUITests()
}

fun DependencyHandler.addTestRunner() {
    androidTestImplementation(TestDependencies.testRunner)
    androidTestImplementation(TestDependencies.testRules)
}

fun DependencyHandler.addUnitTests() {
    testImplementation(TestDependencies.junit)
    testImplementation(TestDependencies.hamcrest)
    testImplementation(TestDependencies.truth)
    testImplementation(TestDependencies.arch)
    testImplementation(TestDependencies.assertjCore)
    testImplementation(TestDependencies.mockitoCore)
    testImplementation(TestDependencies.mockitoKotlin)
    testImplementation(TestDependencies.turbine)
    testImplementation(TestDependencies.robolectric)
}

fun DependencyHandler.addIntegrationTests() {
    androidTestImplementation(TestDependencies.coroutinesTest)
    androidTestImplementation(TestDependencies.roomTest)
    androidTestImplementation(TestDependencies.junitExt)
    androidTestImplementation(TestDependencies.arch)
    androidTestImplementation(TestDependencies.truth)
    androidTestImplementation(TestDependencies.turbine)
    androidTestImplementation(TestDependencies.navigation)
}

fun DependencyHandler.addUITests() {
    androidTestImplementation(TestDependencies.kakao)
    debugImplementation(TestDependencies.fragments)
    debugImplementation(TestDependencies.leakCanary)
}