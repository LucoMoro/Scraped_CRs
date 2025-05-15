//<Beginning of snippet n. 0>

// file and network IO exceptions.
AdbHelper.write(mChannel, msg, -1, timeOut);

// create the buffer used to read.
// we read max SYNC_DATA_MAX, but we need 2 4 bytes at the beginning.
initializeBuffer();

// look while there is something to read
while (true) {
    // read up to SYNC_DATA_MAX
    int readCount = fis.read(mBuffer, 8, SYNC_DATA_MAX);

    if (readCount == -1) {
        // we reached the end of the file
        break;
    } else if (readCount < 0) {
        // handle error reading
        return "Error reading data.";
    }

    // now send the data to the device
    // first write the amount read
    ArrayHelper.swap32bitsToArray(readCount, mBuffer, 4);

    // now write it
    AdbHelper.write(mChannel, mBuffer, readCount + 8, timeOut);

    // and advance the monitor
    monitor.advance(readCount);
    int len = ArrayHelper.swap32bitFromArray(mBuffer, 4);

    if (len > 0) {
        AdbHelper.read(mChannel, mBuffer, len, timeOut);

        String message = new String(mBuffer, 0, len);
        Log.e("ddms", "transfer error: " + message);

        return message;
    }
}

return FileListingService.TYPE_OTHER;

private void initializeBuffer() {
    if (mBuffer == null) {
        mBuffer = new byte[SYNC_DATA_MAX + 8];
    }
    System.arraycopy(ID_DATA, 0, mBuffer, 0, ID_DATA.length);
}

//<End of snippet n. 0>