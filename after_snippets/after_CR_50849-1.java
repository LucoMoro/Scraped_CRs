
//<Beginning of snippet n. 0>


// file and network IO exceptions.
AdbHelper.write(mChannel, msg, -1, timeOut);

            System.arraycopy(ID_DATA, 0, getBuffer(), 0, ID_DATA.length);

// look while there is something to read
while (true) {
}

// read up to SYNC_DATA_MAX
                int readCount = fis.read(getBuffer(), 8, SYNC_DATA_MAX);

if (readCount == -1) {
// we reached the end of the file

// now send the data to the device
// first write the amount read
                ArrayHelper.swap32bitsToArray(readCount, getBuffer(), 4);

// now write it
                AdbHelper.write(mChannel, getBuffer(), readCount+8, timeOut);

// and advance the monitor
monitor.advance(readCount);
int len = ArrayHelper.swap32bitFromArray(result, 4);

if (len > 0) {
                AdbHelper.read(mChannel, getBuffer(), len, timeOut);

                String message = new String(getBuffer(), 0, len);
Log.e("ddms", "transfer error: " + message);

return message;

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

//<End of snippet n. 0>








