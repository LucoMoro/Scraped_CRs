//<Beginning of snippet n. 0>
filename = Constants.DEFAULT_DL_FILENAME;

filename = filename.replaceAll("[^\\p{L}\\p{N}\\.\\-_]+", "_");

return filename;
//<End of snippet n. 0>