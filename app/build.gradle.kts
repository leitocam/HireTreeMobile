import com.google.protobuf.gradle.id
import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.google.protobuf)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.calyrsoft.ucbp1"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.calyrsoft.ucbp1"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // BuildConfig fields
        buildConfigField("String", "GEMINI_API_KEY", "\"${getLocalProperty("GEMINI_API_KEY")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

fun getLocalProperty(key: String): String {
    val localProperties = Properties()
    val localPropertiesFile = project.rootProject.file("local.properties")

    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { input ->
            localProperties.load(input)
        }
    } else {
        // Si no existe local.properties, devolvemos una cadena vacía o un valor por defecto para evitar el crash
        // aunque lo ideal es que el archivo exista.
        println("Advertencia: El archivo 'local.properties' no se encuentra.")
        return ""
    }
    // Si la clave no existe, devolvemos una cadena vacía en lugar de lanzar error,
    // para que el build no falle si falta alguna clave opcional.
    return localProperties.getProperty(key) ?: ""
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.firebase.database)
    implementation(libs.firebase.messaging)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation (libs.koin.android)
    implementation (libs.koin.androidx.navigation)
    implementation (libs.koin.androidx.compose)
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(libs.androidx.navigation.compose)
    //serialization
    implementation(libs.kotlinx.serialization.json)


    //proto
    implementation(libs.grpc.protobuf.lite)
    implementation(libs.protobuf.kotlin.lite)
    implementation(libs.grpc.kotlin.stub)
    runtimeOnly(libs.grpc.okhttp)

    //local bundle room
    implementation(libs.bundles.local)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
    testImplementation(libs.room.testing)

    implementation(libs.datastore)

    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    implementation(libs.androidx.work.runtime.ktx)

}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.get()}"
    }
    plugins {
        id("java") {
            artifact = libs.protoc.gen.grpc.java.get().toString()
        }
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpc.get()}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${libs.versions.grpcKotlin.get()}:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("java") { option("lite") }
                create("grpc") { option("lite") }
                create("grpckt") { option("lite") }
            }
            it.builtins {
                create("kotlin") { option("lite") }
            }
        }
    }
}
