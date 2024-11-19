// build.gradle.kts (Nivel de Proyecto)

plugins {
    id("com.android.application") version "8.0.0" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

buildscript {
    repositories {
        google()  // Repositorio de Google
        mavenCentral()  // Repositorio de Maven Central
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.0")  // El plugin de Android para Gradle
        classpath("com.google.gms:google-services:4.4.2")  // El plugin de Google Services para Firebase
    }
}

allprojects {
    repositories {
        google()  // Repositorio de Google
        mavenCentral()  // Repositorio de Maven Central
    }
}
