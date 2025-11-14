// ================================================
// Firebase Configuration
// ================================================
// 🔥 IMPORTANT: Replace this with YOUR Firebase config!
// Get this from Firebase Console → Project Settings → Your apps → Web app

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyC-NYOlKhFKQGsQR310oYIqjrVkiiNCaDw",
  authDomain: "emergencymedical-a43a9.firebaseapp.com",
  databaseURL: "https://emergencymedical-a43a9-default-rtdb.firebaseio.com",
  projectId: "emergencymedical-a43a9",
  storageBucket: "emergencymedical-a43a9.firebasestorage.app",
  messagingSenderId: "493706183621",
  appId: "1:493706183621:web:d1d0aeac356e53004cc1ac"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);

// Get a reference to the database
const database = firebase.database();

console.log("🔥 Firebase initialized successfully!");
console.log("📍 Database URL:", firebaseConfig.databaseURL);
