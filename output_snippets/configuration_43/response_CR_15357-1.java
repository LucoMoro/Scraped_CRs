//<Beginning of snippet n. 0>


if (inUrl.endsWith(".") == true) {
    inUrl = inUrl.substring(0, inUrl.length() - 1);
}

// Reverted URL handling logic for bug 2337042
try {
    webAddress = new WebAddress(inUrl);

//<End of snippet n. 0>