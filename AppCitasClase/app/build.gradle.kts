plugins {
    id("com.android.application")
    id("com.google.gms.google-services")  // Aplica el plugin de Google Services para Firebase
}

android {
    namespace = "com.example.appcitasclase"  // Cambia esto según tu espacio de nombres
    compileSdk = 34  // Asegúrate de que esta versión esté actualizada

    defaultConfig {
        applicationId = "com.example.appcitasclase"  // Asegúrate de que este ID coincida con el de Firebase
        minSdk = 24  // Define el mínimo SDK que tu aplicación soporta
        targetSdk = 34  // Define el SDK objetivo
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false  // Desactiva el minificado para pruebas
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true  // Activa el uso de ViewBinding
    }
}

dependencies {
    // Dependencias estándar de Android
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.activity:activity:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.6.0")
    implementation("androidx.navigation:navigation-ui:2.6.0")

    // Dependencias para pruebas
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.espresso:espresso-core:3.5.1")

    // Dependencias de Firebase (usando Firebase BoM para manejar versiones automáticamente)
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))  // Firebase Bill of Materials
    implementation("com.google.firebase:firebase-analytics")  // Firebase Analytics
    implementation("com.google.firebase:firebase-auth:22.0.0")  // Firebase Authentication

    // Otras dependencias comunes que puedas necesitar
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")  // ConstraintLayout para diseño
}
