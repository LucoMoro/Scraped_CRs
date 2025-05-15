//<Beginning of snippet n. 0>
filename = Constants.DEFAULT_DL_FILENAME;

String unsafeCharacters = "[<>:\"/\\|?*]+"; // Define unsafe characters for VFAT
filename = filename.replaceAll(unsafeCharacters, "_");

// Unicode support: retain valid Unicode characters
StringBuilder sanitizedFilename = new StringBuilder();
for (char c : filename.toCharArray()) {
    if (Character.isLetterOrDigit(c) || c == '.' || c == '-' || c == '_') {
        sanitizedFilename.append(c);
    }
}

filename = sanitizedFilename.toString();

// Error handling for empty or underscore-only filename
if (filename.isEmpty() || filename.equals("_")) {
    filename = Constants.DEFAULT_DL_FILENAME; // Provide default filename
}

return filename;
//<End of snippet n. 0>