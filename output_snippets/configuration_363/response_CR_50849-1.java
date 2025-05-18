//<Beginning of snippet n. 0>
AdbHelper.write(mChannel, msg, -1, timeOut);

initializeBuffer();

while (true) {
    int readCount = -1;
    try {
        readCount = fis.read(getBuffer(), 8, SYNC_DATA_MAX);
    } catch (IOException e) {
        Log.e("ddms", "Read error: " + e.getMessage());
        // Handle persistent failures, consider retrying or alternative error flows here
        return FileListingService.TYPE_OTHER;
    }

    if (readCount == -1) {
        break; // End of file reached
    }

    ArrayHelper.swap32bitsToArray(readCount, mBuffer, 4);
    AdbHelper.write(mChannel, mBuffer, readCount + 8, timeOut);
    monitor.advance(readCount);
    
    int len = ArrayHelper.swap32bitFromArray(result, 4);
    if (len > 0) {
        AdbHelper.read(mChannel, mBuffer, len, timeOut);
        String message = new String(mBuffer, 0, len);
        Log.e("ddms", "transfer error: " + message);
        return message;
    }
}

return FileListingService.TYPE_OTHER;

private byte[] getBuffer() {
    initializeBuffer();
    return mBuffer;
}

private void initializeBuffer() {
    if (mBuffer == null) {
        mBuffer = new byte[SYNC_DATA_MAX + 8];
        System.arraycopy(ID_DATA, 0, mBuffer, 0, ID_DATA.length);
    }
}
//<End of snippet n. 0>