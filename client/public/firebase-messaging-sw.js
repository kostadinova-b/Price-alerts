importScripts('https://www.gstatic.com/firebasejs/9.0.1/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.0.1/firebase-messaging-compat.js');

let body = '';

const firebaseConfig = {
  apiKey: "AIzaSyCQ5hggZ3aAHIGsP0vQG1xVZBDRr3kASQQ",
    authDomain: "final-proj-e8fc0.firebaseapp.com",
    projectId: "final-proj-e8fc0",
    storageBucket: "final-proj-e8fc0.appspot.com",
    messagingSenderId: "145682721153",
    appId: "1:145682721153:web:06c643cb56c75a4e3a2080",
    measurementId: "G-GHF514N1C0"
};

firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();

self.addEventListener('push', (event) => {
 // console.log('Received a push notification:', event);

  const notificationData = event.data.json();
  //console.log(notificationData);

  const options = {
    body: notificationData.notification.body,
    icon: 'alert1.png',
    data: {
      url: notificationData.url,
    },
  };
  body = notificationData.notification.body;
  event.waitUntil(self.registration.showNotification(notificationData.notification.title, options));
});



self.addEventListener('notificationclick', (event) => {
  console.log('Clicked on a notification:', event);
    const regex = /#instrument_(\d+)/;
    const match = body.match(regex);
    console.log(body+" "+match);
    let number = 0;
    if (match) {
      number = match[1];
}
event.waitUntil(clients.openWindow(`http://localhost:3000/stocks/s?id=${number}&img=/static/media/stock1.8b35dd8c86c080a65b57.jpg&currency=USD&market=NYSE`));

});