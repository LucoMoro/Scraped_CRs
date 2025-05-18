//<Beginning of snippet n. 0>

int index = line.indexOf('/');

if (index == -1)
    continue;

String tmpLib = line.substring(index);

if (tmpLib.matches("/system/lib/.*\\.so")) {
    if (library == null || !tmpLib.equals(library)) {
        if (library != null) {
            // startAddr and endAddr references have been removed
            cd.addNativeLibraryMapInfo(library);
        }

        library = tmpLib;
    } else {
        // add the new end
        endAddr = Long.parseLong(line.substring(9, 17), 16); // Assuming this is the new end
    }
}
} catch (NumberFormatException e) {
    Log.e("Error", "Number format exception occurred.");
//<End of snippet n. 0>