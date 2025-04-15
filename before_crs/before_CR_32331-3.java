/*Added support for Filter parameter for PBAP function PullPhoneBook

VW Premium car kit sometimes behaves strangely if it receives entries with unexpected fields.
E.g. it does not like mail addresses with additional attributes.

Change-Id:I6d3145d802ad87e5fb47217a213f9bcb12670f60Signed-off-by: Martin Gerczuk <admin@android-rsap.com>*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java b/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java
//Synthetic comment -- index 6002263..232d46e 100755

//Synthetic comment -- @@ -449,6 +449,8 @@

public boolean vcard21;

public AppParamValue() {
maxListCount = 0xFFFF;
listStartOffset = 0;
//Synthetic comment -- @@ -457,12 +459,22 @@
order = "";
needTag = 0x00;
vcard21 = true;
}

        public void dump() {
            Log.i(TAG, "maxListCount=" + maxListCount + " listStartOffset=" + listStartOffset
                    + " searchValue=" + searchValue + " searchAttr=" + searchAttr + " needTag="
                    + needTag + " vcard21=" + vcard21 + " order=" + order);
}
}

//Synthetic comment -- @@ -475,11 +487,21 @@
switch (appParam[i]) {
case ApplicationParameter.TRIPLET_TAGID.FILTER_TAGID:
i += 2; // length and tag field in triplet
i += ApplicationParameter.TRIPLET_LENGTH.FILTER_LENGTH;
break;
case ApplicationParameter.TRIPLET_TAGID.ORDER_TAGID:
i += 2; // length and tag field in triplet
appParamValue.order = Byte.toString(appParam[i]);
i += ApplicationParameter.TRIPLET_LENGTH.ORDER_LENGTH;
break;
case ApplicationParameter.TRIPLET_TAGID.SEARCH_VALUE_TAGID:
//Synthetic comment -- @@ -495,12 +517,14 @@
} else {
appParamValue.searchValue = new String(appParam, i + 1, length);
}
i += length;
i += 1;
break;
case ApplicationParameter.TRIPLET_TAGID.SEARCH_ATTRIBUTE_TAGID:
i += 2;
appParamValue.searchAttr = Byte.toString(appParam[i]);
i += ApplicationParameter.TRIPLET_LENGTH.SEARCH_ATTRIBUTE_LENGTH;
break;
case ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID:
//Synthetic comment -- @@ -512,6 +536,7 @@
int lowValue = appParam[i + 1] & 0xff;
appParamValue.maxListCount = highValue * 256 + lowValue;
}
i += ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH;
break;
case ApplicationParameter.TRIPLET_TAGID.LISTSTARTOFFSET_TAGID:
//Synthetic comment -- @@ -519,6 +544,7 @@
int highValue = appParam[i] & 0xff;
int lowValue = appParam[i + 1] & 0xff;
appParamValue.listStartOffset = highValue * 256 + lowValue;
i += ApplicationParameter.TRIPLET_LENGTH.LISTSTARTOFFSET_LENGTH;
break;
case ApplicationParameter.TRIPLET_TAGID.FORMAT_TAGID:
//Synthetic comment -- @@ -526,6 +552,7 @@
if (appParam[i] != 0) {
appParamValue.vcard21 = false;
}
i += ApplicationParameter.TRIPLET_LENGTH.FORMAT_LENGTH;
break;
default:
//Synthetic comment -- @@ -535,8 +562,6 @@
}
}

        if (D) appParamValue.dump();

return parseOk;
}

//Synthetic comment -- @@ -853,7 +878,7 @@
return pushBytes(op, ownerVcard);
} else {
return mVcardManager.composeAndSendPhonebookOneVcard(op, intIndex, vcard21, null,
                        mOrderBy );
}
} else {
if (intIndex <= 0 || intIndex > size) {
//Synthetic comment -- @@ -864,7 +889,7 @@
// begin from 1.vcf
if (intIndex >= 1) {
return mVcardManager.composeAndSendCallLogVcards(appParamValue.needTag, op,
                        intIndex, intIndex, vcard21);
}
}
return ResponseCodes.OBEX_HTTP_OK;
//Synthetic comment -- @@ -925,15 +950,15 @@
return pushBytes(op, ownerVcard);
} else {
return mVcardManager.composeAndSendPhonebookVcards(op, 1, endPoint, vcard21,
                            ownerVcard);
}
} else {
return mVcardManager.composeAndSendPhonebookVcards(op, startPoint, endPoint,
                        vcard21, null);
}
} else {
return mVcardManager.composeAndSendCallLogVcards(appParamValue.needTag, op,
                    startPoint + 1, endPoint + 1, vcard21);
}
}









//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardComposer.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardComposer.java
new file mode 100644
//Synthetic comment -- index 0000000..0df80d1

//Synthetic comment -- @@ -0,0 +1,138 @@








//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java
//Synthetic comment -- index 659add6..85a68c0 100644

//Synthetic comment -- @@ -43,7 +43,6 @@
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;

//Synthetic comment -- @@ -51,7 +50,6 @@
import com.android.vcard.VCardComposer;
import com.android.vcard.VCardConfig;
import com.android.internal.telephony.CallerInfo;
import com.android.vcard.VCardPhoneNumberTranslationCallback;

import java.io.IOException;
import java.io.OutputStream;
//Synthetic comment -- @@ -123,7 +121,7 @@
size = getCallHistorySize(type);
break;
}
        if (V) Log.v(TAG, "getPhonebookSzie size = " + size + " type = " + type);
return size;
}

//Synthetic comment -- @@ -273,7 +271,7 @@
}

public final int composeAndSendCallLogVcards(final int type, Operation op,
            final int startPoint, final int endPoint, final boolean vcardType21) {
if (startPoint < 1 || startPoint > endPoint) {
Log.e(TAG, "internal error: startPoint or endPoint is not correct.");
return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
//Synthetic comment -- @@ -330,11 +328,11 @@

if (V) Log.v(TAG, "Call log query selection is: " + selection);

        return composeAndSendVCards(op, selection, vcardType21, null, false);
}

public final int composeAndSendPhonebookVcards(Operation op, final int startPoint,
            final int endPoint, final boolean vcardType21, String ownerVCard) {
if (startPoint < 1 || startPoint > endPoint) {
Log.e(TAG, "internal error: startPoint or endPoint is not correct.");
return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
//Synthetic comment -- @@ -375,11 +373,11 @@

if (V) Log.v(TAG, "Query selection is: " + selection);

        return composeAndSendVCards(op, selection, vcardType21, ownerVCard, true);
}

public final int composeAndSendPhonebookOneVcard(Operation op, final int offset,
            final boolean vcardType21, String ownerVCard, int orderByWhat) {
if (offset < 1) {
Log.e(TAG, "Internal error: offset is not correct.");
return ResponseCodes.OBEX_HTTP_INTERNAL_ERROR;
//Synthetic comment -- @@ -424,11 +422,11 @@

if (V) Log.v(TAG, "Query selection is: " + selection);

        return composeAndSendVCards(op, selection, vcardType21, ownerVCard, true);
}

public final int composeAndSendVCards(Operation op, final String selection,
            final boolean vcardType21, String ownerVCard, boolean isContacts) {
long timestamp = 0;
if (V) timestamp = System.currentTimeMillis();

//Synthetic comment -- @@ -445,22 +443,7 @@
}
vcardType |= VCardConfig.FLAG_REFRAIN_IMAGE_EXPORT;

                composer = new VCardComposer(mContext, vcardType, true);
                // BT does want PAUSE/WAIT conversion while it doesn't want the other formatting
                // done by vCard library by default.
                composer.setPhoneNumberTranslationCallback(
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
                        });
buffer = new HandlerForStringBuffer(op, ownerVCard);
if (!composer.init(Contacts.CONTENT_URI, selection, null, Contacts._ID) ||
!buffer.onInit(mContext)) {







