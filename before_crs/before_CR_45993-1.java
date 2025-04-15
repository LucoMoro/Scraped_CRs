/*Fix switching running/cached apps with memory bar

Fixes issue #38735.

Invalidate the owner fragment options menu when changing mode from within
RunningProcessesView.

Change-Id:I85af7a953a1f4f570902f0959ee284f208871bce*/
//Synthetic comment -- diff --git a/src/com/android/settings/applications/ManageApplications.java b/src/com/android/settings/applications/ManageApplications.java
//Synthetic comment -- index 1cc9dcc..dfc1a9c 100644

//Synthetic comment -- @@ -1047,6 +1047,7 @@
mOptionsMenu.findItem(SHOW_RUNNING_SERVICES).setVisible(showingBackground);
mOptionsMenu.findItem(SHOW_BACKGROUND_PROCESSES).setVisible(!showingBackground);
mOptionsMenu.findItem(RESET_APP_PREFERENCES).setVisible(false);
} else {
mOptionsMenu.findItem(SORT_ORDER_ALPHA).setVisible(mSortOrder != SORT_ORDER_ALPHA);
mOptionsMenu.findItem(SORT_ORDER_SIZE).setVisible(mSortOrder != SORT_ORDER_SIZE);








//Synthetic comment -- diff --git a/src/com/android/settings/applications/RunningProcessesView.java b/src/com/android/settings/applications/RunningProcessesView.java
//Synthetic comment -- index 7c3ebb0..bae29c8 100644

//Synthetic comment -- @@ -413,6 +413,9 @@
@Override
public void onClick(View v) {
mAdapter.setShowBackground(true);
}
});
mForegroundProcessText = (TextView)findViewById(R.id.foregroundText);
//Synthetic comment -- @@ -420,6 +423,9 @@
@Override
public void onClick(View v) {
mAdapter.setShowBackground(false);
}
});








