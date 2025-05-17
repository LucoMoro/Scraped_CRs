//<Beginning of snippet n. 0>

/**
* Creates a Uri which parses the given encoded URI string.
*
* @param uriString an RFC 2396-compliant, encoded URI
* @throws NullPointerException if uriString is null
* @return Uri for this given uri string
* @deprecated RFC 3296 is for LDAP, and RFC 2396 pertains to URI parsing.
*/
public Uri createUri(String uriString) {
    if (uriString == null) {
        throw new NullPointerException("uriString cannot be null");
    }
    // Implementation of URI parsing goes here
    // ...
}

//<End of snippet n. 0>