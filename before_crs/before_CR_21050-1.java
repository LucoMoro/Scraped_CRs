/*Delete Flakey createSocket Test

Bug 3188260

Not sure how this could be improved...without deleting it.

Change-Id:I236b17fb8be63af7ff33e58bbf6c28fe3816bffd*/
//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/SSLCertificateSocketFactoryTest.java b/tests/tests/net/src/android/net/cts/SSLCertificateSocketFactoryTest.java
//Synthetic comment -- index 258ac4d..444dd96 100644

//Synthetic comment -- @@ -16,23 +16,21 @@

package android.net.cts;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.net.ssl.SSLPeerUnverifiedException;

import android.net.SSLCertificateSocketFactory;
import android.test.AndroidTestCase;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;

@TestTargetClass(SSLCertificateSocketFactory.class)
public class SSLCertificateSocketFactoryTest extends AndroidTestCase {
private SSLCertificateSocketFactory mFactory;
//Synthetic comment -- @@ -70,78 +68,6 @@
assertNotNull(sf);
}

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "createSocket",
            args = {java.net.InetAddress.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "createSocket",
            args = {java.net.Socket.class, java.lang.String.class, int.class, boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "createSocket",
            args = {java.lang.String.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.NOT_FEASIBLE,
            method = "createSocket",
            args = {java.lang.String.class, int.class, java.net.InetAddress.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "createSocket",
            args = {java.net.InetAddress.class, int.class, java.net.InetAddress.class, int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "SSLCertificateSocketFactory",
            args = {int.class}
        )
    })
    @BrokenTest("flaky")
    public void testCreateSocket() throws Exception {
        new SSLCertificateSocketFactory(100);
        int port = 443;
        String host = "www.fortify.net";
        InetAddress inetAddress = null;
        inetAddress = InetAddress.getLocalHost();
        try {
            mFactory.createSocket(inetAddress, port);
            fail("should throw exception!");
        } catch (IOException e) {
            // expected
        }

        try {
            InetAddress inetAddress1 = InetAddress.getLocalHost();
            InetAddress inetAddress2 = InetAddress.getLocalHost();
            mFactory.createSocket(inetAddress1, port, inetAddress2, port);
            fail("should throw exception!");
        } catch (IOException e) {
            // expected
        }

        try {
            Socket socket = new Socket();
            mFactory.createSocket(socket, host, port, true);
            fail("should throw exception!");
        } catch (IOException e) {
            // expected
        }
        Socket socket = null;
        socket = mFactory.createSocket(host, port);
        assertNotNull(socket);
        assertNotNull(socket.getOutputStream());
        assertNotNull(socket.getInputStream());

        // it throw exception when calling createSocket(String, int, InetAddress, int)
        // The socket level is invalid.
    }

// a host and port that are expected to be available but have
// a cert with a different CN, in this case CN=mtalk.google.com
private static String TEST_CREATE_SOCKET_HOST = "mobile-gtalk.l.google.com";







