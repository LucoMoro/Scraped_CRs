/*Supporting oem unsolicited communication

Android has support for unsolicited communication but doesn't
has interfaces open to java application. This is a contribution to
Android open source where it allows android java application to use
API's to register for oem unsolicited communication.

Change-Id:I6e45ae40a3fc10a301b41fa668623542cf05a5d5*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CommandsInterface.java b/telephony/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index 5de0426..c9afa64 100644

//Synthetic comment -- @@ -461,6 +461,16 @@
void unregisterForSignalInfo(Handler h);

/**
     * Sets the handler for Event Notifications for oem unsolicited Info.
     *
     * @param h Handler for notification message.
     * @param what User-defined message code.
     * @param obj User object.
     */
    public void setOnUnsolOemHookRaw(Handler h, int what, Object obj);
    public void unSetOnUnsolOemHookRaw(Handler h);

    /**
* Registers the handler for CDMA number information record
* Unlike the register* methods, there's only one notification handler
*








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Phone.java b/telephony/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 3c9a1f8..bce593a 100644

//Synthetic comment -- @@ -1198,6 +1198,32 @@
void invokeOemRilRequestStrings(String[] strings, Message response);

/**
     * Initiates RIL_UNSOL_OEM_HOOK_RAW on RIL implementation.
     * specific to oems hook libraries to use to accomplish communication
     * from native ril to android ril and provide further communication to
     * java application layer.
     *
     * @param h MessageHandler which is part of client application
     * which will monitor for event id (int what)
     * @param what Event Id on which client will be interested to get a
     * unsolicited request
     * @param Object obj not used
     */
    void initiateUnsolOemHookRaw(Handler h, int what, Object obj);

    /**
     * Releases the object created for unsolicited communication
     * The application which calls InitiateUnsolOemHookRaw should call
     * this function to release when application no longer need unsol-
     * cited communication from the native and the android RIL.
     *
     * @param h MessageHandler which is part of client application
     * which will monitor for event id (int what)
     * @return void
     */
    public void releaseUnsolOemHookRaw(Handler h);

    /**
* Get the current active Data Call list
*
* @param response <strong>On success</strong>, "response" bytes is








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index c3c8f5e..b71742e 100644

//Synthetic comment -- @@ -728,6 +728,37 @@
mCM.invokeOemRilRequestStrings(strings, response);
}

    /**
     * Initiates the unsolicited communication for oem ril. This is
     * specific to oems hook libraries to use to accomplish communication
     * from native ril to android ril and provide further communication to
     * java application layer.
     *
     * @param h MessageHandler which is part of client application
     * which will monitor for event id (int what)
     * @param what Event Id on which client will be interested to get a
     * unsolicited request
     * @param Object obj not used
     * @return void
     */
    public void initiateUnsolOemHookRaw(Handler h, int what, Object obj) {
        mCM.setOnUnsolOemHookRaw(h, what, obj);
    }

    /**
     * Releases the object created for unsolicited communication
     * The application which calls InitiateUnsolOemHookRaw should call
     * this function to release when applicaiton no longer need unsol-
     * cited communication from the native and the android RIL.
     *
     * @param h MessageHandler which is part of client application
     * which will monitor for event id (int what)
     * @return void
     */
    public void releaseUnsolOemHookRaw(Handler h) {
        mCM.unSetOnUnsolOemHookRaw(h);
    }

public void notifyDataActivity() {
mNotifier.notifyDataActivity(this);
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneProxy.java b/telephony/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 5e7dcb0..afa52f0 100644

//Synthetic comment -- @@ -576,6 +576,14 @@
mActivePhone.invokeOemRilRequestStrings(strings, response);
}

    public void initiateUnsolOemHookRaw(Handler h, int what, Object obj) {
        mActivePhone.initiateUnsolOemHookRaw(h, what, obj);
    }

    public void releaseUnsolOemHookRaw(Handler h) {
        mActivePhone.releaseUnsolOemHookRaw(h);
    }

public void getDataCallList(Message response) {
mActivePhone.getDataCallList(response);
}







