package com.gtest

import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdMobInterstitialModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private var interstitialAd: InterstitialAd? = null

    override fun getName(): String = "AdMobInterstitialModule"

    @ReactMethod
    fun loadAndShowInterstitialAd(promise: Promise) {
        Log.d("AdMobInterstitial", "loadAndShowInterstitialAd called")
        currentActivity?.runOnUiThread {
            Log.d("AdMobInterstitial", "Running on UI thread")
            val adRequest = AdRequest.Builder().build()
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
                            promise.resolve("Interstitial ad shown successfully")
                        } ?: run {
                            Log.d("AdMobInterstitial", "Activity is null during ad show")
                            promise.reject("AD_ERROR", "Activity is null during ad show")
                        }
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.e("AdMobInterstitial", "Ad failed to load: Error Code: ${error.code}, Message: ${error.message}, Domain: ${error.domain}, Cause: ${error.cause?.toString() ?: "Unknown"}")
                        interstitialAd = null
                        val errorMessage = "Error Code: ${error.code}, Message: ${error.message}, Domain: ${error.domain}, Cause: ${error.cause?.toString() ?: "Unknown"}"
                        promise.reject("AD_ERROR", errorMessage)
                    }
                }
            )
        } ?: run {
            Log.e("AdMobInterstitial", "Activity is null during ad load")
            promise.reject("AD_ERROR", "Activity is null during ad load")
        }
    }
}