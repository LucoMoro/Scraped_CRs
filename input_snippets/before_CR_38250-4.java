
//<Beginning of snippet n. 0>


* loadDataWithBaseURL()} with an appropriate base URL.
* <p>
* If the value of the encoding parameter is 'base64', then the data must
     * be encoded as base64. Otherwise, the data must use ASCII encoding for
* octets inside the range of safe URL characters and use the standard %xx
* hex encoding of URLs for octets outside that range. For example,
* '#', '%', '\', '?' should be replaced by %23, %25, %27, %3f respectively.
     * <p>
* The 'data' scheme URL formed by this method uses the default US-ASCII
     * charset. If you need need to set a different charset, you should form a
     * 'data' scheme URL which explicitly specifies a charset parameter in the
     * mediatype portion of the URL and call {@link #loadUrl(String)} instead.
* Note that the charset obtained from the mediatype portion of a data URL
* always overrides that specified in the HTML or XML document itself.
*

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


dataUrl.append(mimeType);
if ("base64".equals(encoding)) {
dataUrl.append(";base64");
}
dataUrl.append(",");
dataUrl.append(data);

//<End of snippet n. 1>








