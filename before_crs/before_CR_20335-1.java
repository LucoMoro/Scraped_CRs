/*Huawei fix for call function tests failing on Mobile Internet Devices, Personal Media Players and other non-phone devices

Change-Id:I372d1e6a566d16f01a12be4b2954bc6f955e43f7*/
//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java b/tests/tests/content/src/android/content/cts/AvailableIntentsTest.java
//Synthetic comment -- index b08e953..ffdb40c7 100644

//Synthetic comment -- @@ -21,10 +21,12 @@
import dalvik.annotation.TestTargetNew;

import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.test.AndroidTestCase;

import java.util.List;
//Synthetic comment -- @@ -34,6 +36,14 @@
private static final String NORMAL_URL = "http://www.google.com/";
private static final String SECURE_URL = "https://www.google.com/";

/**
* Assert target intent can be handled by at least one Activity.
* @param intent - the Intent will be handled.
//Synthetic comment -- @@ -147,6 +157,12 @@
args = {java.lang.String.class, android.net.Uri.class}
)
public void testDialPhoneNumber() {
Uri uri = Uri.parse("tel:(212)5551212");
Intent intent = new Intent(Intent.ACTION_DIAL, uri);
assertCanBeHandled(intent);
//Synthetic comment -- @@ -161,6 +177,12 @@
args = {java.lang.String.class, android.net.Uri.class}
)
public void testDialVoicemail() {
Uri uri = Uri.parse("voicemail:");
Intent intent = new Intent(Intent.ACTION_DIAL, uri);
assertCanBeHandled(intent);







