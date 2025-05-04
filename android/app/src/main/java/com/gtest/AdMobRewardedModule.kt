package com.gtest

import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdMobRewardedModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private var rewardedAd: RewardedAd? = null

    override fun getName(): String = "AdMobRewardedModule"

    @ReactMethod
    fun loadAndShowRewardedAd(promise: Promise) {
        Log.d("AdMobRewarded", "loadAndShowRewardedAd called")
        currentActivity?.runOnUiThread {
            Log.d("AdMobRewarded", "Running on UI thread")
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(
                reactApplicationContext,
                "ca-app-pub-3940256099942544/5224354917", // Test Rewarded Ad Unit ID
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        Log.d("AdMobRewarded", "Rewarded ad loaded successfully")
                        rewardedAd = ad
                        currentActivity?.let { activity ->
                            rewardedAd?.show(activity) { rewardItem ->
                                Log.d("AdMobRewarded", "Reward earned: ${rewardItem.amount}")
                                promise.resolve(rewardItem.amount)
                            }
                            Log.d("AdMobRewarded", "Rewarded ad shown")
                        } ?: run {
                            Log.d("AdMobRewarded", "Activity is null during ad show")
                            promise.reject("AD_ERROR", "Activity is null during ad show")
                        }
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.e("AdMobRewarded", "Ad failed to load: Error Code: ${error.code}, Message: ${error.message}, Domain: ${error.domain}, Cause: ${error.cause?.toString() ?: "Unknown"}")
                        rewardedAd = null
                        val errorMessage = "Error Code: ${error.code}, Message: ${error.message}, Domain: ${error.domain}, Cause: ${error.cause?.toString() ?: "Unknown"}"
                        promise.reject("AD_ERROR", errorMessage)
                    }
                }
            )
        } ?: run {
            Log.e("AdMobRewarded", "Activity is null during ad load")
            promise.reject("AD_ERROR", "Activity is null during ad load")
        }
    }
}