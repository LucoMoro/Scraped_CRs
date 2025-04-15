/*Fix NPE when pulling file.

If an error occurs when pulling a file, SyncService calls
readErrorMessage which accesses an uninitialized buffer.

This commit refactors SyncService to wrap all access to the
buffer in a lazy load get method.

Bug 6605801

Change-Id:Ib1a4d75c305e9990d12c379bdefc5d362c9ebac1*/




//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/SyncService.java b/ddmlib/src/main/java/com/android/ddmlib/SyncService.java
//Synthetic comment -- index 43d0452..cf91eb5 100644

//Synthetic comment -- @@ -646,12 +646,7 @@
// file and network IO exceptions.
AdbHelper.write(mChannel, msg, -1, timeOut);

            System.arraycopy(ID_DATA, 0, getBuffer(), 0, ID_DATA.length);

// look while there is something to read
while (true) {
//Synthetic comment -- @@ -661,7 +656,7 @@
}

// read up to SYNC_DATA_MAX
                int readCount = fis.read(getBuffer(), 8, SYNC_DATA_MAX);

if (readCount == -1) {
// we reached the end of the file
//Synthetic comment -- @@ -670,10 +665,10 @@

// now send the data to the device
// first write the amount read
                ArrayHelper.swap32bitsToArray(readCount, getBuffer(), 4);

// now write it
                AdbHelper.write(mChannel, getBuffer(), readCount+8, timeOut);

// and advance the monitor
monitor.advance(readCount);
//Synthetic comment -- @@ -719,9 +714,9 @@
int len = ArrayHelper.swap32bitFromArray(result, 4);

if (len > 0) {
                AdbHelper.read(mChannel, getBuffer(), len, timeOut);

                String message = new String(getBuffer(), 0, len);
Log.e("ddms", "transfer error: " + message);

return message;
//Synthetic comment -- @@ -880,4 +875,17 @@

return FileListingService.TYPE_OTHER;
}

    /**
     * Retrieve the buffer, allocating if necessary
     * @return
     */
    private byte[] getBuffer() {
        if (mBuffer == null) {
            // create the buffer used to read.
            // we read max SYNC_DATA_MAX, but we need 2 4 bytes at the beginning.
            mBuffer = new byte[SYNC_DATA_MAX + 8];
        }
        return mBuffer;
    }
}







