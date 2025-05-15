//<Beginning of snippet n. 0>
filename = Constants.DEFAULT_DL_FILENAME;
}

filename = filename.replaceAll("[^\\p{L}\\p{N}\\.\\-_]+", "_");

if (filename.length() > 255) {
    throw new IllegalArgumentException("Filename exceeds maximum length of 255 characters");
}

return filename;
}
//<End of snippet n. 0>