package com.gtest

import android.os.Bundle
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint
import com.facebook.react.defaults.DefaultReactActivityDelegate
import com.google.android.gms.ads.MobileAds

class MainActivity : ReactActivity() {
    override fun getMainComponentName(): String = "gtest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) { initializationStatus ->
            // Başlatma tamamlandı
        }
    }

    override fun createReactActivityDelegate(): ReactActivityDelegate {
        return DefaultReactActivityDelegate(
            this,
            mainComponentName,
            DefaultNewArchitectureEntryPoint.fabricEnabled,
            DefaultNewArchitectureEntryPoint.concurrentReactEnabled
        )
    }
}