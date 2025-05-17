//<Beginning of snippet n. 0>
AdbHelper.write(mChannel, msg, -1, timeOut);

getBuffer();
System.arraycopy(ID_DATA, 0, mBuffer, 0, ID_DATA.length);

int readCount = -1;
try {
    readCount = fis.read(mBuffer, 8, SYNC_DATA_MAX);
} catch (IOException e) {
    Log.e("ddms", "File read error", e);
    return "File read error occurred";
}

if (readCount == -1) {
    ArrayHelper.swap32bitsToArray(readCount, mBuffer, 4);
    AdbHelper.write(mChannel, mBuffer, readCount + 8, timeOut);
    monitor.advance(readCount);
    int len = ArrayHelper.swap32bitFromArray(mBuffer, 4);

    if (len > 0) {
        AdbHelper.read(mChannel, mBuffer, len, timeOut);
        String message = new String(mBuffer, 0, len);
        Log.e("ddms", "transfer error: " + message);
        return message;
    }
    return FileListingService.TYPE_OTHER;
}

private byte[] getBuffer() {
    if (mBuffer == null) {
        mBuffer = new byte[SYNC_DATA_MAX + 8];
    }
    return mBuffer;
}
//<End of snippet n. 0>