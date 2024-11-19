pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)  // Asegúrate de que se usen los repositorios definidos en `settings.gradle.kts`
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AppCitasClase"
include(":app")  // Asegúrate de incluir el módulo 'app'
