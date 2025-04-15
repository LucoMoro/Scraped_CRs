/*Wake up TransactService when MMS data is available

In 2G network, mobile data is suspend during voice
call. MMS data will be notified as unavailable.
After voice call is end, It may notify Default data
connection before MMS data connection. When it
notify Default connection, it is unavailable in
MMS networkInfo and TransactionService will not
launch transaction; When it nofity MMS connection,
it is not connected so it will not wake up
Transaction Service.

How to reproduce:
1) Make a voice call in 2G network;
2) Try to send a MMS during voice call;
3) MMS is sending and waiting for data connection;
4) Hang up the voice call;
5) MMS stays in Sending state.

Change-Id:Ia719e8736cc6ccfcd236bceea4cb94f7c4dd94e5Signed-off-by: Bin Li <libin@marvell.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MmsSystemEventReceiver.java b/src/com/android/mms/transaction/MmsSystemEventReceiver.java
//Synthetic comment -- index b8eb917..1879c55 100644

//Synthetic comment -- @@ -65,12 +65,18 @@
MmsApp.getApplication().getPduLoaderManager().removePdu(changed);
} else if (action.equals(TelephonyIntents.ACTION_ANY_DATA_CONNECTION_STATE_CHANGED)) {
String state = intent.getStringExtra(PhoneConstants.STATE_KEY);

if (Log.isLoggable(LogTag.TRANSACTION, Log.VERBOSE)) {
                Log.v(TAG, "ANY_DATA_STATE event received: " + state);
}

            if (state.equals("CONNECTED")) {
wakeUpService(context);
}
} else if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {







