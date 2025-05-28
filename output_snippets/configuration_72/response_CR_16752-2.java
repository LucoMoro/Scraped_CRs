//<Beginning of snippet n. 0>
filename = Constants.DEFAULT_DL_FILENAME;
}

filename = filename.replaceAll("[<>:\"/\\|?*]", "_");

if (filename.length() > 255) {
    throw new IllegalArgumentException("Filename exceeds the maximum length of 255 characters");
}

if (containsUnsafeCharacters(filename)) {
    throw new IllegalArgumentException("Filename contains unsafe characters");
}

return filename;

private boolean containsUnsafeCharacters(String filename) {
    return filename.matches(".*[<>:\"/\\|?*].*");
}
//<End of snippet n. 0>