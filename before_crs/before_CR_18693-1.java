/*code cleanup.
unused imports, local variabls and static constans removed from
com.android.bluetooth.opp package.

Change-Id:I0fd682a190221e216fac71723c05d3828b9fa42d*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppBatch.java b/src/com/android/bluetooth/opp/BluetoothOppBatch.java
//Synthetic comment -- index 5913b4c..7f51fe2 100644

//Synthetic comment -- @@ -61,7 +61,6 @@

public class BluetoothOppBatch {
private static final String TAG = "BtOppBatch";
    private static final boolean D = Constants.DEBUG;
private static final boolean V = Constants.VERBOSE;

public int mId;








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppManager.java b/src/com/android/bluetooth/opp/BluetoothOppManager.java
//Synthetic comment -- index 73411d9..8fdab04 100644

//Synthetic comment -- @@ -55,7 +55,6 @@
*/
public class BluetoothOppManager {
private static final String TAG = "BluetoothOppManager";
    private static final boolean D = Constants.DEBUG;
private static final boolean V = Constants.VERBOSE;

private static BluetoothOppManager INSTANCE;








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppNotification.java b/src/com/android/bluetooth/opp/BluetoothOppNotification.java
//Synthetic comment -- index 310a289..ed0d402 100644

//Synthetic comment -- @@ -53,7 +53,6 @@
*/
class BluetoothOppNotification {
private static final String TAG = "BluetoothOppNotification";
    private static final boolean D = Constants.DEBUG;
private static final boolean V = Constants.VERBOSE;

static final String status = "(" + BluetoothShare.STATUS + " == '192'" + ")";








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppPreference.java b/src/com/android/bluetooth/opp/BluetoothOppPreference.java
//Synthetic comment -- index 7874222..8e7b7eb 100644

//Synthetic comment -- @@ -46,7 +46,6 @@
*/
public class BluetoothOppPreference {
private static final String TAG = "BluetoothOppPreference";
    private static final boolean D = Constants.DEBUG;
private static final boolean V = Constants.VERBOSE;

private static BluetoothOppPreference INSTANCE;








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppReceiver.java b/src/com/android/bluetooth/opp/BluetoothOppReceiver.java
//Synthetic comment -- index 98800d4..4361007 100644

//Synthetic comment -- @@ -54,7 +54,6 @@
*/
public class BluetoothOppReceiver extends BroadcastReceiver {
private static final String TAG = "BluetoothOppReceiver";
    private static final boolean D = Constants.DEBUG;
private static final boolean V = Constants.VERBOSE;

@Override








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppRfcommListener.java b/src/com/android/bluetooth/opp/BluetoothOppRfcommListener.java
//Synthetic comment -- index c33cb66..9d9fa18 100644

//Synthetic comment -- @@ -49,8 +49,6 @@
public class BluetoothOppRfcommListener {
private static final String TAG = "BtOppRfcommListener";

    private static final boolean D = Constants.DEBUG;

private static final boolean V = Constants.VERBOSE;

public static final int MSG_INCOMING_BTOPP_CONNECTION = 100;








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppSendFileInfo.java b/src/com/android/bluetooth/opp/BluetoothOppSendFileInfo.java
//Synthetic comment -- index 44ca9bd..a9d0578 100644

//Synthetic comment -- @@ -43,7 +43,6 @@
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.provider.ContactsContract.Contacts;

/**
* This class stores information about a single sending file It will only be








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppService.java b/src/com/android/bluetooth/opp/BluetoothOppService.java
//Synthetic comment -- index 51b282c..02bc5d5 100644

//Synthetic comment -- @@ -37,9 +37,7 @@

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
//Synthetic comment -- @@ -263,7 +261,7 @@
} catch (IOException e) {
Log.e(TAG, "close tranport error");
}
                        } else if (Constants.USE_TCP_DEBUG && !Constants.USE_TCP_SIMPLE_SERVER){
Log.i(TAG, "Start Obex Server in TCP DEBUG mode");
createServerSession(transport);
} else {








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppTransfer.java b/src/com/android/bluetooth/opp/BluetoothOppTransfer.java
//Synthetic comment -- index d892e70..3b76351 100644

//Synthetic comment -- @@ -100,18 +100,12 @@

private EventHandler mSessionHandler;

    /*
     * TODO check if we need PowerManager here
     */
    private PowerManager mPowerManager;

private long mTimestamp;

public BluetoothOppTransfer(Context context, PowerManager powerManager,
BluetoothOppBatch batch, BluetoothOppObexSession session) {

mContext = context;
        mPowerManager = powerManager;
mBatch = batch;
mSession = session;









//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppTransferActivity.java b/src/com/android/bluetooth/opp/BluetoothOppTransferActivity.java
//Synthetic comment -- index d63a6fa..e36df0b 100644

//Synthetic comment -- @@ -36,7 +36,6 @@

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppTransferHistory.java b/src/com/android/bluetooth/opp/BluetoothOppTransferHistory.java
//Synthetic comment -- index 1f5d9d9..7679544 100644

//Synthetic comment -- @@ -62,8 +62,6 @@
View.OnCreateContextMenuListener, OnItemClickListener {
private static final String TAG = "BluetoothOppTransferHistory";

    private static final boolean D = Constants.DEBUG;

private static final boolean V = Constants.VERBOSE;

private ListView mListView;
//Synthetic comment -- @@ -242,7 +240,7 @@
* android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
* .AdapterView, android.view.View, int, long)
*/
    public void onItemClick(AdapterView parent, View view, int position, long id) {
// Open the selected item
mTransferCursor.moveToPosition(position);
openCompleteTransfer();








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/TestActivity.java b/src/com/android/bluetooth/opp/TestActivity.java
//Synthetic comment -- index 4266f68..ed8226b 100644

//Synthetic comment -- @@ -455,8 +455,6 @@
class TestTcpServer extends ServerRequestHandler implements Runnable {
private static final String TAG = "ServerRequestHandler";

    private static final boolean D = Constants.DEBUG;

private static final boolean V = Constants.VERBOSE;

static final int port = 6500;







