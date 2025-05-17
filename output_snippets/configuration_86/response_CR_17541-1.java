//<Beginning of snippet n. 0>

int index = line.indexOf('/');

if (index == -1)
    continue;

String tmpLib = line.substring(index);

if (tmpLib.matches("/system/lib/[^/]+\\.so$") && !tmpLib.equals("/system/framework/framework-res.apk")) {
    if (library == null || !tmpLib.equals(library)) {
        if (library != null) {
            cd.addNativeLibraryMapInfo(startAddr, endAddr, library);
        }

        library = tmpLib;
        // Removed tmpStart and tmpEnd logic
    }
}
// Removed logging of exception
}catch (NumberFormatException e) {
    Log.e("LibraryError", "An error occurred while processing the library");
}
//<End of snippet n. 0>