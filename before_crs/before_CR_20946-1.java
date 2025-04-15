/*CTS : remove testCallVoicemail

Comment from btmura : Test is invalid, because the CDD doesn't say
what is required in terms of permissions by the applications that
respond to these intents. Also the Dialer isn't part of the core
systems application list defined under the CDD's 3.2.3.1. Finally,
it appears that Intent.ACTION_CALL_PRIVILEGED is a private API as
well.

Change-Id:I9dc661d8f35b0960e79061232014eee51e54410b*/
//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java b/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java
//Synthetic comment -- index 88d5f1c..21a66c4 100644

//Synthetic comment -- @@ -54,28 +54,6 @@
*   {@link android.Manifest.permission#CALL_PRIVILEGED}.
*/
@SmallTest
    public void testCallVoicemail() {
        PackageManager packageManager = getContext().getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
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
        }
     }

    /**
     * Verify that Intent.ACTION_CALL_PRIVILEGED requires permissions.
     * <p>Requires Permission:
     *   {@link android.Manifest.permission#CALL_PRIVILEGED}.
     */
    @SmallTest
public void testCall911() {
PackageManager packageManager = getContext().getPackageManager();
if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {







