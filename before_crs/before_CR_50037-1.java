/*Track new value of SSL_MODE_HANDSHAKE_CUTTHROUGH in external/openssl

Change-Id:Ie5cd6d4201ce2f361384eebe37487a3586321e8b*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/NativeCrypto.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/NativeCrypto.java
//Synthetic comment -- index 7ccc0ba..740a027 100644

//Synthetic comment -- @@ -479,7 +479,7 @@
public static final int RSA_NO_PADDING    = 3;

// SSL mode from ssl.h
    public static final long SSL_MODE_HANDSHAKE_CUTTHROUGH = 0x00000040L;

// SSL options from ssl.h
public static final long SSL_OP_NO_TICKET                              = 0x00004000L;







