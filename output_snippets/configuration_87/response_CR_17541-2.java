//<Beginning of snippet n. 0>
try {
    int index = line.indexOf('/');
    if (index == -1) {
        continue;
    }

    String tmpLib = line.substring(index);
    if (!tmpLib.matches("/system/lib/.*\\.so") || tmpLib.equals("/system/framework/framework-res.apk")) {
        continue;
    }

    if (library == null || !tmpLib.equals(library)) {
        if (library != null) {
            // Removed the call to addNativeLibraryMapInfo as startAddr and endAddr are no longer needed
            Log.d("ddms", library);
        }

        library = tmpLib;
    }
} catch (NumberFormatException e) {
    Log.d("Error", "An error occurred while parsing the address.");
}
//<End of snippet n. 0>