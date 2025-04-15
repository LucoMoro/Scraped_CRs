/*Reduce GC in Bluetooth application

During OPP transfer, the Bluetooth application uses a ContentProvider
to keep track of how many bytes have been transferred. An update()
is called every 64Kb and in turn causes a couple of query(). One of
the listeners updates a notification. All of this causes a lot of
small memory allocations and means that the GC has to clean up a lot
of objects during transfer.
Partly fixed by only calling update() every percent of transfer
instead of every 64Kb block, which means it works best on large
files.

Change-Id:Ia24b5e460fd52ed066867887ff6d22c579ec8e7e*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppObexClientSession.java b/src/com/android/bluetooth/opp/BluetoothOppObexClientSession.java
//Synthetic comment -- index ea7e4b2..178c0fa 100644

//Synthetic comment -- @@ -442,6 +442,10 @@
}
}

                    final int onePercent = Math.max((int)(fileInfo.mLength / 100), 1);
                    int percentPosition = 0;
                    updateValues = new ContentValues();

while (!mInterrupted && okToProceed && (position != fileInfo.mLength)) {
{
if (V) timestamp = System.currentTimeMillis();
//Synthetic comment -- @@ -458,15 +462,21 @@
okToProceed = false;
} else {
position += readLength;
                                percentPosition += readLength;
if (V) {
Log.v(TAG, "Sending file position = " + position
+ " readLength " + readLength + " bytes took "
+ (System.currentTimeMillis() - timestamp) + " ms");
}

                                // Limit the number of update() calls to once per percent as it is
                                // expensive.
                                if (percentPosition >= onePercent) {
                                    updateValues.put(BluetoothShare.CURRENT_BYTES, position);
                                    mContext1.getContentResolver().update(contentUri, updateValues,
                                            null, null);
                                    percentPosition = percentPosition % onePercent;
                                }
}
}
}








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppObexServerSession.java b/src/com/android/bluetooth/opp/BluetoothOppObexServerSession.java
//Synthetic comment -- index 3512fad..40a0cf2 100644

//Synthetic comment -- @@ -460,6 +460,10 @@
int readLength = 0;
long timestamp = 0;
try {
                ContentValues updateValues = new ContentValues();
                final int onePercent = Math.max((int)(fileInfo.mLength / 100), 1);
                int percentPosition = 0;

while ((!mInterrupted) && (position != fileInfo.mLength)) {

if (V) timestamp = System.currentTimeMillis();
//Synthetic comment -- @@ -473,6 +477,7 @@

bos.write(b, 0, readLength);
position += readLength;
                    percentPosition += readLength;

if (V) {
Log.v(TAG, "Receive file position = " + position + " readLength "
//Synthetic comment -- @@ -480,9 +485,12 @@
+ (System.currentTimeMillis() - timestamp) + " ms");
}

                    // Limit the number of update() calls to once per percent as it is expensive.
                    if (percentPosition >= onePercent) {
                        updateValues.put(BluetoothShare.CURRENT_BYTES, position);
                        mContext.getContentResolver().update(contentUri, updateValues, null, null);
                        percentPosition = percentPosition % onePercent;
                    }
}
} catch (IOException e1) {
Log.e(TAG, "Error when receiving file");







