/*Set the user-visible hint on fragments in FragmentStatePagerAdapter

Unlike FragmentPagerAdapter, FragmentStatePagerAdapter did not set
the user-visible hint on fragments when they were paged. This
commit fixes the inconsistency.

Change-Id:I8098279ee398dfcd37f999e67688d7cb591f9d65Signed-off-by: Veeti Paananen <veeti.paananen@rojekti.fi>*/




//Synthetic comment -- diff --git a/v13/java/android/support/v13/app/FragmentStatePagerAdapter.java b/v13/java/android/support/v13/app/FragmentStatePagerAdapter.java
//Synthetic comment -- index c547608..1339318 100644

//Synthetic comment -- @@ -118,6 +118,7 @@
mFragments.add(null);
}
FragmentCompat.setMenuVisibility(fragment, false);
        FragmentCompat.setUserVisibleHint(fragment, false);
mFragments.set(position, fragment);
mCurTransaction.add(container.getId(), fragment);

//Synthetic comment -- @@ -148,9 +149,11 @@
if (fragment != mCurrentPrimaryItem) {
if (mCurrentPrimaryItem != null) {
FragmentCompat.setMenuVisibility(mCurrentPrimaryItem, false);
                FragmentCompat.setUserVisibleHint(mCurrentPrimaryItem, false);
}
if (fragment != null) {
FragmentCompat.setMenuVisibility(fragment, true);
                FragmentCompat.setUserVisibleHint(fragment, true);
}
mCurrentPrimaryItem = fragment;
}








//Synthetic comment -- diff --git a/v4/java/android/support/v4/app/FragmentStatePagerAdapter.java b/v4/java/android/support/v4/app/FragmentStatePagerAdapter.java
//Synthetic comment -- index b82e548..0fd1d7f 100644

//Synthetic comment -- @@ -114,6 +114,7 @@
mFragments.add(null);
}
fragment.setMenuVisibility(false);
        fragment.setUserVisibleHint(false);
mFragments.set(position, fragment);
mCurTransaction.add(container.getId(), fragment);

//Synthetic comment -- @@ -144,9 +145,11 @@
if (fragment != mCurrentPrimaryItem) {
if (mCurrentPrimaryItem != null) {
mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
}
if (fragment != null) {
fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
}
mCurrentPrimaryItem = fragment;
}







