//<Beginning of snippet n. 0>
filename = Constants.DEFAULT_DL_FILENAME;
}

filename = filename.replaceAll("[<>:\"/\\\\|?*]", "_");

if (filename.length() > 255) {
    throw new IllegalArgumentException("Filename exceeds the maximum length of 255 characters");
}

if (!isValidFilename(filename)) {
    throw new IllegalArgumentException("Filename contains unsafe characters: " + getUnsafeCharacters(filename));
}

return filename;

private boolean isValidFilename(String filename) {
    return !filename.matches(".*[<>:\"/\\\\|?*].*");
}

private String getUnsafeCharacters(String filename) {
    StringBuilder unsafeCharacters = new StringBuilder();
    for (char c : filename.toCharArray()) {
        if ("<>:\"/\\|?*".indexOf(c) >= 0) {
            unsafeCharacters.append(c);
        }
    }
    return unsafeCharacters.toString();
}
//<End of snippet n. 0>