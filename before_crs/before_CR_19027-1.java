/*code cleanup.
    - unused import statements
    - unused local variables
    - unused private methods and variabls

Change-Id:I882d43b9c9ce7f0517ead70f4c966b4274b2edd0*/
//Synthetic comment -- diff --git a/src/com/android/contacts/AttachImage.java b/src/com/android/contacts/AttachImage.java
//Synthetic comment -- index 6970842..c63e02f 100644

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
//Synthetic comment -- index 14f54c9..5eca329 100644

//Synthetic comment -- @@ -357,7 +357,7 @@
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
//Synthetic comment -- index 0d2c7eb..4883108 100644

//Synthetic comment -- @@ -27,7 +27,6 @@

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
//Synthetic comment -- @@ -415,7 +414,6 @@

private QueryHandler mQueryHandler;
private boolean mJustCreated;
    private boolean mSyncEnabled;
Uri mSelectedContactUri;

//    private boolean mDisplayAll;
//Synthetic comment -- @@ -835,8 +833,6 @@

mQueryHandler = new QueryHandler(this);
mJustCreated = true;

        mSyncEnabled = true;
}

/**
//Synthetic comment -- @@ -1036,8 +1032,6 @@
registerProviderStatusObserver();
mPhotoLoader.resume();

        Activity parent = getParent();

// Do this before setting the filter. The filter thread relies
// on some state that is initialized in setDefaultMode
if (mMode == MODE_DEFAULT) {
//Synthetic comment -- @@ -2961,13 +2955,10 @@
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
//Synthetic comment -- @@ -3040,7 +3031,6 @@
int typeColumnIndex;
int dataColumnIndex;
int labelColumnIndex;
            int defaultType;
int nameColumnIndex;
int phoneticNameColumnIndex;
boolean displayAdditionalData = mDisplayAdditionalData;
//Synthetic comment -- @@ -3054,7 +3044,6 @@
dataColumnIndex = PHONE_NUMBER_COLUMN_INDEX;
typeColumnIndex = PHONE_TYPE_COLUMN_INDEX;
labelColumnIndex = PHONE_LABEL_COLUMN_INDEX;
                    defaultType = Phone.TYPE_HOME;
break;
}
case MODE_PICK_POSTAL:
//Synthetic comment -- @@ -3064,7 +3053,6 @@
dataColumnIndex = POSTAL_ADDRESS_COLUMN_INDEX;
typeColumnIndex = POSTAL_TYPE_COLUMN_INDEX;
labelColumnIndex = POSTAL_LABEL_COLUMN_INDEX;
                    defaultType = StructuredPostal.TYPE_HOME;
break;
}
default: {
//Synthetic comment -- @@ -3078,7 +3066,6 @@
dataColumnIndex = -1;
typeColumnIndex = -1;
labelColumnIndex = -1;
                    defaultType = Phone.TYPE_HOME;
displayAdditionalData = false;
highlightingEnabled = mHighlightWhenScrolling && mMode != MODE_STREQUENT;
}
//Synthetic comment -- @@ -3135,7 +3122,6 @@
viewToUse = view.getPhotoView();
}

                final int position = cursor.getPosition();
mPhotoLoader.loadPhoto(viewToUse, photoId);
}

//Synthetic comment -- @@ -3255,7 +3241,6 @@

private void bindSectionHeader(View itemView, int position, boolean displaySectionHeaders) {
final ContactListItemView view = (ContactListItemView)itemView;
            final ContactListItemCache cache = (ContactListItemCache) view.getTag();
if (!displaySectionHeaders) {
view.setSectionHeader(null);
view.setDividerVisible(true);
//Synthetic comment -- @@ -3286,8 +3271,7 @@

// Get the split between starred and frequent items, if the mode is strequent
mFrequentSeparatorPos = ListView.INVALID_POSITION;
            int cursorCount = 0;
            if (cursor != null && (cursorCount = cursor.getCount()) > 0
&& mMode == MODE_STREQUENT) {
cursor.move(-1);
for (int i = 0; cursor.moveToNext(); i++) {








//Synthetic comment -- diff --git a/src/com/android/contacts/ExportVCardActivity.java b/src/com/android/contacts/ExportVCardActivity.java
//Synthetic comment -- index 5bccc7a..553cb24 100644

//Synthetic comment -- @@ -85,9 +85,6 @@

private class ErrorReasonDisplayer implements Runnable {
private final int mResId;
        public ErrorReasonDisplayer(int resId) {
            mResId = resId;
        }
public ErrorReasonDisplayer(String errorReason) {
mResId = R.id.dialog_fail_to_export_with_reason;
mErrorReason = errorReason;








//Synthetic comment -- diff --git a/src/com/android/contacts/ImportVCardActivity.java b/src/com/android/contacts/ImportVCardActivity.java
//Synthetic comment -- index 0a324fe..3aeacd6 100644

//Synthetic comment -- @@ -66,7 +66,6 @@
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;

//Synthetic comment -- @@ -331,7 +330,6 @@
VCardSourceDetector detector, List<String> errorFileNameList) {
final Context context = ImportVCardActivity.this;
VCardEntryConstructor builder;
            final String currentLanguage = Locale.getDefault().getLanguage();
int vcardType = VCardConfig.getVCardTypeFromString(
context.getString(R.string.config_import_vcard_type));
if (charset != null) {
//Synthetic comment -- @@ -585,7 +583,6 @@
finish();
} else {
int size = mAllVCardFileList.size();
                final Context context = ImportVCardActivity.this;
if (size == 0) {
runOnUIThread(new DialogDisplayer(R.id.dialog_vcard_not_found));
} else {
//Synthetic comment -- @@ -920,7 +917,7 @@
int attempts = 0;
while (mVCardReadThread.isAlive() && attempts < 10) {
try {
                    Thread.currentThread().sleep(20);
} catch (InterruptedException ie) {
// Keep on going until max attempts is reached.
}








//Synthetic comment -- diff --git a/src/com/android/contacts/PinnedHeaderListView.java b/src/com/android/contacts/PinnedHeaderListView.java
//Synthetic comment -- index 9d1391b..a2dc6ec 100644

//Synthetic comment -- @@ -151,7 +151,6 @@
case PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP: {
View firstView = getChildAt(0);
int bottom = firstView.getBottom();
                int itemHeight = firstView.getHeight();
int headerHeight = mHeaderView.getHeight();
int y;
int alpha;








//Synthetic comment -- diff --git a/src/com/android/contacts/SplitAggregateView.java b/src/com/android/contacts/SplitAggregateView.java
//Synthetic comment -- index b85a4ab..fb3c87c 100644

//Synthetic comment -- @@ -20,9 +20,7 @@
import com.android.contacts.model.Sources;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract.RawContacts;
//Synthetic comment -- @@ -31,7 +29,6 @@
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;








//Synthetic comment -- diff --git a/src/com/android/contacts/TabStripView.java b/src/com/android/contacts/TabStripView.java
//Synthetic comment -- index 9b875d1..791a6e3 100644

//Synthetic comment -- @@ -18,12 +18,9 @@

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;








//Synthetic comment -- diff --git a/src/com/android/contacts/TwelveKeyDialer.java b/src/com/android/contacts/TwelveKeyDialer.java
//Synthetic comment -- index 07927de..af81053 100644

//Synthetic comment -- @@ -996,7 +996,7 @@
/**
* Handle clicks from the dialpad chooser.
*/
    public void onItemClick(AdapterView parent, View v, int position, long id) {
DialpadChooserAdapter.ChoiceItem item =
(DialpadChooserAdapter.ChoiceItem) parent.getItemAtPosition(position);
int itemId = item.id;








//Synthetic comment -- diff --git a/src/com/android/contacts/ViewContactActivity.java b/src/com/android/contacts/ViewContactActivity.java
//Synthetic comment -- index ead6a4a..0332682 100644

//Synthetic comment -- @@ -814,7 +814,7 @@
return super.onKeyDown(keyCode, event);
}

    public void onItemClick(AdapterView parent, View v, int position, long id) {
ViewEntry entry = ViewAdapter.getEntry(mSections, position, SHOW_SEPARATORS);
if (entry != null) {
Intent intent = entry.intent;








//Synthetic comment -- diff --git a/src/com/android/contacts/model/EntityDelta.java b/src/com/android/contacts/model/EntityDelta.java
//Synthetic comment -- index cdf2e41..a8f2c4a 100644

//Synthetic comment -- @@ -29,12 +29,10 @@
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;








//Synthetic comment -- diff --git a/src/com/android/contacts/model/EntitySet.java b/src/com/android/contacts/model/EntitySet.java
//Synthetic comment -- index 830f8da..0fafa9f 100644

//Synthetic comment -- @@ -40,7 +40,9 @@
* and applying another {@link EntitySet} over it.
*/
public class EntitySet extends ArrayList<EntityDelta> implements Parcelable {
    private boolean mSplitRawContacts;

private EntitySet() {
}








//Synthetic comment -- diff --git a/src/com/android/contacts/model/Sources.java b/src/com/android/contacts/model/Sources.java
//Synthetic comment -- index be3f17d..979285b 100644

//Synthetic comment -- @@ -42,7 +42,6 @@
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

/**
* Singleton holder for all parsed {@link ContactsSource} available on the








//Synthetic comment -- diff --git a/src/com/android/contacts/ui/ContactsPreferencesActivity.java b/src/com/android/contacts/ui/ContactsPreferencesActivity.java
//Synthetic comment -- index 5a89745..e508f33 100644

//Synthetic comment -- @@ -656,8 +656,6 @@
private Sources mSources;
private AccountSet mAccounts;

        private boolean mChildWithPhones = false;

public DisplayAdapter(Context context) {
mContext = context;
mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//Synthetic comment -- @@ -674,7 +672,6 @@
* numbers, in addition to the total contacts.
*/
public void setChildDescripWithPhones(boolean withPhones) {
            mChildWithPhones = withPhones;
}

/** {@inheritDoc} */
//Synthetic comment -- @@ -688,7 +685,6 @@
final TextView text2 = (TextView)convertView.findViewById(android.R.id.text2);
final CheckBox checkbox = (CheckBox)convertView.findViewById(android.R.id.checkbox);

            final AccountDisplay account = mAccounts.get(groupPosition);
final GroupDelta child = (GroupDelta)this.getChild(groupPosition, childPosition);
if (child != null) {
// Handle normal group, with title and checkbox
//Synthetic comment -- @@ -860,7 +856,6 @@
int childPosition, long id) {
final CheckBox checkbox = (CheckBox)view.findViewById(android.R.id.checkbox);

        final AccountDisplay account = (AccountDisplay)mAdapter.getGroup(groupPosition);
final GroupDelta child = (GroupDelta)mAdapter.getChild(groupPosition, childPosition);
if (child != null) {
checkbox.toggle();
//Synthetic comment -- @@ -1027,7 +1022,6 @@
@Override
protected Void doInBackground(Activity target, AccountSet... params) {
final Context context = target;
            final ContentValues values = new ContentValues();
final ContentResolver resolver = context.getContentResolver();

try {








//Synthetic comment -- diff --git a/src/com/android/contacts/ui/EditContactActivity.java b/src/com/android/contacts/ui/EditContactActivity.java
//Synthetic comment -- index 3e248ea..da9518b 100644

//Synthetic comment -- @@ -787,7 +787,6 @@
private class DeleteClickListener implements DialogInterface.OnClickListener {

public void onClick(DialogInterface dialog, int which) {
            Sources sources = Sources.getInstance(EditContactActivity.this);
// Mark all raw contacts for deletion
for (EntityDelta delta : mState) {
delta.markDeleted();








//Synthetic comment -- diff --git a/src/com/android/contacts/ui/QuickContactWindow.java b/src/com/android/contacts/ui/QuickContactWindow.java
//Synthetic comment -- index 20d5bfd..b10db2b 100644

//Synthetic comment -- @@ -50,7 +50,6 @@
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.QuickContact;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.StatusUpdates;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Phone;
//Synthetic comment -- @@ -654,32 +653,6 @@
}
}

    /**
     * Find the QuickContact-specific presence icon for showing in chiclets.
     */
    private Drawable getTrackPresenceIcon(int status) {
        int resId;
        switch (status) {
            case StatusUpdates.AVAILABLE:
                resId = R.drawable.quickcontact_slider_presence_active;
                break;
            case StatusUpdates.IDLE:
            case StatusUpdates.AWAY:
                resId = R.drawable.quickcontact_slider_presence_away;
                break;
            case StatusUpdates.DO_NOT_DISTURB:
                resId = R.drawable.quickcontact_slider_presence_busy;
                break;
            case StatusUpdates.INVISIBLE:
                resId = R.drawable.quickcontact_slider_presence_inactive;
                break;
            case StatusUpdates.OFFLINE:
            default:
                resId = R.drawable.quickcontact_slider_presence_inactive;
        }
        return mContext.getResources().getDrawable(resId);
    }

/** Read {@link String} from the given {@link Cursor}. */
private static String getAsString(Cursor cursor, String columnName) {
final int index = cursor.getColumnIndex(columnName);








//Synthetic comment -- diff --git a/src/com/android/contacts/ui/ShowOrCreateActivity.java b/src/com/android/contacts/ui/ShowOrCreateActivity.java
//Synthetic comment -- index e0781d2..1c53a07 100755

//Synthetic comment -- @@ -28,7 +28,6 @@
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.EntityIterator;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;








//Synthetic comment -- diff --git a/src/com/android/contacts/ui/widget/ContactEditorView.java b/src/com/android/contacts/ui/widget/ContactEditorView.java
//Synthetic comment -- index 83bf2fb..4067c08 100644

//Synthetic comment -- @@ -74,8 +74,6 @@
private Drawable mSecondaryOpen;
private Drawable mSecondaryClosed;

    private View mHeaderColorBar;
    private View mSideBar;
private ImageView mHeaderIcon;
private TextView mHeaderAccountType;
private TextView mHeaderAccountName;
//Synthetic comment -- @@ -114,8 +112,6 @@
mGeneral = (ViewGroup)findViewById(R.id.sect_general);
mSecondary = (ViewGroup)findViewById(R.id.sect_secondary);

        mHeaderColorBar = findViewById(R.id.header_color_bar);
        mSideBar = findViewById(R.id.color_bar);
mHeaderIcon = (ImageView) findViewById(R.id.header_icon);
mHeaderAccountType = (TextView) findViewById(R.id.header_account_type);
mHeaderAccountName = (TextView) findViewById(R.id.header_account_name);
//Synthetic comment -- @@ -287,17 +283,6 @@
super.writeToParcel(out, flags);
out.writeInt(mSecondaryVisible ? 1 : 0);
}

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
}

/**








//Synthetic comment -- diff --git a/src/com/android/contacts/ui/widget/GenericEditorView.java b/src/com/android/contacts/ui/widget/GenericEditorView.java
//Synthetic comment -- index 24262bb..9e738e5 100644

//Synthetic comment -- @@ -422,17 +422,6 @@
out.writeInt(mVisibilities.length);
out.writeIntArray(mVisibilities);
}

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
}

/**








//Synthetic comment -- diff --git a/src/com/android/contacts/ui/widget/ReadOnlyContactEditorView.java b/src/com/android/contacts/ui/widget/ReadOnlyContactEditorView.java
//Synthetic comment -- index 4635f6a..202e807 100644

//Synthetic comment -- @@ -21,15 +21,11 @@
import com.android.contacts.model.EntityDelta;
import com.android.contacts.model.EntityModifier;
import com.android.contacts.model.ContactsSource.DataKind;
import com.android.contacts.model.ContactsSource.EditType;
import com.android.contacts.model.Editor.EditorListener;
import com.android.contacts.model.EntityDelta.ValuesDelta;
import com.android.contacts.ui.ViewIdGenerator;

import android.content.Context;
import android.content.Entity;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
//Synthetic comment -- @@ -56,8 +52,6 @@
private TextView mReadOnlyWarning;
private ViewGroup mGeneral;

    private View mHeaderColorBar;
    private View mSideBar;
private ImageView mHeaderIcon;
private TextView mHeaderAccountType;
private TextView mHeaderAccountName;
//Synthetic comment -- @@ -87,8 +81,6 @@
mReadOnlyWarning = (TextView) findViewById(R.id.read_only_warning);
mGeneral = (ViewGroup)findViewById(R.id.sect_general);

        mHeaderColorBar = findViewById(R.id.header_color_bar);
        mSideBar = findViewById(R.id.color_bar);
mHeaderIcon = (ImageView) findViewById(R.id.header_icon);
mHeaderAccountType = (TextView) findViewById(R.id.header_account_type);
mHeaderAccountName = (TextView) findViewById(R.id.header_account_name);








//Synthetic comment -- diff --git a/src/com/android/contacts/util/DataStatus.java b/src/com/android/contacts/util/DataStatus.java
//Synthetic comment -- index 88c6594..c4c3553 100644

//Synthetic comment -- @@ -139,10 +139,6 @@
return cursor.getString(cursor.getColumnIndex(columnName));
}

    private static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

private static int getInt(Cursor cursor, String columnName, int missingValue) {
final int columnIndex = cursor.getColumnIndex(columnName);
return cursor.isNull(columnIndex) ? missingValue : cursor.getInt(columnIndex);







