package com.gtest

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdMobRewardedModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private var rewardedAd: RewardedAd? = null

    override fun getName(): String = "AdMobRewardedModule"

    @ReactMethod
    fun loadAndShowRewardedAd() {
        // Ana iş parçacığında çalıştır
        currentActivity?.runOnUiThread {
            val adRequest = AdRequest.Builder()
                .addTestDevice("YOUR_DEVICE_ID") // Logcat'ten aldığınız cihaz ID'sini buraya ekleyin
                .build()
            RewardedAd.load(
                reactApplicationContext,
                "ca-app-pub-3940256099942544/5224354917", // Test Rewarded Ad Unit ID
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        rewardedAd = ad
                        currentActivity?.let { activity ->
                            rewardedAd?.show(activity) { rewardItem ->
                                reactApplicationContext
                                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                                    .emit("onRewardEarned", rewardItem.amount)
                            }
                        } ?: run {
                            reactApplicationContext
                                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                                .emit("onAdFailedToLoad", "Activity is null")
                        }
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        rewardedAd = null
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