import ReactNativeAsyncStorage from '@react-native-async-storage/async-storage';

import {initializeApp} from 'firebase/app';
import {initializeAuth, getReactNativePersistence} from 'firebase/auth';

const firebaseConfig = {
  apiKey: 'AIzaSyB1Sg1c8LKwp8162aQO9Ws48AqjzXBmpS8',
  authDomain: 'ninja-ddda1.firebaseapp.com',
  databaseURL: 'https://ninja-ddda1.firebaseio.com',
  projectId: 'ninja-ddda1',
  storageBucket: 'ninja-ddda1.firebasestorage.app',
  messagingSenderId: '491406833717',
  appId: '1:491406833717:web:d24c9b389eb48149dad97c',
};

const app = initializeApp(firebaseConfig);

const auth = initializeAuth(app, {
  persistence: getReactNativePersistence(ReactNativeAsyncStorage),
});

export {app, auth};
