plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.devtoolsKsp) // added for ksp plugin
    alias(libs.plugins.kotlinKapt) //added Kotlin kapt plugin
    alias(libs.plugins.daggerHilt) //added for HILT
}

android {
    namespace = "com.rounak.machinetest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rounak.machinetest"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        getByName("release") {
            isDebuggable = false //by default = false
            buildConfigField("String", "BASE_URL", "\"https://navkiraninfotech.com/g-mee-api/api/v1/\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            buildConfigField("String", "BASE_URL", "\"https://navkiraninfotech.com/g-mee-api/api/v1/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    //enable data binding
    buildFeatures {
        dataBinding = true
    }

    // to enable addition of buildConfigField fields
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)

    // mdc
    implementation(libs.material)

    // constraint layout
    implementation(libs.androidx.constraintlayout)

    //ViewModel And LiveData Related Dependencies------------------
    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // alternately - if using Java8, use the following instead of lifecycle-compiler
    implementation(libs.androidx.lifecycle.common.java8)

    //coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)


    // Retrofit dependency
    implementation(libs.retrofit)
    //Gson dependency
    implementation(libs.converter.gson)
    //Logging interceptor dependency
    implementation(libs.logging.interceptor)

    //ssp
    implementation(libs.ssp.android)

    //sdp
    implementation(libs.sdp.android)

    // Glide
    implementation(libs.glide)
    ksp(libs.glide.ksp)

    //HILT
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    //recycler view
    implementation(libs.androidx.recyclerview)

    //fragment ktx for by viewModel
    implementation(libs.androidx.fragment.ktx)
}

//added for HILT
kapt {
    correctErrorTypes = true
}

//added for HILT
hilt {
    enableAggregatingTask = true
}