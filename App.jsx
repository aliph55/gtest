import React, {useEffect} from 'react';
import {View, Button, NativeModules, NativeEventEmitter} from 'react-native';

const {AdMobInterstitialModule, AdMobRewardedModule} = NativeModules;

const interstitialEmitter = new NativeEventEmitter(AdMobInterstitialModule);
const rewardedEmitter = new NativeEventEmitter(AdMobRewardedModule);

const App = () => {
  useEffect(() => {
    const interstitialErrorSubscription = interstitialEmitter.addListener(
      'onAdFailedToLoad',
      error => {
        console.log(`Interstitial reklam yüklenemedi: ${error}`);
        alert(`Interstitial reklam yüklenemedi: ${error}`);
      },
    );

    const rewardSubscription = rewardedEmitter.addListener(
      'onRewardEarned',
      amount => {
        console.log(`Ödül kazanıldı: ${amount}`);
        alert(`Ödül kazanıldı: ${amount}`);
      },
    );

    const rewardedErrorSubscription = rewardedEmitter.addListener(
      'onAdFailedToLoad',
      error => {
        console.log(`Ödüllü reklam yüklenemedi: ${error}`);
        alert(`Ödüllü reklam yüklenemedi: ${error}`);
      },
    );

    return () => {
      interstitialErrorSubscription.remove();
      rewardSubscription.remove();
      rewardedErrorSubscription.remove();
    };
  }, []);

  const showInterstitialAd = () => {
    AdMobInterstitialModule.loadAndShowInterstitialAd();
  };

  const showRewardedAd = () => {
    AdMobRewardedModule.loadAndShowRewardedAd();
  };

  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
      <Button title="Geçiş Reklamını Göster" onPress={showInterstitialAd} />
      <Button title="Ödüllü Reklamı Göster" onPress={showRewardedAd} />
    </View>
  );
};

export default App;
