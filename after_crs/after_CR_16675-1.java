/*Fix bug causing the user to be incorrectly prompted to apply security settings.

Exchange 2007 (by default) requires a minimum password length of 4, this policy
is still pushed to the device even if the password policy is disabled
alltogether (mPasswordMode=0).

Change-Id:I127f860b2c8ed75bb35df1b4f6005acf526fe2d4*/




//Synthetic comment -- diff --git a/src/com/android/email/SecurityPolicy.java b/src/com/android/email/SecurityPolicy.java
//Synthetic comment -- index 5dc9c14..028e731 100644

//Synthetic comment -- @@ -259,7 +259,7 @@
DevicePolicyManager dpm = getDPM();
if (dpm.isAdminActive(mAdminName)) {
// check each policy explicitly
            if (policies.mMinPasswordLength > 0 && policies.mPasswordMode > 0) {
if (dpm.getPasswordMinimumLength(mAdminName) < policies.mMinPasswordLength) {
return false;
}







