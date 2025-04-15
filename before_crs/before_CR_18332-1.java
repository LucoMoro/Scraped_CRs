/*frameworks/base/telephony: Ignore events on a destroyed phone

	It is possible to receive responses from RIL after phone has released
	it's references. Ignore events in such cases, since those events no
	longer make sense, and some phone members have been de-initialized.

Change-Id:If3e6c43f55eacf1bc7d2cb020dc7394268271c3f*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index c3c8f5e..58ee2bf 100644

//Synthetic comment -- @@ -243,6 +243,11 @@
public void handleMessage(Message msg) {
AsyncResult ar;

switch(msg.what) {
case EVENT_CALL_RING:
Log.d(LOG_TAG, "Event EVENT_CALL_RING Received state=" + getState());








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 1c81a07..c517269 100755

//Synthetic comment -- @@ -990,6 +990,11 @@
AsyncResult ar;
Message     onComplete;

switch(msg.what) {
case EVENT_RADIO_AVAILABLE: {
mCM.getBasebandVersion(obtainMessage(EVENT_GET_BASEBAND_VERSION_DONE));








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaCallTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaCallTracker.java
//Synthetic comment -- index 3669e60..0f9f4c6 100644

//Synthetic comment -- @@ -920,6 +920,10 @@
handleMessage (Message msg) {
AsyncResult ar;

switch (msg.what) {
case EVENT_POLL_CALLS_RESULT:{
Log.d(LOG_TAG, "Event EVENT_POLL_CALLS_RESULT Received");








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java b/telephony/java/com/android/internal/telephony/cdma/CdmaServiceStateTracker.java
//Synthetic comment -- index d2a4bd8..a48754e 100755

//Synthetic comment -- @@ -292,6 +292,11 @@
int[] ints;
String[] strings;

switch (msg.what) {
case EVENT_RADIO_AVAILABLE:
break;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java b/telephony/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index 87b0c60..00a557f6 100644

//Synthetic comment -- @@ -187,6 +187,11 @@

boolean isRecordLoadResponse = false;

try { switch (msg.what) {
case EVENT_RUIM_READY:
onRuimReady();








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index 49de5f9..29ad626 100644

//Synthetic comment -- @@ -1204,6 +1204,11 @@
AsyncResult ar;
Message onComplete;

switch (msg.what) {
case EVENT_RADIO_AVAILABLE: {
mCM.getBasebandVersion(








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmCallTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmCallTracker.java
//Synthetic comment -- index 06f310c..13fa3ac 100644

//Synthetic comment -- @@ -831,6 +831,11 @@
handleMessage (Message msg) {
AsyncResult ar;

switch (msg.what) {
case EVENT_POLL_CALLS_RESULT:
ar = (AsyncResult)msg.obj;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmServiceStateTracker.java
//Synthetic comment -- index d539f6f1..60b6b07 100644

//Synthetic comment -- @@ -343,6 +343,11 @@
String[] strings;
Message message;

switch (msg.what) {
case EVENT_RADIO_AVAILABLE:
//this is unnecessary








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index c80c608..55ead1d 100644

//Synthetic comment -- @@ -470,6 +470,11 @@

boolean isRecordLoadResponse = false;

try { switch (msg.what) {
case EVENT_SIM_READY:
onSimReady();







