package com.gtest

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdMobInterstitialModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private var interstitialAd: InterstitialAd? = null

    override fun getName(): String = "AdMobInterstitialModule"

    @ReactMethod
    fun loadAndShowInterstitialAd() {
        // Ana iş parçacığında çalıştır
        currentActivity?.runOnUiThread {
            val adRequest = AdRequest.Builder()
                .addTestDevice("YOUR_DEVICE_ID") // Logcat'ten aldığınız cihaz ID'sini buraya ekleyin
                .build()
            InterstitialAd.load(
                reactApplicationContext,
                "ca-app-pub-3940256099942544/1033173712", // Test Interstitial Ad Unit ID
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        interstitialAd = ad
                        currentActivity?.let { activity ->
                            interstitialAd?.show(activity)
                        } ?: run {
                            reactApplicationContext
                                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                                .emit("onAdFailedToLoad", "Activity is null")
                        }
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        interstitialAd = null
                        reactApplicationContext
                            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                            .emit("onAdFailedToLoad", error.message)
                    }
                }
            )
        } ?: run {
            // Activity null ise hata gönder
            reactApplicationContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("onAdFailedToLoad", "Activity is null")
        }
    }
}