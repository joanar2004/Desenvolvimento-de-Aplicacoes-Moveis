plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "dam.A50829.coolweatherapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "dam.A50829.coolweatherapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Removemos o buildFeatures/dataBinding daqui para evitar erros de geração de código

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Mantemos a versão 17 por segurança, já que foi a que configuraste no Android Studio
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Biblioteca para a API (Mantemos porque precisas delas para o tempo)
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.android.gms:play-services-location:21.2.0")

    // Removemos as bibliotecas de Lifecycle e ViewModel para limpar o projeto

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}