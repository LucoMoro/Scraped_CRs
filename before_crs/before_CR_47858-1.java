/*Use Rlog

Change-Id:I44919126606cd55f7dce7c942027ed0d914f04bd*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/CellIdentityCdma.java b/telephony/java/android/telephony/CellIdentityCdma.java
//Synthetic comment -- index 9579b91..6e2a70d 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
* CellIdentity is to represent a unique CDMA cell
//Synthetic comment -- @@ -219,6 +219,6 @@
* log
*/
private static void log(String s) {
        Log.w(LOG_TAG, s);
}
}








//Synthetic comment -- diff --git a/telephony/java/android/telephony/CellIdentityGsm.java b/telephony/java/android/telephony/CellIdentityGsm.java
//Synthetic comment -- index 21cb790..bda96be 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
* CellIdentity to represent a unique GSM or UMTS cell
//Synthetic comment -- @@ -204,6 +204,6 @@
* log
*/
private static void log(String s) {
        Log.w(LOG_TAG, s);
}
}








//Synthetic comment -- diff --git a/telephony/java/android/telephony/CellIdentityLte.java b/telephony/java/android/telephony/CellIdentityLte.java
//Synthetic comment -- index ad822bb..f72d583 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
* CellIdentity is to represent a unique LTE cell
//Synthetic comment -- @@ -199,6 +199,6 @@
* log
*/
private static void log(String s) {
        Log.w(LOG_TAG, s);
}
}








//Synthetic comment -- diff --git a/telephony/java/android/telephony/CellInfoCdma.java b/telephony/java/android/telephony/CellInfoCdma.java
//Synthetic comment -- index ea48e2e..a5d6e9c 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
* Immutable cell information from a point in time.
//Synthetic comment -- @@ -143,6 +143,6 @@
* log
*/
private static void log(String s) {
        Log.w(LOG_TAG, s);
}
}








//Synthetic comment -- diff --git a/telephony/java/android/telephony/CellInfoGsm.java b/telephony/java/android/telephony/CellInfoGsm.java
//Synthetic comment -- index bd14d45a..bf0eca8 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
* Immutable cell information from a point in time.
//Synthetic comment -- @@ -142,6 +142,6 @@
* log
*/
private static void log(String s) {
        Log.w(LOG_TAG, s);
}
}








//Synthetic comment -- diff --git a/telephony/java/android/telephony/CellInfoLte.java b/telephony/java/android/telephony/CellInfoLte.java
//Synthetic comment -- index 2f81b65..35dea24 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
* Immutable cell information from a point in time.
//Synthetic comment -- @@ -148,6 +148,6 @@
* log
*/
private static void log(String s) {
        Log.w(LOG_TAG, s);
}
}








//Synthetic comment -- diff --git a/telephony/java/android/telephony/CellSignalStrengthCdma.java b/telephony/java/android/telephony/CellSignalStrengthCdma.java
//Synthetic comment -- index 660326c..190fea2 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
* LTE signal strength related information.
//Synthetic comment -- @@ -376,6 +376,6 @@
* log
*/
private static void log(String s) {
        Log.w(LOG_TAG, s);
}
}








//Synthetic comment -- diff --git a/telephony/java/android/telephony/CellSignalStrengthGsm.java b/telephony/java/android/telephony/CellSignalStrengthGsm.java
//Synthetic comment -- index 4108f61..2c36344 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
* LTE signal strength related information.
//Synthetic comment -- @@ -229,6 +229,6 @@
* log
*/
private static void log(String s) {
        Log.w(LOG_TAG, s);
}
}








//Synthetic comment -- diff --git a/telephony/java/android/telephony/CellSignalStrengthLte.java b/telephony/java/android/telephony/CellSignalStrengthLte.java
//Synthetic comment -- index 925f4d4..55680c8 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
* LTE signal strength related information.
//Synthetic comment -- @@ -293,6 +293,6 @@
* log
*/
private static void log(String s) {
        Log.w(LOG_TAG, s);
}
}








