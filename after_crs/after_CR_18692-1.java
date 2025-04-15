/*code cleanup.
some unused import statement, local variabls, private members and static constants removed form
classes in com.android.bluetooth/pbap package.

Change-Id:I95d7d403ab78afa6f9ada82cfb357056a6bf61d0*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapCallLogComposer.java b/src/com/android/bluetooth/pbap/BluetoothPbapCallLogComposer.java
//Synthetic comment -- index c5449fa..c7fadc1 100755

//Synthetic comment -- @@ -168,8 +168,6 @@
}

if (mCareHandlerErrors) {
for (OneEntryHandler handler : mHandlerList) {
if (!handler.onEntryCreated(vcard)) {
return false;








//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java b/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java
//Synthetic comment -- index 58ced0c..62d3c6f 100644

//Synthetic comment -- @@ -38,7 +38,6 @@
import android.text.TextUtils;
import android.util.Log;
import android.provider.CallLog.Calls;
import android.provider.CallLog;

import java.io.IOException;
//Synthetic comment -- @@ -138,8 +137,6 @@
// record current path the client are browsing
private String mCurrentPath = "";

private Handler mCallback = null;

private Context mContext;
//Synthetic comment -- @@ -168,7 +165,6 @@

public BluetoothPbapObexServer(Handler callback, Context context) {
super();
mCallback = callback;
mContext = context;
mVcardManager = new BluetoothPbapVcardManager(mContext);
//Synthetic comment -- @@ -910,7 +906,6 @@
if (D) Log.d(TAG, "pullPhonebook(): requestSize=" + requestSize + " startPoint=" +
startPoint + " endPoint=" + endPoint);

boolean vcard21 = appParamValue.vcard21;
if (appParamValue.needTag == BluetoothPbapObexServer.ContentType.PHONEBOOK) {
if (startPoint == 0) {








//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapService.java b/src/com/android/bluetooth/pbap/BluetoothPbapService.java
//Synthetic comment -- index e0a705f..c9194d4 100644

//Synthetic comment -- @@ -40,7 +40,6 @@
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothPbap;
//Synthetic comment -- @@ -51,13 +50,11 @@
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import javax.obex.ServerSession;

//Synthetic comment -- @@ -559,7 +556,6 @@
public void handleMessage(Message msg) {
if (VERBOSE) Log.v(TAG, "Handler(): got msg=" + msg.what);

switch (msg.what) {
case START_LISTENER:
if (mAdapter.isEnabled()) {








//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java b/src/com/android/bluetooth/pbap/BluetoothPbapVcardManager.java
//Synthetic comment -- index e9e7805..f78e1b0 100644

//Synthetic comment -- @@ -77,16 +77,8 @@
Contacts.DISPLAY_NAME, // 4
};

private static final int PHONE_NUMBER_COLUMN_INDEX = 3;

static final String SORT_ORDER_PHONE_NUMBER = CommonDataKinds.Phone.NUMBER + " ASC";

static final String[] CONTACTS_PROJECTION = new String[] {
//Synthetic comment -- @@ -495,7 +487,6 @@
* Handler to emit VCard String to PCE once size grow to maxPacketSize.
*/
public class HandlerForStringBuffer implements OneEntryHandler {
private Operation operation;

private OutputStream outputStream;







