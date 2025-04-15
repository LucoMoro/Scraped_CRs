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
//Synthetic comment -- index 6efac93..521c46a 100644

//Synthetic comment -- @@ -432,6 +432,10 @@
}
}

while (!mInterrupted && okToProceed && (position != fileInfo.mLength)) {
{
if (V) timestamp = System.currentTimeMillis();
//Synthetic comment -- @@ -448,15 +452,21 @@
okToProceed = false;
} else {
position += readLength;
if (V) {
Log.v(TAG, "Sending file position = " + position
+ " readLength " + readLength + " bytes took "
+ (System.currentTimeMillis() - timestamp) + " ms");
}
                                updateValues = new ContentValues();
                                updateValues.put(BluetoothShare.CURRENT_BYTES, position);
                                mContext1.getContentResolver().update(contentUri, updateValues,
                                        null, null);
}
}
}








//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppObexServerSession.java b/src/com/android/bluetooth/opp/BluetoothOppObexServerSession.java
//Synthetic comment -- index 278d29e..c48e90a 100644

//Synthetic comment -- @@ -444,6 +444,10 @@
int readLength = 0;
long timestamp = 0;
try {
while ((!mInterrupted) && (position != fileInfo.mLength)) {

if (V) timestamp = System.currentTimeMillis();
//Synthetic comment -- @@ -457,6 +461,7 @@

bos.write(b, 0, readLength);
position += readLength;

if (V) {
Log.v(TAG, "Receive file position = " + position + " readLength "
//Synthetic comment -- @@ -464,9 +469,12 @@
+ (System.currentTimeMillis() - timestamp) + " ms");
}

                    ContentValues updateValues = new ContentValues();
                    updateValues.put(BluetoothShare.CURRENT_BYTES, position);
                    mContext.getContentResolver().update(contentUri, updateValues, null, null);
}
} catch (IOException e1) {
Log.e(TAG, "Error when receiving file");







