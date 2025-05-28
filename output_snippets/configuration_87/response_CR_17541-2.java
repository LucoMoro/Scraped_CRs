```
<Beginning of snippet n. 0>
try {
    int index = line.indexOf('/');

    if (index == -1) {
        return;
    }

    String tmpLib = line.substring(index);
    String library = null; // Assuming library variable initialization

    if (library == null || !tmpLib.matches("/system/lib/.*\\.so")) {
        library = tmpLib;
    }
} catch (NumberFormatException e) {
    // log a generic error without exposing sensitive information
}
<End of snippet n. 0>