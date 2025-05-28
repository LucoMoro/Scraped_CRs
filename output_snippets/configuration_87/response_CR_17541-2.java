//<Beginning of snippet n. 0>

String line; // Assuming line is initialized elsewhere
String library = null;

try {
    int index = line.indexOf('/');

    if (index == -1) {
        return; // Break the flow instead of using continue
    }

    String tmpLib = line.substring(index);

    if (!tmpLib.matches("/system/lib/.*\\.so")) {
        return; // Break the flow instead of using continue
    }

    if (library == null || !tmpLib.equals(library)) {
        if (library != null) {
            // Address range logic removed
        }

        // now init the new library
        library = tmpLib;
    } 
} catch (NumberFormatException e) {
    Log.e("Error", "An error occurred while parsing.");

//<End of snippet n. 0>