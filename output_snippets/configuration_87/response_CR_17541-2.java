//<Beginning of snippet n. 0>
try {
    long tmpStart = Long.parseLong(line.substring(0, 8), 16);
    long tmpEnd = Long.parseLong(line.substring(9, 17), 16);

    int index = line.indexOf('/');

    if (index == -1)
        continue;

    String tmpLib = line.substring(index);

    if (tmpLib.matches("/system/lib/.*\\.so") && (library == null || !tmpLib.equals(library))) {
        if (library != null) {
            cd.addNativeLibraryMapInfo(startAddr, endAddr, library);
            if (BuildConfig.DEBUG) {
                Log.d("ddms", library + "(" + Long.toHexString(startAddr) + " - " + Long.toHexString(endAddr) + ")");
            }
        }

        library = tmpLib;
        startAddr = tmpStart;
        endAddr = tmpEnd;
    } else {
        endAddr = tmpEnd;
    }
} catch (NumberFormatException e) {
    Log.e("Error", "Invalid number format", e);
//<End of snippet n. 0>