//Synthetic comment -- diff --git a/telephony/java/android/telephony/PhoneNumberUtils.java b/telephony/java/android/telephony/PhoneNumberUtils.java
//Synthetic comment -- index f740718..8b85d8c 100644

//Synthetic comment -- @@ -33,7 +33,7 @@
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;

import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ISO_COUNTRY;
//Synthetic comment -- @@ -353,7 +353,7 @@
}

private static void log(String msg) {
        Log.d(LOG_TAG, msg);
}
/** index of the last character of the network portion
*  (eg anything after is a post-dial string)
//Synthetic comment -- @@ -1711,7 +1711,7 @@
return false;
}

        Log.d(LOG_TAG, "System property doesn't provide any emergency numbers."
+ " Use embedded logic for determining ones.");

// No ecclist system property, so use our own list.
//Synthetic comment -- @@ -1805,7 +1805,7 @@
} else {
Locale locale = context.getResources().getConfiguration().locale;
countryIso = locale.getCountry();
            Log.w(LOG_TAG, "No CountryDetector; falling back to countryIso based on locale: "
+ countryIso);
}
return isEmergencyNumberInternal(number, countryIso, useExactMatch);
//Synthetic comment -- @@ -2015,7 +2015,7 @@
// This should never happen since we checked the if dialStr is null
// and if it contains the plus sign in the beginning of this function.
// The plus sign is part of the network portion.
                        Log.e("checkAndProcessPlusCode: null newDialStr", networkDialStr);
return dialStr;
}
postDialStr = extractPostDialPortion(tempDialStr);
//Synthetic comment -- @@ -2035,7 +2035,7 @@
if (dialableIndex < 0) {
postDialStr = "";
}
                            Log.e("wrong postDialStr=", postDialStr);
}
}
if (DBG) log("checkAndProcessPlusCode,postDialStr=" + postDialStr);
//Synthetic comment -- @@ -2044,7 +2044,7 @@
// TODO: Support NANP international conversion and other telephone numbering plans.
// Currently the phone is never used in non-NANP system, so return the original
// dial string.
                Log.e("checkAndProcessPlusCode:non-NANP not supported", dialStr);
}
}
return retStr;
//Synthetic comment -- @@ -2103,7 +2103,7 @@
}
}
} else {
            Log.e("isNanp: null dialStr passed in", dialStr);
}
return retVal;
}
//Synthetic comment -- @@ -2119,7 +2119,7 @@
retVal = true;
}
} else {
            Log.e("isOneNanp: null dialStr passed in", dialStr);
}
return retVal;
}
//Synthetic comment -- @@ -2158,7 +2158,7 @@
delimiterIndex = number.indexOf("%40");
}
if (delimiterIndex < 0) {
            Log.w(LOG_TAG,
"getUsernameFromUriNumber: no delimiter found in SIP addr '" + number + "'");
delimiterIndex = number.length();
}








//Synthetic comment -- diff --git a/telephony/java/android/telephony/PhoneStateListener.java b/telephony/java/android/telephony/PhoneStateListener.java
//Synthetic comment -- index f3ccae6..ff77fc0 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
import android.telephony.SignalStrength;
import android.telephony.CellLocation;
import android.telephony.CellInfo;
import android.util.Log;

import com.android.internal.telephony.IPhoneStateListener;

//Synthetic comment -- @@ -348,7 +348,7 @@

Handler mHandler = new Handler() {
public void handleMessage(Message msg) {
            //Log.d("TelephonyRegistry", "what=0x" + Integer.toHexString(msg.what) + " msg=" + msg);
switch (msg.what) {
case LISTEN_SERVICE_STATE:
PhoneStateListener.this.onServiceStateChanged((ServiceState)msg.obj);








//Synthetic comment -- diff --git a/telephony/java/android/telephony/ServiceState.java b/telephony/java/android/telephony/ServiceState.java
//Synthetic comment -- index a9a5e90..dcb73dc 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
* Contains phone state and service related information.
//Synthetic comment -- @@ -460,7 +460,7 @@
break;
default:
rtString = "Unexpected";
                Log.w(LOG_TAG, "Unexpected radioTechnology=" + rt);
break;
}
return rtString;








//Synthetic comment -- diff --git a/telephony/java/android/telephony/SignalStrength.java b/telephony/java/android/telephony/SignalStrength.java
//Synthetic comment -- index f998935..e2da53e 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
* Contains phone signal strength related information.
//Synthetic comment -- @@ -919,6 +919,6 @@
* log
*/
private static void log(String s) {
        Log.w(LOG_TAG, s);
}
}








