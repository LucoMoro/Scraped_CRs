/*Problems sending/receiving MMS, tears down MMS APN

The MMS APN incorrectly tears down when connected. This is due to an
error in the code checking if the phone is connected to a MMS APN. In
TransactionSettings the default path is used, but this causes the
selection parameter to be ignored in TelephonyProvider. The result is
that when connected to a non-MMS APN it believes it is connected to a
MMS APN. The fix is to simply remove default in the URI and construct
the WHERE clause manually.

Change-Id:I25ffc26c68a40676841e34fd56aeab08604f4920*/




//Synthetic comment -- diff --git a/src/com/android/mms/transaction/TransactionSettings.java b/src/com/android/mms/transaction/TransactionSettings.java
//Synthetic comment -- index a086298..ba37d19 100644

//Synthetic comment -- @@ -61,12 +61,16 @@
* @param context The context of the MMS Client
*/
public TransactionSettings(Context context, String apnName) {
        String selection = Telephony.Carriers.CURRENT + " IS NOT NULL";
        String[] selectionArgs = null;
        if (apnName != null) {
            selection += " AND " + Telephony.Carriers.APN + "=?";
            selectionArgs = new String[]{ apnName.trim() };
        }

Cursor cursor = SqliteWrapper.query(context, context.getContentResolver(),
                            Telephony.Carriers.CONTENT_URI,
                            APN_PROJECTION, selection, selectionArgs, null);

if (Log.isLoggable(LogTag.TRANSACTION, Log.VERBOSE)) {
Log.v(TAG, "TransactionSettings looking for apn: " + selection + " returned: " +







