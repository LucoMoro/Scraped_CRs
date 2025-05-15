//<Beginning of snippet n. 0>
try (FileInputStream fis = new FileInputStream(file)) {
    AdbHelper.write(mChannel, msg, -1, timeOut);

    if (mBuffer == null) {
        mBuffer = new byte[SYNC_DATA_MAX + 8];
    }
    System.arraycopy(ID_DATA, 0, mBuffer, 0, ID_DATA.length);

    int readCount;
    while ((readCount = fis.read(mBuffer, 8, SYNC_DATA_MAX)) != -1) {
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
} catch (IOException e) {
    Log.e("ddms", "IOException occurred: " + e.getMessage(), e);
    return FileListingService.TYPE_OTHER;
}
//<End of snippet n. 0>