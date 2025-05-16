//<Beginning of snippet n. 0>


// file and network IO exceptions.
AdbHelper.write(mChannel, msg, -1, timeOut);

            // create the buffer used to read.
            // we read max SYNC_DATA_MAX, but we need 2 4 bytes at the beginning.
            initializeBuffer();

            System.arraycopy(ID_DATA, 0, mBuffer, 0, ID_DATA.length);

// look while there is something to read
while (true) {
}

// read up to SYNC_DATA_MAX
                int readCount = 0;
                try {
                    readCount = fis.read(mBuffer, 8, SYNC_DATA_MAX);
                } catch (IOException e) {
                    Log.e("ddms", "Error reading from file: " + e.getMessage());
                    return FileListingService.TYPE_OTHER;
                }

if (readCount == -1) {
// we reached the end of the file

// now send the data to the device
// first write the amount read
                ArrayHelper.swap32bitsToArray(readCount, mBuffer, 4);

// now write it
                AdbHelper.write(mChannel, mBuffer, readCount + 8, timeOut);

// and advance the monitor
                if (readCount > 0) {
                    monitor.advance(readCount);
                }
                
                int len = ArrayHelper.swap32bitFromArray(result, 4);

if (len > 0) {
                AdbHelper.read(mChannel, mBuffer, len, timeOut);

                String message = new String(mBuffer, 0, len);
                Log.e("ddms", "transfer error: " + message);

                return message;
}

return FileListingService.TYPE_OTHER;
}

private void initializeBuffer() {
    if (mBuffer == null) {
        mBuffer = new byte[SYNC_DATA_MAX + 8];
    }
}

//<End of snippet n. 0>