//Synthetic comment -- diff --git a/telephony/java/android/telephony/TelephonyManager.java b/telephony/java/android/telephony/TelephonyManager.java
//Synthetic comment -- index 2fa41e7..8b140c8 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;

import com.android.internal.telephony.IPhoneSubInfo;
import com.android.internal.telephony.ITelephony;
//Synthetic comment -- @@ -411,7 +411,7 @@
cmdline = new String(buffer, 0, count);
}
} catch (IOException e) {
            Log.d(TAG, "No /proc/cmdline exception=" + e);
} finally {
if (is != null) {
try {
//Synthetic comment -- @@ -420,7 +420,7 @@
}
}
}
        Log.d(TAG, "/proc/cmdline=" + cmdline);
return cmdline;
}

//Synthetic comment -- @@ -467,7 +467,7 @@
}
}

        Log.d(TAG, "getLteOnCdmaMode=" + retVal + " curVal=" + curVal +
" product_type='" + productType +
"' lteOnCdmaProductType='" + sLteOnCdmaProductType + "'");
return retVal;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CallerInfo.java b/telephony/java/com/android/internal/telephony/CallerInfo.java
//Synthetic comment -- index b1a5872..228a630 100644

//Synthetic comment -- @@ -29,6 +29,7 @@
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;
//Synthetic comment -- @@ -46,7 +47,7 @@
*/
public class CallerInfo {
private static final String TAG = "CallerInfo";
    private static final boolean VDBG = Log.isLoggable(TAG, Log.VERBOSE);

public static final String UNKNOWN_NUMBER = "-1";
public static final String PRIVATE_NUMBER = "-2";
//Synthetic comment -- @@ -164,7 +165,7 @@
info.isCachedPhotoCurrent = false;
info.contactExists = false;

