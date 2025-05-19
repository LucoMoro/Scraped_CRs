```
<Beginning of snippet n. 0>
try {
    int index = line.indexOf('/');

    if (index == -1) {
        return;
    }

    String tmpLib = line.substring(index);
    String library = null; // Assuming library variable initialization

    if (!tmpLib.matches("/system/lib/.*\\.so")) {
        library = tmpLib;
    }
} catch (NumberFormatException e) {
    // Secure logging mechanism here
}
<End of snippet n. 0>