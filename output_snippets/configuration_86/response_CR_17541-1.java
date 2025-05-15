//<Beginning of snippet n. 0>

long tmpStart = Long.parseLong(line.substring(0, 8), 16);
long tmpEnd = Long.parseLong(line.substring(9, 17), 16);

/*
 * only check for library addresses as defined in
 * //device/config/prelink-linux-arm.map
 */

int index = line.indexOf('/');

if (index == -1)
    continue;

String tmpLib = line.substring(index);

if (tmpLib.matches("^/system/lib/.*\\.so$")) {
    if (library == null ||
            (library != null && !tmpLib.equals(library))) {

        if (library != null) {
            cd.addNativeLibraryMapInfo(startAddr, endAddr, library);
            Log.d("ddms", library + "(" + Long.toHexString(startAddr) +
                    " - " + Long.toHexString(endAddr) + ")");
        }

        // now init the new library
        library = tmpLib;
        startAddr = tmpStart;
        endAddr = tmpEnd;
    } else {
        // add the new end
        endAddr = tmpEnd;
    }
}
} catch (NumberFormatException e) {
    e.printStackTrace();

//<End of snippet n. 0>