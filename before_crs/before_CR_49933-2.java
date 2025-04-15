/*Check if the Activity is finishing before showDialog()

Check if the Activity is going through its finishing phase before
displaying the Dialog, because Showing dialog once the activity
is no longer in context will cause BadTokenException.

Change-Id:I58940e694f61cd79536abe2df60a0b745a7b806f*/
//Synthetic comment -- diff --git a/src/com/android/settings/accounts/AccountSyncSettings.java b/src/com/android/settings/accounts/AccountSyncSettings.java
//Synthetic comment -- index 196908e..e1ff398 100644

//Synthetic comment -- @@ -112,7 +112,8 @@
} catch (AuthenticatorException e) {
// handled below
}
                                if (failed) {
showDialog(FAILED_REMOVAL_DIALOG);
} else {
finish();







