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
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
//Synthetic comment -- @@ -62,6 +63,7 @@

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.lang.ref.WeakReference;

/**
//Synthetic comment -- @@ -124,6 +126,7 @@
public int type;
public String label;
public String number;
        public String formattedNumber;

public static ContactInfo EMPTY = new ContactInfo();
}
//Synthetic comment -- @@ -144,6 +147,23 @@
int numberType;
String numberLabel;
}
    
    /**
     * Shared builder used by {@link #formatPhoneNumber(String)} to minimize
     * allocations when formatting phone numbers.
     */
    private static final SpannableStringBuilder sEditable = new SpannableStringBuilder();
    
    /**
     * Invalid formatting type constant for {@link #sFormattingType}.
     */
    private static final int FORMATTING_TYPE_INVALID = -1;
    
    /**
     * Cached formatting type for current {@link Locale}, as provided by
     * {@link PhoneNumberUtils#getFormatTypeForLocale(Locale)}.
     */
    private static int sFormattingType = FORMATTING_TYPE_INVALID;

/** Adapter class to fill in data for the Call Log */
final class RecentCallsAdapter extends ResourceCursorAdapter
//Synthetic comment -- @@ -300,6 +320,10 @@
info.type = phonesCursor.getInt(PHONE_TYPE_COLUMN_INDEX);
info.label = phonesCursor.getString(LABEL_COLUMN_INDEX);
info.number = phonesCursor.getString(MATCHED_NUMBER_COLUMN_INDEX);
                        
                        // New incoming phone number invalidates our formatted
                        // cache. Any cache fills happen only on the GUI thread.
                        info.formattedNumber = null;

mContactInfo.put(ciq.number, info);
// Inform list to update this item, if in view
//Synthetic comment -- @@ -366,6 +390,7 @@
final RecentCallsListItemViews views = (RecentCallsListItemViews) view.getTag();

String number = c.getString(NUMBER_COLUMN_INDEX);
            String formattedNumber = null;
String callerName = c.getString(CALLER_NAME_COLUMN_INDEX);
int callerNumberType = c.getInt(CALLER_NUMBERTYPE_COLUMN_INDEX);
String callerNumberLabel = c.getString(CALLER_NUMBERLABEL_COLUMN_INDEX);
//Synthetic comment -- @@ -393,6 +418,12 @@
enqueueRequest(number, c.getPosition(),
callerName, callerNumberType, callerNumberLabel);
}
                
                // Format and cache phone number for found contact
                if (info.formattedNumber == null) {
                    info.formattedNumber = formatPhoneNumber(info.number);
                }
                formattedNumber = info.formattedNumber;
}

String name = info.name;
//Synthetic comment -- @@ -405,6 +436,9 @@
name = callerName;
ntype = callerNumberType;
label = callerNumberLabel;
                
                // Format the cached call_log phone number
                formattedNumber = formatPhoneNumber(number);
}
// Set the text lines
if (!TextUtils.isEmpty(name)) {
//Synthetic comment -- @@ -413,7 +447,7 @@
CharSequence numberLabel = Phones.getDisplayLabel(context, ntype, label,
mLabelArray);
views.numberView.setVisibility(View.VISIBLE);
                views.numberView.setText(formattedNumber);
if (!TextUtils.isEmpty(numberLabel)) {
views.labelView.setText(numberLabel);
views.labelView.setVisibility(View.VISIBLE);
//Synthetic comment -- @@ -430,8 +464,8 @@
} else if (number.equals(mVoiceMailNumber)) {
number = getString(R.string.voicemail);
} else {
                    // Just a raw number, and no cache, so format it nicely
                    number = formatPhoneNumber(number);
}

views.line1View.setText(number);
//Synthetic comment -- @@ -510,6 +544,9 @@
mVoiceMailNumber = ((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE))
.getVoiceMailNumber();
mQueryHandler = new QueryHandler(this);
        
        // Reset locale-based formatting cache
        sFormattingType = FORMATTING_TYPE_INVALID;
}

@Override
//Synthetic comment -- @@ -568,6 +605,28 @@
}
}

    /**
     * Format the given phone number using
     * {@link PhoneNumberUtils#formatNumber(android.text.Editable, int)}. This
     * helper method uses {@link #sEditable} and {@link #sFormattingType} to
     * prevent allocations between multiple calls.
     * <p>
     * Because of the shared {@link #sEditable} builder, <b>this method is not
     * thread safe</b>, and should only be called from the GUI thread.
     */
    private String formatPhoneNumber(String number) {
        // Cache formatting type if not already present
        if (sFormattingType == FORMATTING_TYPE_INVALID) {
            sFormattingType = PhoneNumberUtils.getFormatTypeForLocale(Locale.getDefault());
        }
        
        sEditable.clear();
        sEditable.append(number);
        
        PhoneNumberUtils.formatNumber(sEditable, sFormattingType);
        return sEditable.toString();
    }

private void resetNewCallsFlag() {
// Mark all "new" missed calls as not new anymore
StringBuilder where = new StringBuilder("type=");