        if (VDBG) Log.v(TAG, "getCallerInfo() based on cursor...");

if (cursor != null) {
if (cursor.moveToFirst()) {
//Synthetic comment -- @@ -209,10 +210,10 @@
columnIndex = getColumnIndexForPersonId(contactRef, cursor);
if (columnIndex != -1) {
info.person_id = cursor.getLong(columnIndex);
                    if (VDBG) Log.v(TAG, "==> got info.person_id: " + info.person_id);
} else {
// No valid columnIndex, so we can't look up person_id.
                    Log.w(TAG, "Couldn't find person_id column for " + contactRef);
// Watch out: this means that anything that depends on
// person_id will be broken (like contact photo lookups in
// the in-call UI, for example.)
//Synthetic comment -- @@ -269,7 +270,7 @@
* with all relevant fields empty or null.
*/
public static CallerInfo getCallerInfo(Context context, String number) {
        if (VDBG) Log.v(TAG, "getCallerInfo() based on number...");

if (TextUtils.isEmpty(number)) {
return null;
//Synthetic comment -- @@ -414,7 +415,7 @@
// permission to retrieve VM number and would not call
// this method.
// Leave phoneNumber untouched.
            Log.e(TAG, "Cannot access VoiceMail.", se);
}
// TODO: There is no voicemail picture?
// FIXME: FIND ANOTHER ICON
//Synthetic comment -- @@ -473,10 +474,10 @@
// So instead, figure out the column to use for person_id by just
// looking at the URI itself.

        if (VDBG) Log.v(TAG, "- getColumnIndexForPersonId: contactRef URI = '"
+ contactRef + "'...");
// Warning: Do not enable the following logging (due to ANR risk.)
        // if (VDBG) Log.v(TAG, "- MIME type: "
//                 + context.getContentResolver().getType(contactRef));

String url = contactRef.toString();
//Synthetic comment -- @@ -484,25 +485,25 @@
if (url.startsWith("content://com.android.contacts/data/phones")) {
// Direct lookup in the Phone table.
// MIME type: Phone.CONTENT_ITEM_TYPE (= "vnd.android.cursor.item/phone_v2")
            if (VDBG) Log.v(TAG, "'data/phones' URI; using RawContacts.CONTACT_ID");
columnName = RawContacts.CONTACT_ID;
} else if (url.startsWith("content://com.android.contacts/data")) {
// Direct lookup in the Data table.
// MIME type: Data.CONTENT_TYPE (= "vnd.android.cursor.dir/data")
            if (VDBG) Log.v(TAG, "'data' URI; using Data.CONTACT_ID");
// (Note Data.CONTACT_ID and RawContacts.CONTACT_ID are equivalent.)
columnName = Data.CONTACT_ID;
} else if (url.startsWith("content://com.android.contacts/phone_lookup")) {
// Lookup in the PhoneLookup table, which provides "fuzzy matching"
// for phone numbers.
// MIME type: PhoneLookup.CONTENT_TYPE (= "vnd.android.cursor.dir/phone_lookup")
            if (VDBG) Log.v(TAG, "'phone_lookup' URI; using PhoneLookup._ID");
columnName = PhoneLookup._ID;
} else {
            Log.w(TAG, "Unexpected prefix for contactRef '" + url + "'");
}
int columnIndex = (columnName != null) ? cursor.getColumnIndex(columnName) : -1;
        if (VDBG) Log.v(TAG, "==> Using column '" + columnName
+ "' (columnIndex = " + columnIndex + ") for person_id lookup...");
return columnIndex;
}
//Synthetic comment -- @@ -529,7 +530,7 @@
* @see com.android.i18n.phonenumbers.PhoneNumberOfflineGeocoder
*/
private static String getGeoDescription(Context context, String number) {
        if (VDBG) Log.v(TAG, "getGeoDescription('" + number + "')...");

if (TextUtils.isEmpty(number)) {
return null;
//Synthetic comment -- @@ -542,17 +543,17 @@
String countryIso = getCurrentCountryIso(context, locale);
PhoneNumber pn = null;
try {
            if (VDBG) Log.v(TAG, "parsing '" + number
+ "' for countryIso '" + countryIso + "'...");
pn = util.parse(number, countryIso);
            if (VDBG) Log.v(TAG, "- parsed number: " + pn);
} catch (NumberParseException e) {
            Log.w(TAG, "getGeoDescription: NumberParseException for incoming number '" + number + "'");
}

if (pn != null) {
String description = geocoder.getDescriptionForNumber(pn, locale);
            if (VDBG) Log.v(TAG, "- got description: '" + description + "'");
return description;
} else {
return null;
//Synthetic comment -- @@ -571,7 +572,7 @@
countryIso = detector.detectCountry().getCountryIso();
} else {
countryIso = locale.getCountry();
        Log.w(TAG, "No CountryDetector; falling back to countryIso based on locale: "
+ countryIso);
}
return countryIso;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/CallerInfoAsyncQuery.java b/telephony/java/com/android/internal/telephony/CallerInfoAsyncQuery.java
//Synthetic comment -- index 4912749..dd5f644 100644

//Synthetic comment -- @@ -31,7 +31,7 @@
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;

/**
* Helper class to make it easier to run asynchronous caller-id lookup queries.
//Synthetic comment -- @@ -137,13 +137,13 @@
// However, if there is any code that this Handler calls (such as in
// super.handleMessage) that DOES place unexpected messages on the
// queue, then we need pass these messages on.
                    if (DBG) Log.d(LOG_TAG, "Unexpected command (CookieWrapper is null): " + msg.what +
" ignored by CallerInfoWorkerHandler, passing onto parent.");

super.handleMessage(msg);
} else {

                    if (DBG) Log.d(LOG_TAG, "Processing event: " + cw.event + " token (arg1): " + msg.arg1 +
" command: " + msg.what + " query URI: " + sanitizeUriToString(args.uri));

switch (cw.event) {
//Synthetic comment -- @@ -199,7 +199,7 @@
*/
@Override
protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (DBG) Log.d(LOG_TAG, "##### onQueryComplete() #####   query complete for token: " + token);

//get the cookie and notify the listener.
CookieWrapper cw = (CookieWrapper) cookie;
//Synthetic comment -- @@ -208,7 +208,7 @@
// from within this code.
// However, if there is any code that calls this method, we should
// check the parameters to make sure they're viable.
                if (DBG) Log.d(LOG_TAG, "Cookie is null, ignoring onQueryComplete() request.");
return;
}

//Synthetic comment -- @@ -237,13 +237,13 @@
mCallerInfo = new CallerInfo().markAsVoiceMail();
} else {
mCallerInfo = CallerInfo.getCallerInfo(mQueryContext, mQueryUri, cursor);
                    if (DBG) Log.d(LOG_TAG, "==> Got mCallerInfo: " + mCallerInfo);

CallerInfo newCallerInfo = CallerInfo.doSecondaryLookupIfNecessary(
mQueryContext, cw.number, mCallerInfo);
if (newCallerInfo != mCallerInfo) {
mCallerInfo = newCallerInfo;
                        if (DBG) Log.d(LOG_TAG, "#####async contact look up with numeric username"
+ mCallerInfo);
}

//Synthetic comment -- @@ -279,7 +279,7 @@
}
}

                if (DBG) Log.d(LOG_TAG, "constructing CallerInfo object for token: " + token);

//notify that we can clean up the queue after this.
CookieWrapper endMarker = new CookieWrapper();
//Synthetic comment -- @@ -289,7 +289,7 @@

//notify the listener that the query is complete.
if (cw.listener != null) {
                if (DBG) Log.d(LOG_TAG, "notifying listener: " + cw.listener.getClass().toString() +
" for token: " + token + mCallerInfo);
cw.listener.onQueryComplete(token, cw.cookie, mCallerInfo);
}
//Synthetic comment -- @@ -312,7 +312,7 @@
CallerInfoAsyncQuery c = new CallerInfoAsyncQuery();
c.allocate(context, contactRef);

        if (DBG) Log.d(LOG_TAG, "starting query for URI: " + contactRef + " handler: " + c.toString());

//create cookieWrapper, start query
CookieWrapper cw = new CookieWrapper();
//Synthetic comment -- @@ -339,9 +339,9 @@
public static CallerInfoAsyncQuery startQuery(int token, Context context, String number,
OnQueryCompleteListener listener, Object cookie) {
if (DBG) {
            Log.d(LOG_TAG, "##### CallerInfoAsyncQuery startQuery()... #####");
            Log.d(LOG_TAG, "- number: " + /*number*/ "xxxxxxx");
            Log.d(LOG_TAG, "- cookie: " + cookie);
}

// Construct the URI object and query params, and start the query.
//Synthetic comment -- @@ -352,7 +352,7 @@

if (PhoneNumberUtils.isUriNumber(number)) {
// "number" is really a SIP address.
            if (DBG) Log.d(LOG_TAG, "  - Treating number as a SIP address: " + /*number*/ "xxxxxxx");

// We look up SIP addresses directly in the Data table:
contactRef = Data.CONTENT_URI;
//Synthetic comment -- @@ -384,11 +384,11 @@
}

if (DBG) {
            Log.d(LOG_TAG, "==> contactRef: " + sanitizeUriToString(contactRef));
            Log.d(LOG_TAG, "==> selection: " + selection);
if (selectionArgs != null) {
for (int i = 0; i < selectionArgs.length; i++) {
                    Log.d(LOG_TAG, "==> selectionArgs[" + i + "]: " + selectionArgs[i]);
}
}
}
//Synthetic comment -- @@ -426,7 +426,7 @@
*/
public void addQueryListener(int token, OnQueryCompleteListener listener, Object cookie) {

        if (DBG) Log.d(LOG_TAG, "adding listener to query: " + sanitizeUriToString(mHandler.mQueryUri) +
" handler: " + mHandler.toString());

//create cookieWrapper, add query request to end of queue.








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/GsmAlphabet.java b/telephony/java/com/android/internal/telephony/GsmAlphabet.java
//Synthetic comment -- index 04b1220..19047c8 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
import android.text.TextUtils;
import android.util.SparseIntArray;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
//Synthetic comment -- @@ -477,11 +477,11 @@
StringBuilder ret = new StringBuilder(lengthSeptets);

if (languageTable < 0 || languageTable > sLanguageTables.length) {
            Log.w(TAG, "unknown language table " + languageTable + ", using default");
languageTable = 0;
}
if (shiftTable < 0 || shiftTable > sLanguageShiftTables.length) {
            Log.w(TAG, "unknown single shift table " + shiftTable + ", using default");
shiftTable = 0;
}

//Synthetic comment -- @@ -491,11 +491,11 @@
String shiftTableToChar = sLanguageShiftTables[shiftTable];

if (languageTableToChar.isEmpty()) {
                Log.w(TAG, "no language table for code " + languageTable + ", using default");
languageTableToChar = sLanguageTables[0];
}
if (shiftTableToChar.isEmpty()) {
                Log.w(TAG, "no single shift table for code " + shiftTable + ", using default");
shiftTableToChar = sLanguageShiftTables[0];
}

//Synthetic comment -- @@ -535,7 +535,7 @@
}
}
} catch (RuntimeException ex) {
            Log.e(TAG, "Error GSM 7 bit packed: ", ex);
return null;
}

//Synthetic comment -- @@ -767,7 +767,7 @@
for (int i = 0; i < sz; i++) {
char c = s.charAt(i);
if (c == GSM_EXTENDED_ESCAPE) {
                Log.w(TAG, "countGsmSeptets() string contains Escape character, skipping.");
continue;
}
if (charToLanguageTable.get(c, -1) != -1) {
//Synthetic comment -- @@ -843,7 +843,7 @@
for (int i = 0; i < sz && !lpcList.isEmpty(); i++) {
char c = s.charAt(i);
if (c == GSM_EXTENDED_ESCAPE) {
                Log.w(TAG, "countGsmSeptets() string contains Escape character, ignoring!");
continue;
}
// iterate through enabled locking shift tables
//Synthetic comment -- @@ -1415,7 +1415,7 @@
int numTables = sLanguageTables.length;
int numShiftTables = sLanguageShiftTables.length;
if (numTables != numShiftTables) {
            Log.e(TAG, "Error: language tables array length " + numTables +
" != shift tables array length " + numShiftTables);
}

//Synthetic comment -- @@ -1432,7 +1432,7 @@

int tableLen = table.length();
if (tableLen != 0 && tableLen != 128) {
                Log.e(TAG, "Error: language tables index " + i +
" length " + tableLen + " (expected 128 or 0)");
}

//Synthetic comment -- @@ -1450,7 +1450,7 @@

int shiftTableLen = shiftTable.length();
if (shiftTableLen != 0 && shiftTableLen != 128) {
                Log.e(TAG, "Error: language shift tables index " + i +
" length " + shiftTableLen + " (expected 128 or 0)");
}








