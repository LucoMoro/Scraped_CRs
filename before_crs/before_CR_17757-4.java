/*Problems sending/receiving MMS, tears down MMS APN

The MMS APN incorrectly tears down when connected. This is due to an
error in the code checking if the phone is connected to a MMS APN. In
TransactionSettings the default path is used, but this causes the
selection parameter to be ignored in TelephonyProvider. The result is
that when connected to a non-MMS APN it believes it is connected to a
MMS APN. The fix is to simply remove default in the URI and construct
the WHERE clause manually.*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/TransactionSettings.java b/src/com/android/mms/transaction/TransactionSettings.java
//Synthetic comment -- index f65fd33..5c731f5 100644

//Synthetic comment -- @@ -60,12 +60,16 @@
* @param context The context of the MMS Client
*/
public TransactionSettings(Context context, String apnName) {
        String selection = (apnName != null)?
                Telephony.Carriers.APN + "='" + apnName.trim() + "'": null;

Cursor cursor = SqliteWrapper.query(context, context.getContentResolver(),
                            Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "current"),
                            APN_PROJECTION, selection, null, null);

if (cursor == null) {
Log.e(TAG, "Apn is not found in Database!");







