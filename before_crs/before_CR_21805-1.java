/*Code Cleanup

Basically fix as many warnings in Eclipse as possible.

- Remove unread variables, unnecessary calls/imports
- Try not to use deprecated APIs

Change-Id:I193c225cb006afae2c4608d4dbc581f3f8389c90*/
//Synthetic comment -- diff --git a/src/com/android/contacts/AttachImage.java b/src/com/android/contacts/AttachImage.java
//Synthetic comment -- index 599ab4f..1b65c9d 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.contacts;

import com.google.android.collect.Maps;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
//Synthetic comment -- @@ -25,7 +23,6 @@
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
//Synthetic comment -- @@ -41,7 +38,6 @@

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
* Provides an external interface for other applications to attach images
//Synthetic comment -- @@ -157,9 +153,6 @@
// attach the photo to every raw contact
for (Long rawContactId : mRawContactIds) {

                        // exchange and google only allow one image, so do an update rather than insert
                        boolean shouldUpdate = false;

final Uri rawContactUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI,
rawContactId);
final Uri rawContactDataUri = Uri.withAppendedPath(rawContactUri,








//Synthetic comment -- diff --git a/src/com/android/contacts/CallDetailActivity.java b/src/com/android/contacts/CallDetailActivity.java
//Synthetic comment -- index 9f027bb..c5d096d 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
*/
public class CallDetailActivity extends ListActivity implements
AdapterView.OnItemClickListener {
    private static final String TAG = "CallDetail";

private TextView mCallType;
private ImageView mCallTypeIcon;
//Synthetic comment -- @@ -359,7 +359,7 @@
}
}

    public void onItemClick(AdapterView parent, View view, int position, long id) {
// Handle passing action off to correct handler.
if (view.getTag() instanceof ViewEntry) {
ViewEntry entry = (ViewEntry) view.getTag();








//Synthetic comment -- diff --git a/src/com/android/contacts/Collapser.java b/src/com/android/contacts/Collapser.java
//Synthetic comment -- index 3872dfd..7dd6b2f 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.contacts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;









//Synthetic comment -- diff --git a/src/com/android/contacts/ContactEntryAdapter.java b/src/com/android/contacts/ContactEntryAdapter.java
//Synthetic comment -- index 34ee505..5a7aeff 100644

//Synthetic comment -- @@ -19,8 +19,6 @@
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;








//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsListActivity.java b/src/com/android/contacts/ContactsListActivity.java
//Synthetic comment -- index ac6a3a8..dc27526 100644

//Synthetic comment -- @@ -415,7 +415,6 @@

private QueryHandler mQueryHandler;
private boolean mJustCreated;
    private boolean mSyncEnabled;
Uri mSelectedContactUri;

//    private boolean mDisplayAll;
//Synthetic comment -- @@ -835,8 +834,6 @@

mQueryHandler = new QueryHandler(this);
mJustCreated = true;

        mSyncEnabled = true;
}

/**
//Synthetic comment -- @@ -2979,13 +2976,10 @@
throw new IllegalStateException("couldn't move cursor to position " + position);
}

            boolean newView;
View v;
if (convertView == null || convertView.getTag() == null) {
                newView = true;
v = newView(mContext, cursor, parent);
} else {
                newView = false;
v = convertView;
}
bindView(v, mContext, cursor);
//Synthetic comment -- @@ -3058,7 +3052,6 @@
int typeColumnIndex;
int dataColumnIndex;
int labelColumnIndex;
            int defaultType;
int nameColumnIndex;
int phoneticNameColumnIndex;
boolean displayAdditionalData = mDisplayAdditionalData;
//Synthetic comment -- @@ -3072,7 +3065,6 @@
dataColumnIndex = PHONE_NUMBER_COLUMN_INDEX;
typeColumnIndex = PHONE_TYPE_COLUMN_INDEX;
labelColumnIndex = PHONE_LABEL_COLUMN_INDEX;
                    defaultType = Phone.TYPE_HOME;
break;
}
case MODE_PICK_POSTAL:
//Synthetic comment -- @@ -3082,7 +3074,6 @@
dataColumnIndex = POSTAL_ADDRESS_COLUMN_INDEX;
typeColumnIndex = POSTAL_TYPE_COLUMN_INDEX;
labelColumnIndex = POSTAL_LABEL_COLUMN_INDEX;
                    defaultType = StructuredPostal.TYPE_HOME;
break;
}
default: {
//Synthetic comment -- @@ -3096,7 +3087,6 @@
dataColumnIndex = -1;
typeColumnIndex = -1;
labelColumnIndex = -1;
                    defaultType = Phone.TYPE_HOME;
displayAdditionalData = false;
highlightingEnabled = mHighlightWhenScrolling && mMode != MODE_STREQUENT;
}
//Synthetic comment -- @@ -3154,7 +3144,6 @@
viewToUse = view.getPhotoView();
}

                final int position = cursor.getPosition();
mPhotoLoader.loadPhoto(viewToUse, photoId);
}

//Synthetic comment -- @@ -3305,8 +3294,7 @@

// Get the split between starred and frequent items, if the mode is strequent
mFrequentSeparatorPos = ListView.INVALID_POSITION;
            int cursorCount = 0;
            if (cursor != null && (cursorCount = cursor.getCount()) > 0
&& mMode == MODE_STREQUENT) {
cursor.move(-1);
for (int i = 0; cursor.moveToNext(); i++) {








//Synthetic comment -- diff --git a/src/com/android/contacts/ContactsUtils.java b/src/com/android/contacts/ContactsUtils.java
//Synthetic comment -- index 94dabba..cc893c0 100644

//Synthetic comment -- @@ -50,7 +50,7 @@
import java.util.ArrayList;

public class ContactsUtils {
    private static final String TAG = "ContactsUtils";
private static final String WAIT_SYMBOL_AS_STRING = String.valueOf(PhoneNumberUtils.WAIT);
/**
* Build the display title for the {@link Data#CONTENT_URI} entry in the








//Synthetic comment -- diff --git a/src/com/android/contacts/ExportVCardActivity.java b/src/com/android/contacts/ExportVCardActivity.java
//Synthetic comment -- index ceeecfc..255e8e2 100644

//Synthetic comment -- @@ -56,7 +56,6 @@
private int mFileIndexMinimum;
private int mFileIndexMaximum;
private String mFileNameExtension;
    private String mVCardTypeStr;
private Set<String> mExtensionsToConsider;

private ProgressDialog mProgressDialog;
//Synthetic comment -- @@ -86,9 +85,6 @@

private class ErrorReasonDisplayer implements Runnable {
private final int mResId;
        public ErrorReasonDisplayer(int resId) {
            mResId = resId;
        }
public ErrorReasonDisplayer(String errorReason) {
mResId = R.id.dialog_fail_to_export_with_reason;
mErrorReason = errorReason;
//Synthetic comment -- @@ -245,7 +241,6 @@
mFileNamePrefix = getString(R.string.config_export_file_prefix);
mFileNameSuffix = getString(R.string.config_export_file_suffix);
mFileNameExtension = getString(R.string.config_export_file_extension);
        mVCardTypeStr = getString(R.string.config_export_vcard_type);

mExtensionsToConsider = new HashSet<String>();
mExtensionsToConsider.add(mFileNameExtension);
//Synthetic comment -- @@ -269,7 +264,7 @@
}

@Override
    protected Dialog onCreateDialog(int id) {
switch (id) {
case R.id.dialog_export_confirmation: {
return getExportConfirmationDialog();
//Synthetic comment -- @@ -308,18 +303,18 @@
return mProgressDialog;
}
}
        return super.onCreateDialog(id);
}

@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
if (id == R.id.dialog_fail_to_export_with_reason) {
((AlertDialog)dialog).setMessage(getErrorReason());
} else if (id == R.id.dialog_export_confirmation) {
((AlertDialog)dialog).setMessage(
getString(R.string.confirm_export_message, mTargetFileName));
} else {
            super.onPrepareDialog(id, dialog);
}
}









//Synthetic comment -- diff --git a/src/com/android/contacts/ImportVCardActivity.java b/src/com/android/contacts/ImportVCardActivity.java
//Synthetic comment -- index 46cdf32..15b9daf 100644

//Synthetic comment -- @@ -108,10 +108,6 @@
private static final String LOG_TAG = "ImportVCardActivity";
private static final boolean DO_PERFORMANCE_PROFILE = false;

    private final static int VCARD_VERSION_V21 = 1;
    private final static int VCARD_VERSION_V30 = 2;
    private final static int VCARD_VERSION_V40 = 3;

// Run on the UI thread. Must not be null except after onDestroy().
private Handler mHandler = new Handler();

//Synthetic comment -- @@ -232,7 +228,6 @@
} catch (VCardNestedException e) {
try {
final int estimatedVCardType = detector.getEstimatedType();
                            final String estimatedCharset = detector.getEstimatedCharset();
// Assume that VCardSourceDetector was able to detect the source.
// Try again with the detector.
result = readOneVCardFile(targetUri, estimatedVCardType,
//Synthetic comment -- @@ -256,7 +251,6 @@
getString(R.string.reading_vcard_contacts));
mProgressDialogForReadVCard.setIndeterminate(false);
mProgressDialogForReadVCard.setMax(counter.getCount());
                    String charset = detector.getEstimatedCharset();
createdUri = doActuallyReadOneVCard(targetUri, mAccount, true, detector,
mErrorFileNameList);
} else {  // Read multiple files.
//Synthetic comment -- @@ -280,7 +274,6 @@
} catch (VCardNestedException e) {
// Assume that VCardSourceDetector was able to detect the source.
}
                        String charset = detector.getEstimatedCharset();
doActuallyReadOneVCard(targetUri, mAccount,
false, detector, mErrorFileNameList);
mProgressDialogForReadVCard.incrementProgressBy(1);
//Synthetic comment -- @@ -344,7 +337,6 @@
context.getString(R.string.config_import_vcard_type));
}
final String estimatedCharset = detector.getEstimatedCharset();
            final String currentLanguage = Locale.getDefault().getLanguage();
VCardEntryConstructor builder;
builder = new VCardEntryConstructor(vcardType, mAccount, estimatedCharset);
final VCardEntryCommitter committer = new VCardEntryCommitter(mResolver);
//Synthetic comment -- @@ -562,6 +554,7 @@
private PowerManager.WakeLock mWakeLock;

private class CanceledException extends Exception {
}

public VCardScanThread(File sdcardDirectory) {
//Synthetic comment -- @@ -603,7 +596,6 @@
finish();
} else {
int size = mAllVCardFileList.size();
                final Context context = ImportVCardActivity.this;
if (size == 0) {
runOnUIThread(new DialogDisplayer(R.id.dialog_vcard_not_found));
} else {
//Synthetic comment -- @@ -813,7 +805,7 @@
}

@Override
    protected Dialog onCreateDialog(int resId) {
switch (resId) {
case R.string.import_from_sdcard: {
if (mAccountSelectionListener == null) {
//Synthetic comment -- @@ -903,7 +895,7 @@
}
}

        return super.onCreateDialog(resId);
}

@Override







