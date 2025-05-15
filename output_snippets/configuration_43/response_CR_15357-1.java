//<Beginning of snippet n. 0>
if (inUrl.endsWith(".")) {
    inUrl = inUrl.substring(0, inUrl.length() - 1);
}

try {
    webAddress = new WebAddress(inUrl);
//<End of snippet n. 0>