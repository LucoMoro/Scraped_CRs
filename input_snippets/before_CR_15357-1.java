
//<Beginning of snippet n. 0>


if (inUrl.endsWith(".") == true) {
inUrl = inUrl.substring(0, inUrl.length() - 1);
}
        
        // bug 2337042 strip periop off beginning of url
        if (inUrl.startsWith(".")) {
            inUrl = inUrl.substring(1, inUrl.length());
        }
        if (inUrl.startsWith("/.")) {
            inUrl = inUrl.substring(2, inUrl.length());
        }

try {
webAddress = new WebAddress(inUrl);

//<End of snippet n. 0>








