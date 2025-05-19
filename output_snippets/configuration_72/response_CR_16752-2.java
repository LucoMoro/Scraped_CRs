//<Beginning of snippet n. 0>
filename = Constants.DEFAULT_DL_FILENAME;
}

String unsafeCharactersRegex = "[<>:\"/\\\\|?*\\x00-\\x1F]|[\\u0000-\\u001F]|[\\u007F-\\u009F]";
filename = filename.replaceAll(unsafeCharactersRegex, "_");

int maxFilenameLength = 255; // Maximum length can be set based on filesystem settings
if (filename.length() > maxFilenameLength) {
    throw new IllegalArgumentException("Filename exceeds maximum length of " + maxFilenameLength + " characters.");
}

if (filename.contains("..") || filename.contains("/") || filename.contains("\\") || filename.startsWith("/") || filename.startsWith("\\")) {
    throw new IllegalArgumentException("Filename contains unsafe characters or patterns: it must not contain '..', '/', '\\', or start with '/' or '\\'.");
}

return filename;
}
(c >= '0' && c <= '9');
//<End of snippet n. 0>