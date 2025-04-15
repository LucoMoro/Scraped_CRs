/*Only display the Badge on Contacts, if there is data to display.
Just tap the Android symbol / contact Picture of a Contact with
no Phone number or Email. A empty Badge will open.
This fix prevents the Badge from opening, and immediately
finishes the calling Badge-Activity if there is no data to display
To test:
Create an empty Contact (only with a name), and click the Android symbol
on the Contact.
old Version: Empty Badge
new Version: no Badge

Change-Id:I8991a7db40b6df92d7f8ac43dfd23652f6ff23f5*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ui/QuickContactActivity.java b/src/com/android/contacts/ui/QuickContactActivity.java
//Synthetic comment -- index b5e445f..bac4229 100644

//Synthetic comment -- @@ -69,7 +69,7 @@
final int mode = extras.getInt(QuickContact.EXTRA_MODE, QuickContact.MODE_MEDIUM);
final String[] excludeMimes = extras.getStringArray(QuickContact.EXTRA_EXCLUDE_MIMES);

        mQuickContact.show(lookupUri, target, mode, excludeMimes);
}

private Rect getTargetRect(Intent intent) {








//Synthetic comment -- diff --git a/src/com/android/contacts/ui/QuickContactWindow.java b/src/com/android/contacts/ui/QuickContactWindow.java
//Synthetic comment -- index 132b18c..90117c0 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import com.android.internal.policy.PolicyManager;
import com.google.android.collect.Sets;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
//Synthetic comment -- @@ -157,6 +158,8 @@
private int mWindowRecycled = 0;
private int mActionRecycled = 0;

/**
* Set of {@link Action} that are associated with the aggregate currently
* displayed by this dialog, represented as a map from {@link String}
//Synthetic comment -- @@ -293,7 +296,7 @@
* Start showing a dialog for the given {@link Contacts#_ID} pointing
* towards the given location.
*/
    public synchronized void show(Uri lookupUri, Rect anchor, int mode, String[] excludeMimes) {
if (mQuerying || mShowing) {
Log.w(TAG, "dismissing before showing");
dismissInternal();
//Synthetic comment -- @@ -303,6 +306,9 @@
android.os.Debug.startMethodTracing(TRACE_TAG);
}

// Prepare header view for requested mode
mLookupUri = lookupUri;
mAnchor = new Rect(anchor);
//Synthetic comment -- @@ -485,6 +491,7 @@
}
mShowing = false;
mDismissed = true;

// Cancel any pending queries
mHandler.cancelOperation(TOKEN_DATA);
//Synthetic comment -- @@ -527,7 +534,14 @@
* {@link #showInternal()} when all data items are present.
*/
private void considerShowing() {
        if (mHasData && !mShowing && !mDismissed) {
if (mMode == QuickContact.MODE_MEDIUM && !mHasValidSocial) {
// Missing valid social, swap medium for small header
mHeader.setVisibility(View.GONE);
//Synthetic comment -- @@ -552,7 +566,6 @@
}

handleData(cursor);
        mHasData = true;

if (!cursor.isClosed()) {
cursor.close();
//Synthetic comment -- @@ -1158,6 +1171,7 @@
final Set<String> containedTypes = mActions.keySet();
for (String mimeType : ORDERED_MIMETYPES) {
if (containedTypes.contains(mimeType)) {
final int index = mTrack.getChildCount() - 1;
mTrack.addView(inflateAction(mimeType), index);
containedTypes.remove(mimeType);
//Synthetic comment -- @@ -1167,6 +1181,7 @@
// Then continue with remaining MIME-types in alphabetical order
final String[] remainingTypes = containedTypes.toArray(new String[containedTypes.size()]);
Arrays.sort(remainingTypes);
for (String mimeType : remainingTypes) {
final int index = mTrack.getChildCount() - 1;
mTrack.addView(inflateAction(mimeType), index);







