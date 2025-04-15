/*CTS : remove testCallVoicemail

Comment from btmura : Test is invalid, because the CDD doesn't say
what is required in terms of permissions by the applications that
respond to these intents. Also the Dialer isn't part of the core
systems application list defined under the CDD's 3.2.3.1. Finally,
it appears that Intent.ACTION_CALL_PRIVILEGED is a private API as
well.

byhttps://review.source.android.com//#change,20946Change-Id:I916f67bf6723a88fea6353aa6fa386afeaa433ba*/




//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java b/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java
deleted file mode 100644
//Synthetic comment -- index 88d5f1c..0000000

//Synthetic comment -- @@ -1,96 +0,0 @@







