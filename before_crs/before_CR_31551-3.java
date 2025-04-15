/*Frameworks/base (DSDS):  Handle ril instances for two rild

Change-Id:Id415cd124340930f7b11126154b211379aa517a3*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index f2e7f45..314d322 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -231,6 +231,7 @@

Object     mLastNITZTimeInfo;

//***** Events

static final int EVENT_SEND                 = 1;
//Synthetic comment -- @@ -487,22 +488,35 @@

class RILReceiver implements Runnable {
byte[] buffer;

RILReceiver() {
buffer = new byte[RIL_MAX_COMMAND_BYTES];
}

public void
run() {
int retryCount = 0;

try {for (;;) {
LocalSocket s = null;
LocalSocketAddress l;

try {
s = new LocalSocket();
                    l = new LocalSocketAddress(SOCKET_NAME_RIL,
LocalSocketAddress.Namespace.RESERVED);
s.connect(l);
} catch (IOException ex){
//Synthetic comment -- @@ -519,12 +533,12 @@

if (retryCount == 8) {
Log.e (LOG_TAG,
                            "Couldn't find '" + SOCKET_NAME_RIL
+ "' socket after " + retryCount
+ " times, continuing to retry silently");
} else if (retryCount > 0 && retryCount < 8) {
Log.i (LOG_TAG,
                            "Couldn't find '" + SOCKET_NAME_RIL
+ "' socket; retrying after timeout");
}

//Synthetic comment -- @@ -540,7 +554,7 @@
retryCount = 0;

mSocket = s;
                Log.i(LOG_TAG, "Connected to '" + SOCKET_NAME_RIL + "' socket");

int length = 0;
try {
//Synthetic comment -- @@ -566,14 +580,14 @@
p.recycle();
}
} catch (java.io.IOException ex) {
                    Log.i(LOG_TAG, "'" + SOCKET_NAME_RIL + "' socket closed",
ex);
} catch (Throwable tr) {
Log.e(LOG_TAG, "Uncaught exception read length=" + length +
"Exception:" + tr.toString());
}

                Log.i(LOG_TAG, "Disconnected from '" + SOCKET_NAME_RIL
+ "' socket");

setRadioState (RadioState.RADIO_UNAVAILABLE);
//Synthetic comment -- @@ -600,13 +614,17 @@


//***** Constructors

public RIL(Context context, int preferredNetworkType, int cdmaSubscription) {
super(context);
if (RILJ_LOGD) {
riljLog("RIL(context, preferredNetworkType=" + preferredNetworkType +
" cdmaSubscription=" + cdmaSubscription + ")");
}
mCdmaSubscription  = cdmaSubscription;
mPreferredNetworkType = preferredNetworkType;
mPhoneType = RILConstants.NO_PHONE;
//Synthetic comment -- @@ -631,7 +649,7 @@
riljLog("Not starting RILReceiver: wifi-only");
} else {
riljLog("Starting RILReceiver");
            mReceiver = new RILReceiver();
mReceiverThread = new Thread(mReceiver, "RILReceiver");
mReceiverThread.start();

//Synthetic comment -- @@ -3548,11 +3566,13 @@
}

private void riljLog(String msg) {
        Log.d(LOG_TAG, msg);
}

private void riljLogv(String msg) {
        Log.v(LOG_TAG, msg);
}

private void unsljLog(int response) {







