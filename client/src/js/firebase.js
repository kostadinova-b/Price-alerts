import { initializeApp } from 'firebase/app';
import { getMessaging, getToken, onMessage } from "firebase/messaging"; 

var firebaseConfig = {
    apiKey: "AIzaSyCQ5hggZ3aAHIGsP0vQG1xVZBDRr3kASQQ",
    authDomain: "final-proj-e8fc0.firebaseapp.com",
    projectId: "final-proj-e8fc0",
    storageBucket: "final-proj-e8fc0.appspot.com",
    messagingSenderId: "145682721153",
    appId: "1:145682721153:web:06c643cb56c75a4e3a2080",
    measurementId: "G-GHF514N1C0"
};

const firebaseApp = initializeApp(firebaseConfig);
const messaging = getMessaging(firebaseApp);

export const getUserToken = async (setTokenFound) => {
    try {
        const currentToken = await getToken(messaging, { vapidKey: 'BCShVbMzKJh5reEvzboVVs_doqo2-v_WGTMoLI9-Eh1oqGB6MM9xT6N2IAMGdWdPcYoYJcSGalD-F3fb2o_Js-c' });
        if (currentToken) {
            console.log('current token for client: ', currentToken);
            setTokenFound(true);
            return currentToken;
        } else {
            console.log('No registration token available. Request permission to generate one.');
            setTokenFound(false);
        }
    } catch (err) {
        console.log('An error occurred while retrieving token. ', err);
    }
  }

  export const onMessageListener = () =>
  new Promise((resolve) => {
    onMessage(messaging, (payload) => {
      resolve(payload);
    });
});