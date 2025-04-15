/*NativeCrypto: fix Channel ID tests

The NativeCryptoTest runs from a different class loader, so we need to
make the OpenSSLECGroupContext public to use it from there.

Also make sure we explicitly initialize the EC key at the beginning of
the test.

Change-Id:I733fe6263ef2ef72988987bf608cb806752033f5*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECGroupContext.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLECGroupContext.java
//Synthetic comment -- index 3d9e138..0ca4272 100644

//Synthetic comment -- @@ -26,10 +26,10 @@
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;

public final class OpenSSLECGroupContext {
private final int groupCtx;

    public OpenSSLECGroupContext(int groupCtx) {
this.groupCtx = groupCtx;
}









//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/xnet/provider/jsse/NativeCryptoTest.java b/luni/src/test/java/org/apache/harmony/xnet/provider/jsse/NativeCryptoTest.java
//Synthetic comment -- index 1502520..de73ea1 100644

//Synthetic comment -- @@ -1045,6 +1045,8 @@
}

public void test_SSL_do_handshake_with_channel_id_normal() throws Exception {
        initChannelIdKey();

// Normal handshake with TLS Channel ID.
final ServerSocket listener = new ServerSocket(0);
Hooks cHooks = new Hooks();
//Synthetic comment -- @@ -1069,6 +1071,8 @@
}

public void test_SSL_do_handshake_with_channel_id_not_supported_by_server() throws Exception {
        initChannelIdKey();

// Client tries to use TLS Channel ID but the server does not enable/offer the extension.
final ServerSocket listener = new ServerSocket(0);
Hooks cHooks = new Hooks();
//Synthetic comment -- @@ -1093,6 +1097,8 @@
}

public void test_SSL_do_handshake_with_channel_id_not_enabled_by_client() throws Exception {
        initChannelIdKey();

// Client does not use TLS Channel ID when the server has the extension enabled/offered.
final ServerSocket listener = new ServerSocket(0);
Hooks cHooks = new Hooks();







