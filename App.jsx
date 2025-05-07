import {Image, StyleSheet, TouchableOpacity, View} from 'react-native';
import React, {useEffect, useState} from 'react';
import {
  GoogleSignin,
  statusCodes,
} from '@react-native-google-signin/google-signin';
import {
  GoogleAuthProvider,
  onAuthStateChanged,
  signInWithCredential,
} from 'firebase/auth';
import {auth} from './firebaseConfig';

const App = () => {
  const [user, setUser] = useState(null);

  const [idToken, setIdToken] = useState('');

  GoogleSignin.configure({
    scopes: ['https://www.googleapis.com/auth/userinfo.profile'], // what API you want to access on behalf of the user, default is email and profile
    webClientId:
      '491406833717-quac7f0k5afo10r3rvq0vr51fl6ufl3q.apps.googleusercontent.com', // client ID of type WEB for your server (needed to verify user ID and offline access). Required to get the `idToken` on the user object!
    fflineAccess: true,
    forceCodeForRefreshToken: true,
    profileImageSize: 120,
  });

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, currentUser => {
      //  console.log('currentUser: ', currentUser);
      setUser(currentUser);
    });
    return unsubscribe;
  }, []);

  useEffect(() => {
    if (idToken) {
      // const {id_token} = response.params;
      const credential = GoogleAuthProvider.credential(
        user?.stsTokenManager?.accessToken,
      );
      signInWithCredential(auth, credential)
        .then(userCredential => setUser(userCredential.user))
        .catch(error => console.error(error));
    }
  }, []);

  return (
    <View
      style={{
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
      }}>
      <TouchableOpacity
        onPress={async () => {
          try {
            await GoogleSignin.hasPlayServices();
            const userInfo = await GoogleSignin.signIn();
            // console.log('userInfo ', userInfo);
            setIdToken(userInfo.data.idToken);
            if (userInfo.type === 'success') {
              console.log(userInfo);
            }
          } catch (error) {
            if (error.code === statusCodes.SIGN_IN_CANCELLED) {
              console.log('cancelled', error);
              // user cancelled the login flow
            } else if (error.code === statusCodes.IN_PROGRESS) {
              console.log(
                'operation (e.g. sign in) is in progress already',
                error,
              );
              // operation (e.g. sign in) is in progress already
            } else if (error.code === statusCodes.PLAY_SERVICES_NOT_AVAILABLE) {
              // play services not available or outdated
              console.log(' play services not available or outdated ', error);
            } else if (error.code === statusCodes.SIGN_IN_REQUIRED) {
              console.log('SIGN_IN_REQUIRED ', error);
            } else {
              // some other error happened
              console.log('other ', error);
            }
          }
        }}>
        <Image source={require('./assets/google.png')} />
      </TouchableOpacity>
    </View>
  );
};

export default App;

const styles = StyleSheet.create({});
