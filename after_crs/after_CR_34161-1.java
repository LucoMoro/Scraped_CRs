/*Fix CTS issue.

If the device has no telephony feature, this case will fail. It doesn't check if the device has such feature or not.
the fix is to check the feature first.*/




//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java b/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java
//Synthetic comment -- index 5df3739..a06b603 100755

//Synthetic comment -- @@ -102,9 +102,12 @@
* Test ACTION_CALL when uri is a phone number, it will call the entered phone number.
*/
public void testCallPhoneNumber() {
        PackageManager packageManager = mContext.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            Uri uri = Uri.parse("tel:2125551212");
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            assertCanBeHandled(intent);
        }
}

/**







