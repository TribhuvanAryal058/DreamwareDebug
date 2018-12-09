const functions = require('firebase-functions');
const admin = require("firebase-admin");
admin.initializeApp();

exports.addUser = functions.auth.user().onCreate((user, context) => {
    console.log(user.uid);
    const uid = user.uid;
    return admin.database().ref('/Users/').child(uid).set('new');
});
exports.removeUser = functions.auth.user().onDelete((user, context) => {
    const uid = user.uid;
    return admin.database().ref('/Users/').child(uid).set(null);
});
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
