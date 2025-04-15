/*getParcelableArray* behaviour is sometimes unpredictable

in some case when the data in a savedInstance the Parcelable arrays stored by the application are not unparcel'ed correctly because the ClassLoader doesn't seem to be correct.
The first getSparseParcelableArray() in the code is preceded by setClassLoader() by not the second one. The problem disappears when setClassLoader() is called in both cases.

Change-Id:I2d2f42c285d9130fd543c0154e9d210d430823e3*/




//Synthetic comment -- diff --git a/v4/java/android/support/v4/app/FragmentManager.java b/v4/java/android/support/v4/app/FragmentManager.java
//Synthetic comment -- index d8bed64..21b4c45 100644

//Synthetic comment -- @@ -803,6 +803,7 @@
case Fragment.INITIALIZING:
if (DEBUG) Log.v(TAG, "moveto CREATED: " + f);
if (f.mSavedFragmentState != null) {
                        f.mSavedFragmentState.setClassLoader(mActivity.getClassLoader());
f.mSavedViewState = f.mSavedFragmentState.getSparseParcelableArray(
FragmentManagerImpl.VIEW_STATE_TAG);
f.mTarget = getFragment(f.mSavedFragmentState,







