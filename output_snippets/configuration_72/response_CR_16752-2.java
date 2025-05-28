//<Beginning of snippet n. 0>
filename = Constants.DEFAULT_DL_FILENAME;
}

String unsafeCharactersRegex = "[<>:\"/\\|?*\\x00-\\x1F]";
filename = filename.replaceAll(unsafeCharactersRegex, "_");

if (filename.length() > 255) {
    throw new IllegalArgumentException("Filename exceeds maximum length of 255 characters.");
}

if (filename.contains("..") || filename.contains("/") || filename.contains("\\") || filename.startsWith("/") || filename.startsWith("\\")) {
    throw new IllegalArgumentException("Filename contains unsafe characters or patterns: it must not contain '..', '/', '\\', or start with '/' or '\\'.");
}

return filename;
}
(c >= '0' && c <= '9');
//<End of snippet n. 0>