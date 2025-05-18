//<Beginning of snippet n. 0>

String line; // Assuming line is initialized elsewhere
String library = null;

try {
    int index = line.indexOf('/');

    if (index == -1) {
        return;
    }

    String tmpLib = line.substring(index);

    if (!tmpLib.matches("/system/lib/.*\\.so")) {
        return;
    }

    if (library == null || !tmpLib.equals(library)) {
        library = tmpLib;
    } 
} catch (NumberFormatException e) {
    Log.e("Error", "An error occurred.");

//<End of snippet n. 0>