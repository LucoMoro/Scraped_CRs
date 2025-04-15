/*Supporting oem unsolicited communication

Android has support for unsolicited communication but doesn't
have interfaces open to java application. There are operator
requirements for communicating with the vendor RIL which
is what this API could be used for.

Change-Id:I4c474c8e8536ba76b30fba9d342a153d1603e82b*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CommandsInterface.java b/src/java/com/android/internal/telephony/CommandsInterface.java
//Synthetic comment -- index f7757b3..ec1a48f 100644

//Synthetic comment -- @@ -417,6 +417,16 @@
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








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/Phone.java b/src/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 34aa96c..2a902fb 100644

//Synthetic comment -- @@ -1156,6 +1156,37 @@
*/
void invokeOemRilRequestStrings(String[] strings, Message response);

   /**
    * initiates RIL_UNSOL_OEM_HOOK_RAW on RIL implementation.
    * specific to oems hook libraries to use to accomplish communication
    * from native ril to android ril and provide further communication to
    * java application layer.
    *
    * @param Handler h
    *          MessageHandler which is part of client application
    *          which will monitor for event id (int what)
    * @param int what
    *          Event Id on which client will be interested to get a
    *          unsolicited request
    * @param Object obj not used
    *
    */
    void initiateUnsolOemHookRaw(Handler h, int what, Object obj);

   /**
    * Releases the object created for unsolicited communication
    * The application which calls InitiateUnsolOemHookRaw should call
    * this function to release when application no longer need unsol-
    * cited communication from the native and the android RIL.
    *
    * @param Handler h
    *          MessageHandler which is part of client application
    *          which will monitor for event id (int what)
    *
    * @return void
    */
    public void releaseUnsolOemHookRaw(Handler h);

/**
* Get the current active Data Call list
*








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 7c2f2e0..180f390 100755

//Synthetic comment -- @@ -782,6 +782,42 @@
mCM.invokeOemRilRequestStrings(strings, response);
}

   /**
    * Initiates the unsolicited communication for oem ril. This is
    * specific to oems hook libraries to use to accomplish communication
    * from native ril to android ril and provide further communication to
    * java application layer.
    *
    * @param Handler h
    *          MessageHandler is part of client application,
    *          which will monitor for event id (int what)
    * @param int what
    *          Event Id on which client will be interested to get a
    *          unsolicited request
    * @param Object obj not used
    *
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
    * @param Handler h
    *          MessageHandler is part of client application,
    *          which will monitor for event id (int what)
    *
    * @return void
    */
    public void releaseUnsolOemHookRaw(Handler h) {
        mCM.unSetOnUnsolOemHookRaw(h);
    }

public void notifyDataActivity() {
mNotifier.notifyDataActivity(this);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneProxy.java b/src/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 77135d4..d129ec6 100644

//Synthetic comment -- @@ -677,6 +677,14 @@
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







