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

                    /* Instantiate content resolver update thread */
                    BluetoothOppObexSessionProgress bluetoothOppObexSessionProgress = new BluetoothOppObexSessionProgress(TAG,
                        contentUri, mContext1, fileInfo.mLength);

                    /* Start content resolver update thread, it will wait for the next updateAvailable call */
                    bluetoothOppObexSessionProgress.start();

                    long startTimeStamp = System.currentTimeMillis();

while (!mInterrupted && okToProceed && (position != fileInfo.mLength)) {
{
if (V) timestamp = System.currentTimeMillis();
//Synthetic comment -- @@ -465,11 +474,17 @@
}
updateValues = new ContentValues();
updateValues.put(BluetoothShare.CURRENT_BYTES, position);

                                /* Give the position update to content resolver thread.
                                 * It will handle the update to the data table as a low priority
                                 * without harming OPP performance
                                 */
                                bluetoothOppObexSessionProgress.updateAvailable(updateValues);
}
}
}
                    /* Exit content resolver update thread */
                    bluetoothOppObexSessionProgress.exit();

if (responseCode == ResponseCodes.OBEX_HTTP_FORBIDDEN
|| responseCode == ResponseCodes.OBEX_HTTP_NOT_ACCEPTABLE) {
//Synthetic comment -- @@ -480,8 +495,8 @@
Log.i(TAG, "Remote reject file type " + fileInfo.mMimetype);
status = BluetoothShare.STATUS_NOT_ACCEPTABLE;
} else if (!mInterrupted && position == fileInfo.mLength) {
                        Log.i(TAG, "SendFile finished send out file " + fileInfo.mFileName + " length " + fileInfo.mLength + " at "
                            + fileInfo.mLength / (System.currentTimeMillis() - startTimeStamp) + " kB/s");
outputStream.close();
} else {
error = true;








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppObexServerSession.java b/src/com/android/bluetooth/opp/BluetoothOppObexServerSession.java
//Synthetic comment -- index 3512fad..8c1a9c2 100644

//Synthetic comment -- @@ -454,11 +454,21 @@
bos = new BufferedOutputStream(fileInfo.mOutputStream, 0x10000);
}

        long startTimeStamp = System.currentTimeMillis();

if (!error) {
int outputBufferSize = op.getMaxPacketSize();
byte[] b = new byte[outputBufferSize];
int readLength = 0;
long timestamp = 0;

            /* Instantiate content resolver update thread */
            BluetoothOppObexSessionProgress bluetoothOppObexSessionProgress = new BluetoothOppObexSessionProgress(TAG,
                contentUri, mContext, fileInfo.mLength);

            /* Start content resolver update thread, it will wait for the next updateAvailable call */
            bluetoothOppObexSessionProgress.start();

try {
while ((!mInterrupted) && (position != fileInfo.mLength)) {

//Synthetic comment -- @@ -482,7 +492,12 @@

ContentValues updateValues = new ContentValues();
updateValues.put(BluetoothShare.CURRENT_BYTES, position);

                    /* Give the position update to content resolver thread.
                     * It will handle the update to the data table as a low priority
                     * without harming OPP performance
                     */
                    bluetoothOppObexSessionProgress.updateAvailable(updateValues);
}
} catch (IOException e1) {
Log.e(TAG, "Error when receiving file");
//Synthetic comment -- @@ -494,17 +509,21 @@
}
error = true;
}

            /* Exit content resolver update thread */
            bluetoothOppObexSessionProgress.exit();
}

if (mInterrupted) {
            Log.i(TAG, "receiving file interrupted by user.");
status = BluetoothShare.STATUS_CANCELED;
} else {
if (position == fileInfo.mLength) {
                Log.i(TAG, "Receiving file completed for " + fileInfo.mFileName + " at "
                    + fileInfo.mLength / (System.currentTimeMillis() - startTimeStamp) + " kB/s");
status = BluetoothShare.STATUS_SUCCESS;
} else {
                Log.i(TAG, "Reading file failed at " + position + " of " + fileInfo.mLength);
if (status == -1) {
status = BluetoothShare.STATUS_UNKNOWN_ERROR;
}








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppObexSessionProgress.java b/src/com/android/bluetooth/opp/BluetoothOppObexSessionProgress.java
new file mode 100644
//Synthetic comment -- index 0000000..6ab0b04

//Synthetic comment -- @@ -0,0 +1,97 @@
package com.android.bluetooth.opp;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Process;
import android.util.Log;

/* This thread is dedicated to handling position updates
 * (used for progress bar display) in parallel of OPP file
 * transfer as a low priority task to preserve an optimum bitrate
 */
public class BluetoothOppObexSessionProgress extends Thread {

    private Uri mContentUri;
    private ContentValues mUpdateValues;
    private Context mCtx;
    private boolean mExit;
    private long mLastTimeStamp;
    private long mFileLength;
    private long mLastPosition;
    private String mTag;

    public BluetoothOppObexSessionProgress(String tag, Uri contentUri, Context ctx, long fileLength){

        super("ContentResolverUpdateThread");

        mContentUri = contentUri;
        mCtx = ctx;
        mExit = false;
        mUpdateValues = null;
        mFileLength = fileLength;
        mTag = tag;
    }

    @Override
    public synchronized void run() {

        /* Set the thread to the lowest possible priority
         * to preserve optimal throughput */
        Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);

        mLastTimeStamp = System.currentTimeMillis();
        mLastPosition = 0;

        /* Loop handling position updates when they are available */
        while (!mExit) {

            try {
                /* Wait until updateAvailable or exit notification */
                wait();
            } catch (Exception e) {
                Log.e(mTag, "Unexpected exception in ContentResolverUpdateThread wait() : " + e.getMessage());
            }

            /* If an update is available */
            if (mUpdateValues != null) {
                /* Log byte rate no more than every second */
                long currentTimeStamp = System.currentTimeMillis();
                if (currentTimeStamp - mLastTimeStamp >= 1000) {
                    long currentPosition = mUpdateValues.getAsInteger(BluetoothShare.CURRENT_BYTES);
                    long completion = currentPosition * 100 / mFileLength;
                    long byteRate = (currentPosition - mLastPosition) / (currentTimeStamp - mLastTimeStamp);
                    mLastPosition = currentPosition;
                    mLastTimeStamp = currentTimeStamp;
                    Log.i (mTag, "File transfer " + currentPosition + "/" + mFileLength
                        + " [" + completion + "%] ongoing at " + byteRate + " kB/s");
                }

                /* Update data table */
                mCtx.getContentResolver().update(mContentUri, mUpdateValues,
                        null, null);
                /* Update was treated, remove it */
                mUpdateValues = null;

                Log.d(mTag, "Uri position update done");
            }
        }
        Log.d(mTag, "Exiting ContentResolverUpdateThread thread");
    }

    public synchronized void exit() {

        Log.d(mTag, "ContentResolverUpdateThread thread exit requested");
        mExit = true;
        /* Notify thread so that it can exit wait() and proceed */
        notify();
    }

    public synchronized void updateAvailable(ContentValues updateValues) {

        Log.d(mTag, "ContentResolverUpdateThread position update notified");
        mUpdateValues = updateValues;
        /* Notify thread so that it can exit wait() and proceed */
        notify();
    }
}







