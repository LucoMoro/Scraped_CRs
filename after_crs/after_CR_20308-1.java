/*Fix Build - DO NOT MERGE

Change-Id:If7519440a39abf10657f6ce35faccb219f4ce055*/




//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/SystemFeaturesTest.java b/tests/tests/app/src/android/app/cts/SystemFeaturesTest.java
//Synthetic comment -- index 1f7c9f8..d4fabac 100644

//Synthetic comment -- @@ -196,7 +196,7 @@
}

public void testNfcFeatures() {
        if (NfcAdapter.getDefaultAdapter() != null) {
assertAvailable(PackageManager.FEATURE_NFC);
} else {
assertNotAvailable(PackageManager.FEATURE_NFC);







