```
<Beginning of snippet n. 0>
try {
    int index = line.indexOf('/');

    if (index == -1)
        continue;

    String tmpLib = line.substring(index);

    if (tmpLib.matches("/system/lib/.*\\.so")) {
        library = tmpLib;
    }
} catch (NumberFormatException e) {
    // Log a generic error without sensitive information
    System.err.println("An error occurred while processing the line.");
}
<End of snippet n. 0>