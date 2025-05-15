//<Beginning of snippet n. 0>

// file and network IO exceptions.
AdbHelper.write(mChannel, msg, -1, timeOut);

// create the buffer used to read.
if (mBuffer == null) {
    mBuffer = new byte[SYNC_DATA_MAX + 8];
}
System.arraycopy(ID_DATA, 0, mBuffer, 0, ID_DATA.length);

// look while there is something to read
while (true) {
    int readCount = -1;

    try {
        // read up to SYNC_DATA_MAX
        readCount = fis.read(mBuffer, 8, SYNC_DATA_MAX);
    } catch (IOException e) {
        // Handle the IOException, log it or rethrow as necessary
        Log.e("FileReadError", "IOException while reading file: " + e.getMessage());
        return FileListingService.TYPE_OTHER;
    }

    if (readCount == -1) {
        // we reached the end of the file
        break; // Exit loop if end of file is reached
    }

    // now send the data to the device
    ArrayHelper.swap32bitsToArray(readCount, mBuffer, 4);

    // now write it
    AdbHelper.write(mChannel, mBuffer, readCount + 8, timeOut);

    // and advance the monitor
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

//<End of snippet n. 0>