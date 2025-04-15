/*improve transmission bitrate

OPP file transmission mechanism gives performances
below expectations (about ~130kB/s instead of ~200kB/s).
The sending and receiving loops contained a sqlite
database update needed for progress bar refresh.
This db call could add up to 40% overhead in one
32kB transfer loop.

A thread is created and started just before
sending / receiving data with lowest priority set.
It waits for notifications from the transfer loop
to update the sqlite db at its own pace.
Additionaly byte rate is logged into logcat
every second during transfer.

Change-Id:I44d36b54e7e904fb656851ade6e7b199c6d946cdAuthor: Fabien Peix <fabienx.peix@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Author-tracking-BZ: 1941 17106*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppObexClientSession.java b/src/com/android/bluetooth/opp/BluetoothOppObexClientSession.java
//Synthetic comment -- index ea7e4b2..d83cb20 100644

//Synthetic comment -- @@ -442,6 +442,15 @@
}
}

while (!mInterrupted && okToProceed && (position != fileInfo.mLength)) {
{
if (V) timestamp = System.currentTimeMillis();
//Synthetic comment -- @@ -465,11 +474,17 @@
}
updateValues = new ContentValues();
updateValues.put(BluetoothShare.CURRENT_BYTES, position);
                                mContext1.getContentResolver().update(contentUri, updateValues,
                                        null, null);
}
}
}

if (responseCode == ResponseCodes.OBEX_HTTP_FORBIDDEN
|| responseCode == ResponseCodes.OBEX_HTTP_NOT_ACCEPTABLE) {
//Synthetic comment -- @@ -480,8 +495,8 @@
Log.i(TAG, "Remote reject file type " + fileInfo.mMimetype);
status = BluetoothShare.STATUS_NOT_ACCEPTABLE;
} else if (!mInterrupted && position == fileInfo.mLength) {
                        Log.i(TAG, "SendFile finished send out file " + fileInfo.mFileName
                                + " length " + fileInfo.mLength);
outputStream.close();
} else {
error = true;








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppObexServerSession.java b/src/com/android/bluetooth/opp/BluetoothOppObexServerSession.java
//Synthetic comment -- index 3512fad..8c1a9c2 100644

//Synthetic comment -- @@ -454,11 +454,21 @@
bos = new BufferedOutputStream(fileInfo.mOutputStream, 0x10000);
}

if (!error) {
int outputBufferSize = op.getMaxPacketSize();
byte[] b = new byte[outputBufferSize];
int readLength = 0;
long timestamp = 0;
try {
while ((!mInterrupted) && (position != fileInfo.mLength)) {

//Synthetic comment -- @@ -482,7 +492,12 @@

ContentValues updateValues = new ContentValues();
updateValues.put(BluetoothShare.CURRENT_BYTES, position);
                    mContext.getContentResolver().update(contentUri, updateValues, null, null);
}
} catch (IOException e1) {
Log.e(TAG, "Error when receiving file");
//Synthetic comment -- @@ -494,17 +509,21 @@
}
error = true;
}
}

if (mInterrupted) {
            if (D) Log.d(TAG, "receiving file interrupted by user.");
status = BluetoothShare.STATUS_CANCELED;
} else {
if (position == fileInfo.mLength) {
                if (D) Log.d(TAG, "Receiving file completed for " + fileInfo.mFileName);
status = BluetoothShare.STATUS_SUCCESS;
} else {
                if (D) Log.d(TAG, "Reading file failed at " + position + " of " + fileInfo.mLength);
if (status == -1) {
status = BluetoothShare.STATUS_UNKNOWN_ERROR;
}








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppObexSessionProgress.java b/src/com/android/bluetooth/opp/BluetoothOppObexSessionProgress.java
new file mode 100644
//Synthetic comment -- index 0000000..6ab0b04

//Synthetic comment -- @@ -0,0 +1,97 @@







