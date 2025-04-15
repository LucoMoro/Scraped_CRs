/*Fix potential NPE when saving fragment state.

If a fragment's saved view state is null and the user
visible hint is true then the `result` bundle will have
never been initialized to a value resulting in a
`NullPointerException`.

Change-Id:I8ba585bc6b9298841490d64bc22a8219cd261adb*/
//Synthetic comment -- diff --git a/v4/java/android/support/v4/app/FragmentManager.java b/v4/java/android/support/v4/app/FragmentManager.java
//Synthetic comment -- index b4f47e2..d68e31a 100644

//Synthetic comment -- @@ -1572,6 +1572,9 @@
FragmentManagerImpl.VIEW_STATE_TAG, f.mSavedViewState);
}
if (!f.mUserVisibleHint) {
// Only add this if it's not the default value
result.putBoolean(FragmentManagerImpl.USER_VISIBLE_HINT_TAG, f.mUserVisibleHint);
}







