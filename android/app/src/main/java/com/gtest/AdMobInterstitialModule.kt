package com.gtest

import android.util.Log
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
        Log.d("AdMobInterstitial", "loadAndShowInterstitialAd called")
        currentActivity?.runOnUiThread {
            Log.d("AdMobInterstitial", "Running on UI thread")
            val adRequest = AdRequest.Builder().build() // addTestDevice kaldırıldı
            InterstitialAd.load(
                reactApplicationContext,
                "ca-app-pub-3940256099942544/1033173712", // Test Interstitial Ad Unit ID
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        Log.d("AdMobInterstitial", "Interstitial ad loaded successfully")
                        interstitialAd = ad
                        currentActivity?.let { activity ->
                            interstitialAd?.show(activity)
                            Log.d("AdMobInterstitial", "Interstitial ad shown")
                        } ?: run {
                            Log.d("AdMobInterstitial", "Activity is null during ad show")
                            reactApplicationContext
                                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                                .emit("onAdFailedToLoad", "Activity is null during ad show")
                        }
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.e("AdMobInterstitial", "Ad failed to load: Error Code: ${error.code}, Message: ${error.message}, Domain: ${error.domain}, Cause: ${error.cause?.toString() ?: "Unknown"}")
                        interstitialAd = null
                        val errorMessage = "Error Code: ${error.code}, Message: ${error.message}, Domain: ${error.domain}, Cause: ${error.cause?.toString() ?: "Unknown"}"
                        reactApplicationContext
                            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                            .emit("onAdFailedToLoad", errorMessage)
                    }
                }
            )
        } ?: run {
            Log.e("AdMobInterstitial", "Activity is null during ad load")
            reactApplicationContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("onAdFailedToLoad", "Activity is null during ad load")
        }
    }
}