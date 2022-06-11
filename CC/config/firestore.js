const admin = require("firebase-admin");

// const serviceAccount = require('../service-account-key.json')

// const firebase = admin.initializeApp({
//   credential: admin.credential.cert(serviceAccount)
// });
const firebase = admin.initializeApp({
  credential: admin.credential.applicationDefault()
});

module.exports = firebase