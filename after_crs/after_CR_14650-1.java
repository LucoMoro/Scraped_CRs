/*Increased SMS to MMS threshold. Most of the mobiles support 7 pages for sms. SMS is cheap compare to MMS.

Change-Id:I3ca9f62241919666a0863c9138365179fe2b4d1d*/




//Synthetic comment -- diff --git a/src/com/android/mms/MmsConfig.java b/src/com/android/mms/MmsConfig.java
//Synthetic comment -- index 93a9f8b..7d0bc8a 100644

//Synthetic comment -- @@ -58,7 +58,7 @@
private static int mDefaultMMSMessagesPerThread = 20;       // default value
private static int mMinMessageCountPerThread = 2;           // default value
private static int mMaxMessageCountPerThread = 5000;        // default value
    private static int mSmsToMmsTextThreshold = 7;              // default value
private static int mHttpSocketTimeout = 60*1000;            // default to 1 min
private static int mMinimumSlideElementDuration = 7;        // default to 7 sec
private static boolean mNotifyWapMMSC = false;







