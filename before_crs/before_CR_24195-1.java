/*Thread safety in InetAddress

Make InetAddress class thread safe in a multicore system
by making all native calls synchronized

Change-Id:Ic5df1c04dae2f0101ec5c799211d4a62533afcaeSigned-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/net/InetAddress.java b/luni/src/main/java/java/net/InetAddress.java
//Synthetic comment -- index 2f283bb..7d10bc9 100644

//Synthetic comment -- @@ -294,9 +294,9 @@
return lookupHostByName(host);
}

    private static native String byteArrayToIpString(byte[] address);

    static native byte[] ipStringToByteArray(String address) throws UnknownHostException;

private static String wrongAddressLength() {
return "Invalid IP Address is neither 4 or 16 bytes";
//Synthetic comment -- @@ -455,7 +455,7 @@
}
return lookupHostByName(host)[0];
}
    private static native String gethostname();

/**
* Gets the hashcode of the represented IP address.
//Synthetic comment -- @@ -506,7 +506,7 @@
throw new UnknownHostException(host);
}
}
    private static native byte[][] getaddrinfo(String name) throws UnknownHostException;

/**
* Query the IP stack for the host address. The host is in address form.
//Synthetic comment -- @@ -531,7 +531,7 @@
/**
* Resolves an IP address to a hostname. Thread safe.
*/
    private static native String getnameinfo(byte[] addr);

static String getHostNameInternal(String host, boolean isCheck) throws UnknownHostException {
if (host == null || 0 == host.length()) {







