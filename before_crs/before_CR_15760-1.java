/*No change but git insists it has !

Change-Id:I7a2aeed38fd9dbb2ffc03e8ea320e662e06480f7*/
//Synthetic comment -- diff --git a/core/java/android/provider/Telephony.java b/core/java/android/provider/Telephony.java
//Synthetic comment -- index 113d1084..bf9e854 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package android.provider;

import com.google.android.mms.util.SqliteWrapper;

import android.annotation.SdkConstant;
import android.annotation.SdkConstant.SdkConstantType;
import android.content.ContentResolver;
//Synthetic comment -- @@ -25,12 +23,14 @@
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.text.util.Regex;
import android.util.Config;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;
//Synthetic comment -- @@ -101,6 +101,12 @@
public static final String READ = "read";

/**
* The TP-Status value for the message, or -1 if no status has
* been received
*/
//Synthetic comment -- @@ -152,6 +158,18 @@
* <P>Type: INTEGER (boolean)</P>
*/
public static final String LOCKED = "locked";
}

/**
//Synthetic comment -- @@ -243,7 +261,7 @@
* @return true if the operation succeeded
*/
public static boolean moveMessageToFolder(Context context,
                Uri uri, int folder) {
if (uri == null) {
return false;
}
//Synthetic comment -- @@ -266,7 +284,7 @@
return false;
}

            ContentValues values = new ContentValues(2);

values.put(TYPE, folder);
if (markAsUnread) {
//Synthetic comment -- @@ -274,6 +292,7 @@
} else if (markAsRead) {
values.put(READ, Integer.valueOf(1));
}

return 1 == SqliteWrapper.update(context, context.getContentResolver(),
uri, values, null, null);
//Synthetic comment -- @@ -545,21 +564,12 @@
*   <li><em>transactionId (Integer)</em> - The WAP transaction
*    ID</li>
*   <li><em>pduType (Integer)</em> - The WAP PDU type</li>
             *   <li><em>data</em> - The data payload of the message</li>
             *   <li><em>contentTypeParameters (HashMap&lt;String,String&gt;)</em>
             *   - Any parameters associated with the content type
             *   (decoded from the WSP Content-Type header)</li>
* </ul>
*
* <p>If a BroadcastReceiver encounters an error while processing
* this intent it should set the result code appropriately.</p>
             *
             * <p>The contentTypeParameters extra value is map of content parameters keyed by
             * their names.</p>
             *
             * <p>If any unassigned well-known parameters are encountered, the key of the map will
             * be 'unassigned/0x...', where '...' is the hex value of the unassigned parameter.  If
             * a parameter has No-Value the value in the map will be null.</p>
*/
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
public static final String WAP_PUSH_RECEIVED_ACTION =
//Synthetic comment -- @@ -648,6 +658,12 @@
public static final String READ = "read";

/**
* The Message-ID of the message.
* <P>Type: TEXT</P>
*/
//Synthetic comment -- @@ -1054,6 +1070,12 @@
* <P>Type: INTEGER (boolean)</P>
*/
public static final String LOCKED = "locked";
}

/**
//Synthetic comment -- @@ -1099,6 +1121,7 @@
* <P>Type: INTEGER</P>
*/
public static final String READ = "read";
/**
* The snippet of the latest message in the thread.
* <P>Type: TEXT</P>
//Synthetic comment -- @@ -1293,7 +1316,7 @@
}

String s = extractAddrSpec(address);
            Matcher match = Regex.EMAIL_ADDRESS_PATTERN.matcher(s);
return match.matches();
}

//Synthetic comment -- @@ -1308,7 +1331,7 @@
return false;
}

            Matcher match = Regex.PHONE_PATTERN.matcher(number);
return match.matches();
}

//Synthetic comment -- @@ -1639,6 +1662,13 @@
*/
public static final String LAST_TRY = "last_try";
}
}

public static final class Carriers implements BaseColumns {







