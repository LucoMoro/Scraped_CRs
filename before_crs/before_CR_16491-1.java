/*Check CtsVerifier Feature Count

Add a quick test that will warn us when the features go out of sync.

Change-Id:Ieda635fa46e417365cf64e1a735bbac8fb80f4be*/
//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/features/FeatureSummaryActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/features/FeatureSummaryActivity.java
//Synthetic comment -- index d448616..5da8130 100644

//Synthetic comment -- @@ -63,7 +63,7 @@
/**
* Constructor does not include 'present' because that's a detected
* value, and not set during creation.
         * 
* @param name value for this.name
* @param required value for this.required
*/








//Synthetic comment -- diff --git a/apps/CtsVerifier/tests/src/com/android/cts/verifier/features/FeatureSummaryActivityTest.java b/apps/CtsVerifier/tests/src/com/android/cts/verifier/features/FeatureSummaryActivityTest.java
new file mode 100644
//Synthetic comment -- index 0000000..43f5da4

//Synthetic comment -- @@ -0,0 +1,55 @@







