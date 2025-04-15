/*Formatting PhoneNumber at QuickContact.

Change-Id:Icd1dbde2af51d58a481f1efb4e0d550d49896108*/




//Synthetic comment -- diff --git a/src/com/android/contacts/ui/QuickContactWindow.java b/src/com/android/contacts/ui/QuickContactWindow.java
//Synthetic comment -- index 889749e..6e9b347 100644

//Synthetic comment -- @@ -59,6 +59,7 @@
import android.provider.ContactsContract.CommonDataKinds.SipAddress;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
//Synthetic comment -- @@ -777,6 +778,7 @@
if (Phone.CONTENT_ITEM_TYPE.equals(mimeType)) {
final String number = getAsString(cursor, Phone.NUMBER);
if (!TextUtils.isEmpty(number)) {
                    mBody = PhoneNumberUtils.formatNumber(number);
final Uri callUri = Uri.fromParts(Constants.SCHEME_TEL, number, null);
mIntent = new Intent(Intent.ACTION_CALL_PRIVILEGED, callUri);
}
//Synthetic comment -- @@ -795,6 +797,7 @@
} else if (Constants.MIME_SMS_ADDRESS.equals(mimeType)) {
final String number = getAsString(cursor, Phone.NUMBER);
if (!TextUtils.isEmpty(number)) {
                    mBody = PhoneNumberUtils.formatNumber(number);
final Uri smsUri = Uri.fromParts(Constants.SCHEME_SMSTO, number, null);
mIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
}







