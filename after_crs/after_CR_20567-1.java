/*fix NoCallPermission tests for non-telephony devices

non-telephony devices are not expected to honour tel:url
requests, and hence the tests for such URL are by default
passed in device which launch telephony feature.

Change-Id:I2531adfc15065971fa240a49311f19fd2972b856*/




//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java b/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java
//Synthetic comment -- index 3b0573b..0813aaa 100644

//Synthetic comment -- @@ -34,14 +34,20 @@
*/
@SmallTest
public void testActionCall() {
        PackageManager packageManager = getContext().getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            Uri uri = Uri.parse("tel:123456");
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                mContext.startActivity(intent);
                fail("startActivity(Intent.ACTION_CALL) did not throw SecurityException as expected");
            } catch (SecurityException e) {
                // expected
            }
        } else {
            // dummy return for non-telephony devices
            return;
}
}

//Synthetic comment -- @@ -52,17 +58,23 @@
*/
@SmallTest
public void testCallVoicemail() {
        PackageManager packageManager = getContext().getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            try {
                //Intent intent = new Intent(Intent.ACTION_CALL_PRIVILEGED,
                Intent intent = new Intent("android.intent.action.CALL_PRIVILEGED",
                        Uri.fromParts("voicemail", "", null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                fail("startActivity(Intent.ACTION_CALL_PRIVILEGED) did not throw SecurityException as expected");
            } catch (SecurityException e) {
                // expected
            }
        } else {
            // dummy return for non-telephony devices
            return;
}
     }

/**
* Verify that Intent.ACTION_CALL_PRIVILEGED requires permissions.







