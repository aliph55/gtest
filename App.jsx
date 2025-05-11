<<<<<<< Updated upstream
import React from 'react';
import {View, Button, NativeModules} from 'react-native';

const {AdMobInterstitialModule, AdMobRewardedModule} = NativeModules;

const App = () => {
  const showInterstitialAd = async () => {
    console.log('Interstitial reklam yükleniyor...');
    try {
      const result = await AdMobInterstitialModule.loadAndShowInterstitialAd();
      console.log(result);
      alert(result);
    } catch (error) {
      console.log(`Interstitial reklam yüklenemedi: ${error.message}`);
      alert(`Interstitial reklam yüklenemedi: ${error.message}`);
    }
  };

  const showRewardedAd = async () => {
    console.log('Ödüllü reklam yükleniyor...');
    try {
      const rewardAmount = await AdMobRewardedModule.loadAndShowRewardedAd();
      console.log(`Ödül kazanıldı: ${rewardAmount}`);
      alert(`Ödül kazanıldı: ${rewardAmount}`);
    } catch (error) {
      console.log(`Ödüllü reklam yüklenemedi: ${error.message}`);
      alert(`Ödüllü reklam yüklenemedi: ${error.message}`);
    }
  };
=======
import {View, Text} from 'react-native';
import {createStaticNavigation} from '@react-navigation/native';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
>>>>>>> Stashed changes

function HomeScreen() {
  return (
<<<<<<< Updated upstream
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
      <Button title="Geçiş Reklamını Göster" onPress={showInterstitialAd} />
      <Button title="Ödüllü Reklamı Göster" onPress={showRewardedAd} />
=======
    <View style={{flex: 1, alignItems: 'center', justifyContent: 'center'}}>
      <Text>Home Screen</Text>
>>>>>>> Stashed changes
    </View>
  );
}

<<<<<<< Updated upstream
export default App;
=======
const RootStack = createNativeStackNavigator({
  screens: {
    Home: HomeScreen,
  },
});

const Navigation = createStaticNavigation(RootStack);

export default function App() {
  return <Navigation />;
}
>>>>>>> Stashed changes
