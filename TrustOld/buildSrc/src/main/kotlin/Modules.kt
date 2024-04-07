import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

object Modules {

    const val app = ":app"

    object Features {
        const val onboarding = ":features:onboarding"
        const val stub = ":features:stub"
        const val dashboard = ":features:dashboard"
        const val storage = ":features:storage"
        const val settings = ":features:settings"
    }

    object Utils {
        const val base = ":utils:common-base"
        const val resources = ":utils:common-resources"
    }
}

fun DependencyHandler.addFeatureModules() {
    implementation(project(Modules.Features.onboarding))
    implementation(project(Modules.Features.stub))
    implementation(project(Modules.Features.dashboard))
    implementation(project(Modules.Features.storage))
    implementation(project(Modules.Features.settings))
}

fun DependencyHandler.addCommonModules() {
    implementation(project(Modules.Utils.base))
    implementation(project(Modules.Utils.resources))
}