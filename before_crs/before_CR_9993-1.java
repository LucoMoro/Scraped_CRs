/*- Tweak TYPE_ALL to TYPE_ALL_MEDIA, because of forced deprecation.*/
//Synthetic comment -- diff --git a/tests/FrameworkTest/tests/src/com/android/frameworktest/settings/RingtonePickerActivityTest.java b/tests/FrameworkTest/tests/src/com/android/frameworktest/settings/RingtonePickerActivityTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 42888ff..f9ccb504

//Synthetic comment -- @@ -61,7 +61,7 @@
}

public void testDefault() {
        mActivity.launchRingtonePickerActivity(true, null, RingtoneManager.TYPE_ALL);
mInstrumentation.waitForIdleSync();

// Go to top
//Synthetic comment -- @@ -81,7 +81,7 @@
}

public void testFirst() {
        mActivity.launchRingtonePickerActivity(true, null, RingtoneManager.TYPE_ALL);
mInstrumentation.waitForIdleSync();

// Go to top
//Synthetic comment -- @@ -107,7 +107,7 @@
testFirst();
Uri firstUri = mActivity.pickedUri;

        mActivity.launchRingtonePickerActivity(true, firstUri, RingtoneManager.TYPE_ALL);
mInstrumentation.waitForIdleSync();

//// Hit cancel:
//Synthetic comment -- @@ -129,7 +129,7 @@
testFirst();
Uri firstUri = mActivity.pickedUri;

        mActivity.launchRingtonePickerActivity(true, firstUri, RingtoneManager.TYPE_ALL);
mInstrumentation.waitForIdleSync();

//// Pick second:
//Synthetic comment -- @@ -152,7 +152,7 @@
}

public void testCancel() {
        mActivity.launchRingtonePickerActivity(true, null, RingtoneManager.TYPE_ALL);
mInstrumentation.waitForIdleSync();

// Go to bottom
//Synthetic comment -- @@ -167,7 +167,7 @@
}

public void testNoDefault() {
        mActivity.launchRingtonePickerActivity(false, null, RingtoneManager.TYPE_ALL);
mInstrumentation.waitForIdleSync();

// Go to top







