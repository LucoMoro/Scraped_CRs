//<Beginning of snippet n. 0>
byte[] mBuffer = null;

try (FileInputStream fis = new FileInputStream(filePath)) {
    initializeBuffer();
    AdbHelper.write(mChannel, msg, -1, timeOut);

    while (true) {
        int readCount = fis.read(mBuffer, 8, SYNC_DATA_MAX);
        if (readCount == -1) {
            break; // Exit loop on end of file
        }

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
    }
} catch (IOException e) {
    Log.e("ddms", "File read error: " + e.getMessage());
    if (mBuffer != null) {
        Log.e("ddms", "Buffer state: " + Arrays.toString(mBuffer));
    }
    mBuffer = null; // Reset buffer on error
    return e.getMessage(); // Notify caller of failure with error message
}

return FileListingService.TYPE_OTHER;

private void initializeBuffer() {
    if (mBuffer == null) {
        mBuffer = new byte[SYNC_DATA_MAX + 8];
        System.arraycopy(ID_DATA, 0, mBuffer, 0, ID_DATA.length);
    }
}
//<End of snippet n. 0>