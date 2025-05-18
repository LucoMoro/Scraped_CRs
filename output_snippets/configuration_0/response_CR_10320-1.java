//<Beginning of snippet n. 0>


/**
* Creates a Uri which parses the given encoded URI string.
*
* @param uriString an RFC 3296-compliant, encoded URI
* @throws NullPointerException if uriString is null
* @return Uri for this given uri string
*/
public Uri createUri(String uriString) {
    if (uriString == null) {
        throw new NullPointerException("uriString cannot be null");
    }
    return Uri.parse(uriString);
}

//<End of snippet n. 0>