import React, {useEffect} from 'react';
import {View, Button, NativeModules, NativeEventEmitter} from 'react-native';

const {AdMobInterstitialModule, AdMobRewardedModule} = NativeModules;

const App = () => {
  useEffect(() => {
    // Her modül için ayrı emitter oluştur
    const interstitialEmitter = new NativeEventEmitter(AdMobInterstitialModule);
    const rewardedEmitter = new NativeEventEmitter(AdMobRewardedModule);

    // Interstitial reklam için hata dinleyici
    const interstitialErrorSubscription = interstitialEmitter.addListener(
      'onAdFailedToLoad',
      error => {
        console.log(`Interstitial reklam yüklenemedi: ${error}`);
        alert(`Interstitial reklam yüklenemedi: ${error}`);
      },
    );

    // Rewarded reklam için ödül dinleyici
    const rewardSubscription = rewardedEmitter.addListener(
      'onRewardEarned',
      amount => {
        console.log(`Ödül kazanıldı: ${amount}`);
        alert(`Ödül kazanıldı: ${amount}`);
      },
    );

    // Rewarded reklam için hata dinleyici
    const rewardedErrorSubscription = rewardedEmitter.addListener(
      'onAdFailedToLoad',
      error => {
        console.log(`Ödüllü reklam yüklenemedi: ${error}`);
        alert(`Ödüllü reklam yüklenemedi: ${error}`);
      },
    );

    // Emitter'ların doğru şekilde oluşturulduğunu logla
    console.log('Emitterlar oluşturuldu:', {
      interstitialEmitter,
      rewardedEmitter,
    });

    // Temizlik fonksiyonu
    return () => {
      console.log('Dinleyiciler kaldırılıyor...');
      interstitialErrorSubscription.remove();
      rewardSubscription.remove();
      rewardedErrorSubscription.remove();
    };
  }, []);

  const showInterstitialAd = () => {
    console.log('Interstitial reklam yükleniyor...');
    AdMobInterstitialModule.loadAndShowInterstitialAd();
  };

  const showRewardedAd = () => {
    console.log('Ödüllü reklam yükleniyor...');
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
