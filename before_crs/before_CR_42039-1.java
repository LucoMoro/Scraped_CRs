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
* Registers the handler for CDMA number information record
* Unlike the register* methods, there's only one notification handler
*








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/Phone.java b/src/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 34aa96c..2a902fb 100644

//Synthetic comment -- @@ -1156,6 +1156,37 @@
*/
void invokeOemRilRequestStrings(String[] strings, Message response);

/**
* Get the current active Data Call list
*








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneBase.java b/src/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index 7c2f2e0..180f390 100755

//Synthetic comment -- @@ -782,6 +782,42 @@
mCM.invokeOemRilRequestStrings(strings, response);
}

public void notifyDataActivity() {
mNotifier.notifyDataActivity(this);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneProxy.java b/src/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 77135d4..d129ec6 100644

//Synthetic comment -- @@ -677,6 +677,14 @@
mActivePhone.invokeOemRilRequestStrings(strings, response);
}

public void getDataCallList(Message response) {
mActivePhone.getDataCallList(response);
}







