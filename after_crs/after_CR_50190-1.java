/*Add TLS Channel ID support to SSLCertificateSocketFactory.

This adds a new method setChannelIdPrivateKey as a hidden API.

Seehttp://tools.ietf.org/html/draft-balfanz-tls-channelid-00for
more information about the TLS Channel ID extension.

Change-Id:I73c1905afdce01d4831de7faa55ea4496575b5a5*/




//Synthetic comment -- diff --git a/core/java/android/net/SSLCertificateSocketFactory.java b/core/java/android/net/SSLCertificateSocketFactory.java
//Synthetic comment -- index 846443d..c0a894b 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
//Synthetic comment -- @@ -88,6 +89,7 @@
private TrustManager[] mTrustManagers = null;
private KeyManager[] mKeyManagers = null;
private byte[] mNpnProtocols = null;
    private ECPrivateKey mChannelIdPrivateKey = null;

private final int mHandshakeTimeoutMillis;
private final SSLClientSessionCache mSessionCache;
//Synthetic comment -- @@ -319,6 +321,20 @@
}

/**
     * Sets the {@link ECPrivateKey} to be used for TLS Channel ID by connections made by this
     * factory.
     *
     * @param privateKey private key (enables TLS Channel ID) or {@code null} for no key (disables
     *        TLS Channel ID). The private key has to be an Elliptic Curve (EC) key based on the
     *        NIST P-256 curve (aka SECG secp256r1 or ANSI X9.62 prime256v1).
     *
     * @hide
     */
    public void setChannelIdPrivateKey(ECPrivateKey privateKey) {
        mChannelIdPrivateKey = privateKey;
    }

    /**
* Enables <a href="http://tools.ietf.org/html/rfc5077#section-3.2">session ticket</a>
* support on the given socket.
*
//Synthetic comment -- @@ -378,6 +394,7 @@
OpenSSLSocketImpl s = (OpenSSLSocketImpl) getDelegate().createSocket(k, host, port, close);
s.setNpnProtocols(mNpnProtocols);
s.setHandshakeTimeout(mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(mChannelIdPrivateKey);
if (mSecure) {
verifyHostname(s, host);
}
//Synthetic comment -- @@ -397,6 +414,7 @@
OpenSSLSocketImpl s = (OpenSSLSocketImpl) getDelegate().createSocket();
s.setNpnProtocols(mNpnProtocols);
s.setHandshakeTimeout(mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(mChannelIdPrivateKey);
return s;
}

//Synthetic comment -- @@ -414,6 +432,7 @@
addr, port, localAddr, localPort);
s.setNpnProtocols(mNpnProtocols);
s.setHandshakeTimeout(mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(mChannelIdPrivateKey);
return s;
}

//Synthetic comment -- @@ -429,6 +448,7 @@
OpenSSLSocketImpl s = (OpenSSLSocketImpl) getDelegate().createSocket(addr, port);
s.setNpnProtocols(mNpnProtocols);
s.setHandshakeTimeout(mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(mChannelIdPrivateKey);
return s;
}

//Synthetic comment -- @@ -445,6 +465,7 @@
host, port, localAddr, localPort);
s.setNpnProtocols(mNpnProtocols);
s.setHandshakeTimeout(mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(mChannelIdPrivateKey);
if (mSecure) {
verifyHostname(s, host);
}
//Synthetic comment -- @@ -462,6 +483,7 @@
OpenSSLSocketImpl s = (OpenSSLSocketImpl) getDelegate().createSocket(host, port);
s.setNpnProtocols(mNpnProtocols);
s.setHandshakeTimeout(mHandshakeTimeoutMillis);
        s.setChannelIdPrivateKey(mChannelIdPrivateKey);
if (mSecure) {
verifyHostname(s, host);
}







