package com.calyrsoft.ucbp1

import android.app.Application
import android.util.Log
import com.calyrsoft.ucbp1.core.config.RemoteConfigService
import com.calyrsoft.ucbp1.di.appModule
import com.calyrsoft.ucbp1.features.logs.LogScheduler
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        // Inicializar Firebase usando google-services.json automáticamente
        try {
            FirebaseApp.initializeApp(this)
            Log.d("HireTree", "✅ Firebase inicializado correctamente")
        } catch (e: Exception) {
            Log.e("HireTree", "❌ Error al inicializar Firebase: ${e.message}", e)
        }

        // Inicializar Remote Config
        initializeRemoteConfig()

        LogScheduler(this).schedulePeriodicaUpload()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }

    private fun initializeRemoteConfig() {
        applicationScope.launch {
            try {
                val remoteConfig = RemoteConfigService()
                val result = remoteConfig.fetchConfig(fetchFromServer = true)

                if (result.isSuccess) {
                    Log.i("HireTree", "✅ Remote Config inicializado correctamente")
                    remoteConfig.logCurrentConfig()
                } else {
                    Log.w("HireTree", "⚠️ Remote Config usando valores por defecto")
                }
            } catch (e: Exception) {
                Log.e("HireTree", "❌ Error al inicializar Remote Config: ${e.message}")
            }
        }
    }
}