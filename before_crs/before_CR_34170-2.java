/*Handle devices with no telephony.

If the device has no telephony feature, this case will fail.
It doesn't check if the device has such feature or not.
The fix is to check the feature first.

Change-Id:I06b1d3fcfde3e471bce3eb4571bcb693424e9345*/
//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java b/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java
//Synthetic comment -- index 5df3739..a06b603 100755

//Synthetic comment -- @@ -102,9 +102,12 @@
* Test ACTION_CALL when uri is a phone number, it will call the entered phone number.
*/
public void testCallPhoneNumber() {
        Uri uri = Uri.parse("tel:2125551212");
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        assertCanBeHandled(intent);
}

/**







