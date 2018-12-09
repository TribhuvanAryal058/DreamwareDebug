"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();
const CUT_OFF_TIME = 60 * 1000;
exports.deleteOldItems = functions.database.ref('/T/{group}/{item}/{pushid}').onCreate((snapshot, context) => {
    const parentRef = snapshot.ref.parent;
    const Now = Date.now();
    const now = Now - CUT_OFF_TIME;
    console.log(parentRef);
    console.log(now);
    const oldItemsQuery = parentRef.orderByChild('time').endAt(now);
    return oldItemsQuery.once('value').then(snap => {
        console.log(snap.numChildren());
        const promises = [];
        snap.forEach(childSnap => {
            console.log(childSnap.val().id);
            return false;
        });
        return null;
    }).catch(Error => {
        console.error(Error);
    });
});
exports.addUser = functions.auth.user().onCreate((user, context) => {
    console.log(user.uid);
    const uid = user.uid;
    return admin.database().ref('/Users/').child(uid).set('new');
});
exports.removeUser = functions.auth.user().onDelete((user, context) => {
    const uid = user.uid;
    return admin.database().ref('/Users/').child(uid).set(null);
});
//# sourceMappingURL=index.js.map