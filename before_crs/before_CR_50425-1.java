/*Telephony: Ignore events on a destroyed phone

It is possible to receive responses from RIL after phone has released
it's references. Ignore events in such cases, since those events no
longer make sense, and some phone members have been de-initialized.

Make CallTrackers notify UI when ending calls in dispose, since
it has already unregistered, and won't receive the CALL_STATE_CHANGED

Change-Id:I5d8fd9ce3f74b9ae9b5b645565bd24d11be0aebcCRs-Fixed: 228731, 228005, 415801, 350739*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index c62f72f..6e11267 100644

//Synthetic comment -- @@ -303,6 +303,11 @@
public void handleMessage(Message msg) {
AsyncResult ar;

switch(msg.what) {
case EVENT_CALL_RING:
Rlog.d(LOG_TAG, "Event EVENT_CALL_RING Received state=" + getState());








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMAPhone.java b/src/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 1465a17..7ac93c9 100755

//Synthetic comment -- @@ -953,6 +953,11 @@
AsyncResult ar;
Message     onComplete;

switch(msg.what) {
case EVENT_RADIO_AVAILABLE: {
mCM.getBasebandVersion(obtainMessage(EVENT_GET_BASEBAND_VERSION_DONE));








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaCallTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaCallTracker.java
//Synthetic comment -- index 8ec5633..1f3ace9 100644

//Synthetic comment -- @@ -110,14 +110,24 @@
cm.unregisterForCallWaitingInfo(this);
for(CdmaConnection c : connections) {
try {
                if(c != null) hangup(c);
} catch (CallStateException ex) {
Rlog.e(LOG_TAG, "unexpected error on hangup during dispose");
}
}

try {
            if(pendingMO != null) hangup(pendingMO);
} catch (CallStateException ex) {
Rlog.e(LOG_TAG, "unexpected error on hangup during dispose");
}
//Synthetic comment -- @@ -947,6 +957,10 @@
handleMessage (Message msg) {
AsyncResult ar;

switch (msg.what) {
case EVENT_POLL_CALLS_RESULT:{
Rlog.d(LOG_TAG, "Event EVENT_POLL_CALLS_RESULT Received");








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaLteServiceStateTracker.java
//Synthetic comment -- index 54dc099..ed1c366 100644

//Synthetic comment -- @@ -76,6 +76,13 @@
AsyncResult ar;
int[] ints;
String[] strings;
switch (msg.what) {
case EVENT_POLL_STATE_GPRS:
if (DBG) log("handleMessage EVENT_POLL_STATE_GPRS");








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/src/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index a6f5aaf..eb854d0 100755

//Synthetic comment -- @@ -208,6 +208,8 @@
@Override
public void dispose() {
checkCorrectThread();
// Unregister for all events.
cm.unregisterForRadioStateChanged(this);
cm.unregisterForVoiceNetworkStateChanged(this);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index b930348..9aff98e 100644

//Synthetic comment -- @@ -1161,6 +1161,11 @@
AsyncResult ar;
Message onComplete;

switch (msg.what) {
case EVENT_RADIO_AVAILABLE: {
mCM.getBasebandVersion(








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java b/src/java/com/android/internal/telephony/gsm/GsmCallTracker.java
//Synthetic comment -- index 2080976..967eb6f 100644

//Synthetic comment -- @@ -112,14 +112,24 @@

for(GsmConnection c : connections) {
try {
                if(c != null) hangup(c);
} catch (CallStateException ex) {
Rlog.e(LOG_TAG, "unexpected error on hangup during dispose");
}
}

try {
            if(pendingMO != null) hangup(pendingMO);
} catch (CallStateException ex) {
Rlog.e(LOG_TAG, "unexpected error on hangup during dispose");
}
//Synthetic comment -- @@ -839,6 +849,11 @@
handleMessage (Message msg) {
AsyncResult ar;

switch (msg.what) {
case EVENT_POLL_CALLS_RESULT:
ar = (AsyncResult)msg.obj;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/src/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index bd13374..cb17370 100644

//Synthetic comment -- @@ -174,6 +174,12 @@
private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
@Override
public void onReceive(Context context, Intent intent) {
if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
// update emergency string whenever locale changed
updateSpnDisplay();
//Synthetic comment -- @@ -243,6 +249,8 @@
@Override
public void dispose() {
checkCorrectThread();
// Unregister for all events.
cm.unregisterForAvailable(this);
cm.unregisterForRadioStateChanged(this);
//Synthetic comment -- @@ -273,7 +281,7 @@
Message message;

if (!phone.mIsTheCurrentActivePhone) {
            Rlog.e(LOG_TAG, "Received message " + msg +
"[" + msg.what + "] while being destroyed. Ignoring.");
return;
}







