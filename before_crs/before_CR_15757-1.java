/*No changes - just restored changed files from last sync over the orignals.

Change-Id:I2ece6d01902f6b1636fbb847c43aba3c88d53510*/
//Synthetic comment -- diff --git a/core/java/android/provider/Telephony.java b/core/java/android/provider/Telephony.java
//Synthetic comment -- index bf9e854..113d1084 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package android.provider;

import android.annotation.SdkConstant;
import android.annotation.SdkConstant.SdkConstantType;
import android.content.ContentResolver;
//Synthetic comment -- @@ -23,14 +25,12 @@
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Config;
import android.util.Log;
import android.util.Patterns;


import java.util.HashSet;
import java.util.Set;
//Synthetic comment -- @@ -101,12 +101,6 @@
public static final String READ = "read";

/**
         * Indicates whether this message has been seen by the user. The "seen" flag will be
         * used to figure out whether we need to throw up a statusbar notification or not.
         */
        public static final String SEEN = "seen";

        /**
* The TP-Status value for the message, or -1 if no status has
* been received
*/
//Synthetic comment -- @@ -158,18 +152,6 @@
* <P>Type: INTEGER (boolean)</P>
*/
public static final String LOCKED = "locked";

        /**
         * Error code associated with sending or receiving this message
         * <P>Type: INTEGER</P>
         */
        public static final String ERROR_CODE = "error_code";

        /**
         * Meta data used externally.
         * <P>Type: TEXT</P>
         */
        public static final String META_DATA = "meta_data";
}

/**
//Synthetic comment -- @@ -261,7 +243,7 @@
* @return true if the operation succeeded
*/
public static boolean moveMessageToFolder(Context context,
                Uri uri, int folder, int error) {
if (uri == null) {
return false;
}
//Synthetic comment -- @@ -284,7 +266,7 @@
return false;
}

            ContentValues values = new ContentValues(3);

values.put(TYPE, folder);
if (markAsUnread) {
//Synthetic comment -- @@ -292,7 +274,6 @@
} else if (markAsRead) {
values.put(READ, Integer.valueOf(1));
}
            values.put(ERROR_CODE, error);

return 1 == SqliteWrapper.update(context, context.getContentResolver(),
uri, values, null, null);
//Synthetic comment -- @@ -564,12 +545,21 @@
*   <li><em>transactionId (Integer)</em> - The WAP transaction
*    ID</li>
*   <li><em>pduType (Integer)</em> - The WAP PDU type</li>
             *   <li><em>header (byte[])</em> - The header of the message</li>
             *   <li><em>data (byte[])</em> - The data payload of the message</li>
* </ul>
*
* <p>If a BroadcastReceiver encounters an error while processing
* this intent it should set the result code appropriately.</p>
*/
@SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
public static final String WAP_PUSH_RECEIVED_ACTION =
//Synthetic comment -- @@ -658,12 +648,6 @@
public static final String READ = "read";

/**
         * Indicates whether this message has been seen by the user. The "seen" flag will be
         * used to figure out whether we need to throw up a statusbar notification or not.
         */
        public static final String SEEN = "seen";

        /**
* The Message-ID of the message.
* <P>Type: TEXT</P>
*/
//Synthetic comment -- @@ -1070,12 +1054,6 @@
* <P>Type: INTEGER (boolean)</P>
*/
public static final String LOCKED = "locked";

        /**
         * Meta data used externally.
         * <P>Type: TEXT</P>
         */
        public static final String META_DATA = "meta_data";
}

/**
//Synthetic comment -- @@ -1121,7 +1099,6 @@
* <P>Type: INTEGER</P>
*/
public static final String READ = "read";

/**
* The snippet of the latest message in the thread.
* <P>Type: TEXT</P>
//Synthetic comment -- @@ -1316,7 +1293,7 @@
}

String s = extractAddrSpec(address);
            Matcher match = Patterns.EMAIL_ADDRESS.matcher(s);
return match.matches();
}

//Synthetic comment -- @@ -1331,7 +1308,7 @@
return false;
}

            Matcher match = Patterns.PHONE.matcher(number);
return match.matches();
}

//Synthetic comment -- @@ -1662,13 +1639,6 @@
*/
public static final String LAST_TRY = "last_try";
}

        public static final class WordsTable {
            public static final String ID = "_id";
            public static final String SOURCE_ROW_ID = "source_id";
            public static final String TABLE_ID = "table_to_use";
            public static final String INDEXED_TEXT = "index_text";
        }
}

public static final class Carriers implements BaseColumns {








//Synthetic comment -- diff --git a/core/java/android/provider/Telephony.java~ b/core/java/android/provider/Telephony.java~
new file mode 100644
//Synthetic comment -- index 0000000..441a27f

//Synthetic comment -- @@ -0,0 +1,1772 @@








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WapPushOverSms.java b/telephony/java/com/android/internal/telephony/WapPushOverSms.java
//Synthetic comment -- index 4ee36af..aab32ed 100644

//Synthetic comment -- @@ -134,7 +134,12 @@
break;
case WspTypeDecoder.CONTENT_TYPE_B_EMN:
	mimeType = WspTypeDecoder.CONTENT_MIME_TYPE_B_EMN;
	break;
default:
if (Config.LOGD) {
Log.w(LOG_TAG,
//Synthetic comment -- @@ -159,7 +164,7 @@
binaryContentType = WspTypeDecoder.CONTENT_TYPE_B_VND_DOCOMO_PF;                
} else if (mimeType.equals(WspTypeDecoder.CONTENT_MIME_TYPE_B_EMN)) {
binaryContentType = WspTypeDecoder.CONTENT_TYPE_B_EMN;
                Log.w(LOG_TAG, "Received PDU. CONTENT_TYPE_B_EMN");
} else {
if (Config.LOGD) Log.w(LOG_TAG, "Received PDU. Unknown Content-Type = " + mimeType);
return Intents.RESULT_SMS_HANDLED;








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WspTypeDecoder.java b/telephony/java/com/android/internal/telephony/WspTypeDecoder.java
//Synthetic comment -- index 7af76af..867dac0 100644

//Synthetic comment -- @@ -39,8 +39,12 @@
public static final int CONTENT_TYPE_B_PUSH_SL = 0x30;
public static final int CONTENT_TYPE_B_PUSH_CO = 0x32;
public static final int CONTENT_TYPE_B_MMS = 0x3e;
public static final int CONTENT_TYPE_B_VND_DOCOMO_PF = 0x0310;
public static final int CONTENT_TYPE_B_EMN = 0x030A;

public static final String CONTENT_MIME_TYPE_B_DRM_RIGHTS_XML =
"application/vnd.oma.drm.rights+xml";
//Synthetic comment -- @@ -50,8 +54,12 @@
public static final String CONTENT_MIME_TYPE_B_PUSH_SL = "application/vnd.wap.slc";
public static final String CONTENT_MIME_TYPE_B_PUSH_CO = "application/vnd.wap.coc";
public static final String CONTENT_MIME_TYPE_B_MMS = "application/vnd.wap.mms-message";
public static final String CONTENT_MIME_TYPE_B_VND_DOCOMO_PF = "application/vnd.docomo.pf";
public static final String CONTENT_MIME_TYPE_B_EMN = "application/vnd.wap.emn+wbxml";

public static final int PARAMETER_ID_X_WAP_APPLICATION_ID = 0x2f;








