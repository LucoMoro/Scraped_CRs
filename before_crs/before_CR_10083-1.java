/*AI 143722: Correctly format phone numbers in call log, instead of leaving raw numbers.
  Caches Editable and Locale values to minimize allocations, and caches formatted values along with other ContactInfo details.  We need to be extremely careful with this change, as it impacts a performance-sensitive codepath: scrolling through the call log.
  BUG=1741249

Automated import of CL 143722*/
//Synthetic comment -- diff --git a/src/com/android/contacts/RecentCallsListActivity.java b/src/com/android/contacts/RecentCallsListActivity.java
//Synthetic comment -- index 8949f6e..6abaf23 100644

//Synthetic comment -- @@ -39,6 +39,7 @@
import android.provider.Contacts.Intents.Insert;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
//Synthetic comment -- @@ -62,6 +63,7 @@

import java.util.HashMap;
import java.util.LinkedList;
import java.lang.ref.WeakReference;

/**
//Synthetic comment -- @@ -124,6 +126,7 @@
public int type;
public String label;
public String number;

public static ContactInfo EMPTY = new ContactInfo();
}
//Synthetic comment -- @@ -144,6 +147,23 @@
int numberType;
String numberLabel;
}

/** Adapter class to fill in data for the Call Log */
final class RecentCallsAdapter extends ResourceCursorAdapter
//Synthetic comment -- @@ -300,6 +320,10 @@
info.type = phonesCursor.getInt(PHONE_TYPE_COLUMN_INDEX);
info.label = phonesCursor.getString(LABEL_COLUMN_INDEX);
info.number = phonesCursor.getString(MATCHED_NUMBER_COLUMN_INDEX);

mContactInfo.put(ciq.number, info);
// Inform list to update this item, if in view
//Synthetic comment -- @@ -366,6 +390,7 @@
final RecentCallsListItemViews views = (RecentCallsListItemViews) view.getTag();

String number = c.getString(NUMBER_COLUMN_INDEX);
String callerName = c.getString(CALLER_NAME_COLUMN_INDEX);
int callerNumberType = c.getInt(CALLER_NUMBERTYPE_COLUMN_INDEX);
String callerNumberLabel = c.getString(CALLER_NUMBERLABEL_COLUMN_INDEX);
//Synthetic comment -- @@ -393,6 +418,12 @@
enqueueRequest(number, c.getPosition(),
callerName, callerNumberType, callerNumberLabel);
}
}

String name = info.name;
//Synthetic comment -- @@ -405,6 +436,9 @@
name = callerName;
ntype = callerNumberType;
label = callerNumberLabel;
}
// Set the text lines
if (!TextUtils.isEmpty(name)) {
//Synthetic comment -- @@ -413,7 +447,7 @@
CharSequence numberLabel = Phones.getDisplayLabel(context, ntype, label,
mLabelArray);
views.numberView.setVisibility(View.VISIBLE);
                views.numberView.setText(number);
if (!TextUtils.isEmpty(numberLabel)) {
views.labelView.setText(numberLabel);
views.labelView.setVisibility(View.VISIBLE);
//Synthetic comment -- @@ -430,8 +464,8 @@
} else if (number.equals(mVoiceMailNumber)) {
number = getString(R.string.voicemail);
} else {
                    // Just a raw number, format it to look pretty
                    number = PhoneNumberUtils.formatNumber(number);
}

views.line1View.setText(number);
//Synthetic comment -- @@ -510,6 +544,9 @@
mVoiceMailNumber = ((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE))
.getVoiceMailNumber();
mQueryHandler = new QueryHandler(this);
}

@Override
//Synthetic comment -- @@ -568,6 +605,28 @@
}
}

private void resetNewCallsFlag() {
// Mark all "new" missed calls as not new anymore
StringBuilder where = new StringBuilder("type=");







