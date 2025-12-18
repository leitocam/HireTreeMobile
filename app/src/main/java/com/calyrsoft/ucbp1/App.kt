package com.calyrsoft.ucbp1

import android.app.Application
import com.calyrsoft.ucbp1.di.appModule
import com.calyrsoft.ucbp1.features.logs.LogScheduler
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        // Manual Firebase initialization to bypass the broken google-services plugin.
        val options = FirebaseOptions.Builder()
            .setApiKey("AIzaSyACVUyuSYQgr215m5bXtWQLLsVQ_Tkpn5Y")
            .setApplicationId("1:655273697086:android:f6e81ed8054eff32e9166f")
            .setProjectId("hiretree-248d4")
            .setStorageBucket("hiretree-248d4.firebasestorage.app")
            .setGcmSenderId("655273697086")
            .build()

        FirebaseApp.initializeApp(this, options)

        LogScheduler(this).schedulePeriodicaUpload()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}