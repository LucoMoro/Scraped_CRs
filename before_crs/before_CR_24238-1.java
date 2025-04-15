/*Fix PolicySerializationTest

...that boolean should not be true!

Change-Id:I7dbd1f0a6b1de7acac3f27b3bc7d8a07e8e11527*/
//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/admin/PolicySerializationTestActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/admin/PolicySerializationTestActivity.java
//Synthetic comment -- index 1d8056f..49cc7fe 100644

//Synthetic comment -- @@ -106,7 +106,7 @@
private void loadPolicy() {
mAdapter.clear();
SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        if (prefs.getBoolean(LOAD_EXPECTED_POLICY_PREFERENCE, true)) {
for (PolicyItem<?> item : mPolicyItems) {
item.loadExpectedValue(prefs);
item.loadActualValue(mDevicePolicyManager, mAdmin);







