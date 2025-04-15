/*Frameworks/base (DSDS):  Handle ril instances for two rild

Change-Id:Id415cd124340930f7b11126154b211379aa517a3*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index f2e7f45..4de13bd 100644

//Synthetic comment -- @@ -1,5 +1,5 @@
/*
 * Copyright (C) 2006, 2012 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -81,6 +81,7 @@
Parcel mp;
RILRequest mNext;


/**
* Retrieves a new RILRequest instance from the pool.
*
//Synthetic comment -- @@ -231,6 +232,7 @@

Object     mLastNITZTimeInfo;

    private Integer mInstanceId;
//***** Events

static final int EVENT_SEND                 = 1;
//Synthetic comment -- @@ -487,22 +489,35 @@

class RILReceiver implements Runnable {
byte[] buffer;
        private Integer mInstId;

RILReceiver() {
buffer = new byte[RIL_MAX_COMMAND_BYTES];
}

        RILReceiver(Integer inst) {
            this();
            mInstId = inst;
        }

public void
run() {
int retryCount = 0;
            String rilSocket = "rild";

try {for (;;) {
LocalSocket s = null;
LocalSocketAddress l;

                if (mInstId == null || mInstId == 0) {
                    rilSocket = SOCKET_NAME_RIL;
                } else {
                    rilSocket = SOCKET_NAME_RIL + mInstId;
                }

try {
s = new LocalSocket();
                    l = new LocalSocketAddress(rilSocket,
LocalSocketAddress.Namespace.RESERVED);
s.connect(l);
} catch (IOException ex){
//Synthetic comment -- @@ -519,12 +534,12 @@

if (retryCount == 8) {
Log.e (LOG_TAG,
                            "Couldn't find '" + rilSocket
+ "' socket after " + retryCount
+ " times, continuing to retry silently");
} else if (retryCount > 0 && retryCount < 8) {
Log.i (LOG_TAG,
                            "Couldn't find '" + rilSocket
+ "' socket; retrying after timeout");
}

//Synthetic comment -- @@ -540,7 +555,7 @@
retryCount = 0;

mSocket = s;
                Log.i(LOG_TAG, "Connected to '" + rilSocket + "' socket");

int length = 0;
try {
//Synthetic comment -- @@ -566,14 +581,14 @@
p.recycle();
}
} catch (java.io.IOException ex) {
                    Log.i(LOG_TAG, "'" + rilSocket + "' socket closed",
ex);
} catch (Throwable tr) {
Log.e(LOG_TAG, "Uncaught exception read length=" + length +
"Exception:" + tr.toString());
}

                Log.i(LOG_TAG, "Disconnected from '" + rilSocket
+ "' socket");

setRadioState (RadioState.RADIO_UNAVAILABLE);
//Synthetic comment -- @@ -600,13 +615,17 @@


//***** Constructors
public RIL(Context context, int preferredNetworkType, int cdmaSubscription) {
        this(context, preferredNetworkType, cdmaSubscription, null);
    }

    public RIL(Context context, int preferredNetworkType, int cdmaSubscription, Integer instanceId) {
super(context);
if (RILJ_LOGD) {
riljLog("RIL(context, preferredNetworkType=" + preferredNetworkType +
" cdmaSubscription=" + cdmaSubscription + ")");
}
        mInstanceId = instanceId;
mCdmaSubscription  = cdmaSubscription;
mPreferredNetworkType = preferredNetworkType;
mPhoneType = RILConstants.NO_PHONE;
//Synthetic comment -- @@ -631,7 +650,7 @@
riljLog("Not starting RILReceiver: wifi-only");
} else {
riljLog("Starting RILReceiver");
            mReceiver = new RILReceiver(mInstanceId);
mReceiverThread = new Thread(mReceiver, "RILReceiver");
mReceiverThread.start();

//Synthetic comment -- @@ -3548,11 +3567,13 @@
}

private void riljLog(String msg) {
        Log.d(LOG_TAG, msg
                + (mInstanceId != null ? (" [SUB" + mInstanceId + "]") : ""));
}

private void riljLogv(String msg) {
        Log.v(LOG_TAG, msg
                + (mInstanceId != null ? (" [SUB" + mInstanceId + "]") : ""));
}

private void unsljLog(int response) {







