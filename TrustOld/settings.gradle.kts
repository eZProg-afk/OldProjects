dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://www.jitpack.io")
    }
}

rootProject.name = "TrustMe"
include(":app")
include(":utils:common-resources", ":utils:common-base")
include(
    ":features:onboarding",
    ":features:stub",
    ":features:calculator-stub",
    ":features:dashboard",
    ":features:storage",
    ":features:settings"
)
include(":data")
