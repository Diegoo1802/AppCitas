// build.gradle (nivel de proyecto)
plugins {
    alias(libs.plugins.androidApplication) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false // Verifica que esta versión sea compatible con tu proyecto
}

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")  // Asegúrate de que esta línea esté presente
    }
}

allprojects {
    // Ya no es necesario agregar los repositorios aquí
}
