/*Changing MMS video default size

change the maximum video attachment support for MMS from
300k up to 950k

Change-Id:Idbf780c5342f87493c8d52c24b1657ebc5eb057e*/
//Synthetic comment -- diff --git a/src/com/android/mms/MmsConfig.java b/src/com/android/mms/MmsConfig.java
//Synthetic comment -- index ca5c9fa..8390a75 100644

//Synthetic comment -- @@ -42,7 +42,7 @@
*/
private static boolean mTransIdEnabled = false;
private static int mMmsEnabled = 1;                         // default to true
    private static int mMaxMessageSize = 300 * 1024;            // default to 300k max size
private static String mUserAgent = DEFAULT_USER_AGENT;
private static String mUaProfTagName = DEFAULT_HTTP_KEY_X_WAP_PROFILE;
private static String mUaProfUrl = null;







