
//<Beginning of snippet n. 0>


* loadDataWithBaseURL()} with an appropriate base URL.
* <p>
* If the value of the encoding parameter is 'base64', then the data must
     * be encoded as base64 US-ASCII data.
     * If the value of the encoding parameter is a {@code charset} (for example
     * 'utf-8'), then the data must be encoded as {@code charset} data.
     * If the value of the encoding parameter is 'charset;base64', then the
     * data must be encoded as base64 {@code charset} data.
     * <p>
     * If the value of encoding parameter is null, then the data
     * must use US-ASCII encoding for
* octets inside the range of safe URL characters and use the standard %xx
* hex encoding of URLs for octets outside that range. For example,
* '#', '%', '\', '?' should be replaced by %23, %25, %27, %3f respectively.
* The 'data' scheme URL formed by this method uses the default US-ASCII
     * charset.
     * <p>
* Note that the charset obtained from the mediatype portion of a data URL
* always overrides that specified in the HTML or XML document itself.
*

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


dataUrl.append(mimeType);
if ("base64".equals(encoding)) {
dataUrl.append(";base64");
        } else if (encoding != null) {
            dataUrl.append(";charset=");
            dataUrl.append(encoding);
}
dataUrl.append(",");
dataUrl.append(data);

//<End of snippet n. 1>








