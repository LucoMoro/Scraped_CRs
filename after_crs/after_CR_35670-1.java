/*Fix potential NPE when saving fragment state.

If a fragment's saved view state is null and the user
visible hint is true then the `result` bundle will have
never been initialized to a value resulting in a
`NullPointerException`.

Mirrors support library changeI8ba585bc6b9298841490d64bc22a8219cd261adb.

Change-Id:Iabd5ac293d2ece3771da9ef257479eca0dcd523c*/




//Synthetic comment -- diff --git a/core/java/android/app/FragmentManager.java b/core/java/android/app/FragmentManager.java
//Synthetic comment -- index 1abb7de..0519d3e 100644

//Synthetic comment -- @@ -1538,6 +1538,9 @@
FragmentManagerImpl.VIEW_STATE_TAG, f.mSavedViewState);
}
if (!f.mUserVisibleHint) {
            if (result == null) {
                result = new Bundle();
            }
// Only add this if it's not the default value
result.putBoolean(FragmentManagerImpl.USER_VISIBLE_HINT_TAG, f.mUserVisibleHint);
}







