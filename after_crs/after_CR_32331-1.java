/*Added support for Filter parameter for PBAP function PullPhoneBook

VW Premium car kit sometimes behaves strangely if it receives entries with unexpected fields.
E.g. it does not like mail addresses with additional attributes.

Change-Id:I6d3145d802ad87e5fb47217a213f9bcb12670f60Signed-off-by: Martin Gerczuk <admin@android-rsap.com>*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java b/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java
//Synthetic comment -- index 6002263..e167663 100755

//Synthetic comment -- @@ -449,6 +449,8 @@

public boolean vcard21;

        public long filter;

public AppParamValue() {
maxListCount = 0xFFFF;
listStartOffset = 0;
//Synthetic comment -- @@ -457,12 +459,23 @@
order = "";
needTag = 0x00;
vcard21 = true;
            filter = 0L;
}

        public long getActualFilter() {
        	// in spec it says "All attributes of the vCard shall be returned
        	// if this header is not specified or carries the value 0x00000000"
        	// but that causes problems with some VW car kits!
//        	if (filter == 0L)
//        		return -1L;
        	
        	long mandatory = BluetoothPbapVcardComposer.FILTER_VERSION |
        	                 BluetoothPbapVcardComposer.FILTER_N |
        	                 BluetoothPbapVcardComposer.FILTER_TEL;
        	if (!vcard21)
        		mandatory |= BluetoothPbapVcardComposer.FILTER_FN;
        	
        	return filter | mandatory;
}
}

//Synthetic comment -- @@ -475,11 +488,21 @@
switch (appParam[i]) {
case ApplicationParameter.TRIPLET_TAGID.FILTER_TAGID:
i += 2; // length and tag field in triplet
                    appParamValue.filter = ((appParam[i+0] << 56) & 0xFF00000000000000L) |
                                           ((appParam[i+1] << 48) & 0x00FF000000000000L) |
                                           ((appParam[i+2] << 40) & 0x0000FF0000000000L) |
                                           ((appParam[i+3] << 32) & 0x000000FF00000000L) |
                                           ((appParam[i+4] << 24) & 0x00000000FF000000L) |
                                           ((appParam[i+5] << 16) & 0x0000000000FF0000L) |
                                           ((appParam[i+6] <<  8) & 0x000000000000FF00L) |
                                           ((appParam[i+7] <<  0) & 0x00000000000000FFL);
                    if (D) Log.i(TAG, "AppParam filter=" + appParamValue.filter);
i += ApplicationParameter.TRIPLET_LENGTH.FILTER_LENGTH;
break;
case ApplicationParameter.TRIPLET_TAGID.ORDER_TAGID:
i += 2; // length and tag field in triplet
appParamValue.order = Byte.toString(appParam[i]);
                    if (D) Log.i(TAG, "AppParam order=" + appParamValue.order);
i += ApplicationParameter.TRIPLET_LENGTH.ORDER_LENGTH;
break;
case ApplicationParameter.TRIPLET_TAGID.SEARCH_VALUE_TAGID:
//Synthetic comment -- @@ -495,12 +518,14 @@
} else {
appParamValue.searchValue = new String(appParam, i + 1, length);
}
                    if (D) Log.i(TAG, "AppParam searchValue=" + appParamValue.searchValue);
i += length;
i += 1;
break;
case ApplicationParameter.TRIPLET_TAGID.SEARCH_ATTRIBUTE_TAGID:
i += 2;
appParamValue.searchAttr = Byte.toString(appParam[i]);
                    if (D) Log.i(TAG, "AppParam searchAttr=" + appParamValue.searchAttr);
i += ApplicationParameter.TRIPLET_LENGTH.SEARCH_ATTRIBUTE_LENGTH;
break;
case ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID:
//Synthetic comment -- @@ -512,6 +537,7 @@
int lowValue = appParam[i + 1] & 0xff;
appParamValue.maxListCount = highValue * 256 + lowValue;
}
                    if (D) Log.i(TAG, "AppParam maxListCount=" + appParamValue.maxListCount);
i += ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH;
break;
case ApplicationParameter.TRIPLET_TAGID.LISTSTARTOFFSET_TAGID:
//Synthetic comment -- @@ -519,6 +545,7 @@
int highValue = appParam[i] & 0xff;
int lowValue = appParam[i + 1] & 0xff;
appParamValue.listStartOffset = highValue * 256 + lowValue;
                    if (D) Log.i(TAG, "AppParam listStartOffset=" + appParamValue.listStartOffset);
i += ApplicationParameter.TRIPLET_LENGTH.LISTSTARTOFFSET_LENGTH;
break;
case ApplicationParameter.TRIPLET_TAGID.FORMAT_TAGID:
//Synthetic comment -- @@ -526,6 +553,7 @@
if (appParam[i] != 0) {
appParamValue.vcard21 = false;
}
                    if (D) Log.i(TAG, "AppParam format=" + appParam[i]);
i += ApplicationParameter.TRIPLET_LENGTH.FORMAT_LENGTH;
break;
default:
//Synthetic comment -- @@ -535,8 +563,6 @@
}
}

return parseOk;
}

//Synthetic comment -- @@ -853,7 +879,7 @@
return pushBytes(op, ownerVcard);
} else {
return mVcardManager.composeAndSendPhonebookOneVcard(op, intIndex, vcard21, null,
                        mOrderBy, appParamValue.getActualFilter());
}
} else {
if (intIndex <= 0 || intIndex > size) {
//Synthetic comment -- @@ -864,7 +890,7 @@
// begin from 1.vcf
if (intIndex >= 1) {
return mVcardManager.composeAndSendCallLogVcards(appParamValue.needTag, op,
                        intIndex, intIndex, vcard21, appParamValue.getActualFilter());
}
}
return ResponseCodes.OBEX_HTTP_OK;
//Synthetic comment -- @@ -925,15 +951,15 @@
return pushBytes(op, ownerVcard);
} else {
return mVcardManager.composeAndSendPhonebookVcards(op, 1, endPoint, vcard21,
                            appParamValue.getActualFilter(), ownerVcard);
}
} else {
return mVcardManager.composeAndSendPhonebookVcards(op, startPoint, endPoint,
                        vcard21, appParamValue.getActualFilter(), null);
}
} else {
return mVcardManager.composeAndSendCallLogVcards(appParamValue.needTag, op,
                    startPoint + 1, endPoint + 1, vcard21, appParamValue.getActualFilter());
}
}









//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardComposer.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardComposer.java
new file mode 100644
//Synthetic comment -- index 0000000..c997f7d

//Synthetic comment -- @@ -0,0 +1,136 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.android.bluetooth.pbap;

import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import com.android.vcard.VCardComposer;
import com.android.vcard.VCardBuilder;
import com.android.vcard.VCardConfig;
import com.android.vcard.VCardPhoneNumberTranslationCallback;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.util.Log;
import android.telephony.PhoneNumberUtils;

public class BluetoothPbapVcardComposer extends VCardComposer
{
	private static final String LOG_TAG = "BluetoothPbapVcardComposer";

    public static final long FILTER_VERSION = (1L << 0); // vCard Version
    public static final long FILTER_FN = (1L << 1); // Formatted Name
    public static final long FILTER_N = (1L << 2); // Structured Presentation of Name
    public static final long FILTER_PHOTO = (1L << 3); // Associated Image or Photo
    public static final long FILTER_BDAY = (1L << 4); // Birthday
    public static final long FILTER_ADR = (1L << 5); // Delivery Address
    public static final long FILTER_LABEL = (1L << 6); // Delivery
    public static final long FILTER_TEL = (1L << 7); // Telephone Number
    public static final long FILTER_EMAIL = (1L << 8); // Electronic Mail Address
    public static final long FILTER_MAILER = (1L << 9); // Electronic Mail
    public static final long FILTER_TZ = (1L << 10); // Time Zone
    public static final long FILTER_GEO = (1L << 11); // Geographic Position
    public static final long FILTER_TITLE = (1L << 12); // Job
    public static final long FILTER_ROLE = (1L << 13); // Role within the Organization
    public static final long FILTER_LOGO = (1L << 14); // Organization Logo
    public static final long FILTER_AGENT = (1L << 15); // vCard of Person Representing
    public static final long FILTER_ORG = (1L << 16); // Name of Organization
    public static final long FILTER_NOTE = (1L << 17); // Comments
    public static final long FILTER_REV = (1L << 18); // Revision
    public static final long FILTER_SOUND = (1L << 19); // Pronunciation of Name
    public static final long FILTER_URL = (1L << 20); // Uniform Resource Locator
    public static final long FILTER_UID = (1L << 21); // Unique ID
    public static final long FILTER_KEY = (1L << 22); // Public Encryption Key
    public static final long FILTER_NICKNAME = (1L << 23); // Nickname
    public static final long FILTER_CATEGORIES = (1L << 24); // Categories
    public static final long FILTER_PROID = (1L << 25); // Product ID
    public static final long FILTER_CLASS = (1L << 26); // Class information
    public static final long FILTER_SORT_STRING = (1L << 27); // String used for sorting operations
    public static final long FILTER_X_IRMC_CALL_DATETIME = (1L << 28); // Time stamp

    private final int mVCardType;
    private final String mCharset;
    private final long mFilter;

    // BT does want PAUSE/WAIT conversion while it doesn't want the other formatting
    // done by vCard library by default.
    VCardPhoneNumberTranslationCallback callback =
            new VCardPhoneNumberTranslationCallback() {
                public String onValueReceived(
                        String rawValue, int type, String label, boolean isPrimary) {
                    // 'p' and 'w' are the standard characters for pause and wait
                    // (see RFC 3601)
                    // so use those when exporting phone numbers via vCard.
                    String numberWithControlSequence = rawValue
                            .replace(PhoneNumberUtils.PAUSE, 'p')
                            .replace(PhoneNumberUtils.WAIT, 'w');
                    return numberWithControlSequence;
                }
            };
    
    public BluetoothPbapVcardComposer(final Context context, final int vcardType,
            long filter, final boolean careHandlerErrors) {
        super(context, vcardType, null, careHandlerErrors);
        mVCardType = vcardType;
        mCharset = null;
        mFilter = filter;
        setPhoneNumberTranslationCallback(callback); // needed?
    }
    
    public String buildVCard(final Map<String, List<ContentValues>> contentValuesListMap) {
        if (contentValuesListMap == null) {
            Log.e(LOG_TAG, "The given map is null. Ignore and return empty String");
            return "";
        } else {
        	Log.i(LOG_TAG, "buildVCard filter = " + mFilter);
            final VCardBuilder builder = new VCardBuilder(mVCardType, mCharset);
            // not perfect here - perhaps subclass VCardBuilder to separate N and FN
            if (((mFilter & FILTER_N) != 0) || ((mFilter & FILTER_FN) != 0))
            	builder.appendNameProperties(contentValuesListMap.get(StructuredName.CONTENT_ITEM_TYPE));
            if ((mFilter & FILTER_NICKNAME) != 0)
            	builder.appendNickNames(contentValuesListMap.get(Nickname.CONTENT_ITEM_TYPE));
            if ((mFilter & FILTER_TEL) != 0)
            	builder.appendPhones(contentValuesListMap.get(Phone.CONTENT_ITEM_TYPE), callback);
            if ((mFilter & FILTER_EMAIL) != 0)
            	builder.appendEmails(contentValuesListMap.get(Email.CONTENT_ITEM_TYPE));
            if ((mFilter & FILTER_ADR) != 0)
            	builder.appendPostals(contentValuesListMap.get(StructuredPostal.CONTENT_ITEM_TYPE));
            if ((mFilter & FILTER_ORG) != 0)
            	builder.appendOrganizations(contentValuesListMap.get(Organization.CONTENT_ITEM_TYPE));
            if ((mFilter & FILTER_URL) != 0)
            	builder.appendWebsites(contentValuesListMap.get(Website.CONTENT_ITEM_TYPE));
            if ((mFilter & FILTER_PHOTO) != 0)
                builder.appendPhotos(contentValuesListMap.get(Photo.CONTENT_ITEM_TYPE));            
            if ((mFilter & FILTER_NOTE) != 0)
            	builder.appendNotes(contentValuesListMap.get(Note.CONTENT_ITEM_TYPE));
            if ((mFilter & FILTER_BDAY) != 0)
            	builder.appendEvents(contentValuesListMap.get(Event.CONTENT_ITEM_TYPE));
            //builder.appendIms(contentValuesListMap.get(Im.CONTENT_ITEM_TYPE));
            //builder.appendRelation(contentValuesListMap.get(Relation.CONTENT_ITEM_TYPE));
            return builder.toString();
        }
    }
}








//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java
//Synthetic comment -- index 659add6..85a68c0 100644

//Synthetic comment -- @@ -43,7 +43,6 @@
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.text.TextUtils;
import android.util.Log;

//Synthetic comment -- @@ -51,7 +50,6 @@
import com.android.vcard.VCardComposer;
import com.android.vcard.VCardConfig;
import com.android.internal.telephony.CallerInfo;

import java.io.IOException;
import java.io.OutputStream;
//Synthetic comment -- @@ -123,7 +121,7 @@
size = getCallHistorySize(type);
break;
}
        if (V) Log.v(TAG, "getPhonebookSize size = " + size + " type = " + type);
return size;
}

//Synthetic comment -- @@ -273,7 +271,7 @@
}

public final int composeAndSendCallLogVcards(final int type, Operation op,
            final int startPoint, final int endPoint, final boolean vcardType21, long filter) {
if (startPoint < 1 || startPoint > endPoint) {
Log.e(TAG, "internal error: startPoint or endPoint is not correct.");
return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
//Synthetic comment -- @@ -330,11 +328,11 @@

if (V) Log.v(TAG, "Call log query selection is: " + selection);

        return composeAndSendVCards(op, selection, vcardType21, filter, null, false);
}

public final int composeAndSendPhonebookVcards(Operation op, final int startPoint,
            final int endPoint, final boolean vcardType21, long filter, String ownerVCard) {
if (startPoint < 1 || startPoint > endPoint) {
Log.e(TAG, "internal error: startPoint or endPoint is not correct.");
return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
//Synthetic comment -- @@ -375,11 +373,11 @@

if (V) Log.v(TAG, "Query selection is: " + selection);

        return composeAndSendVCards(op, selection, vcardType21, filter, ownerVCard, true);
}

public final int composeAndSendPhonebookOneVcard(Operation op, final int offset,
            final boolean vcardType21, String ownerVCard, int orderByWhat, long filter) {
if (offset < 1) {
Log.e(TAG, "Internal error: offset is not correct.");
return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
//Synthetic comment -- @@ -424,11 +422,11 @@

if (V) Log.v(TAG, "Query selection is: " + selection);

        return composeAndSendVCards(op, selection, vcardType21, filter, ownerVCard, true);
}

public final int composeAndSendVCards(Operation op, final String selection,
            final boolean vcardType21, long filter, String ownerVCard, boolean isContacts) {
long timestamp = 0;
if (V) timestamp = System.currentTimeMillis();

//Synthetic comment -- @@ -445,22 +443,7 @@
}
vcardType |= VCardConfig.FLAG_REFRAIN_IMAGE_EXPORT;

                composer = new BluetoothPbapVcardComposer(mContext, vcardType, filter, true);
buffer = new HandlerForStringBuffer(op, ownerVCard);
if (!composer.init(Contacts.CONTENT_URI, selection, null, Contacts._ID) ||
!buffer.onInit(mContext)) {







