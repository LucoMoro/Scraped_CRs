/*Delete Flakey createSocket Test

Bug 3188260

Not sure how this could be improved...without deleting it.

Change-Id:I236b17fb8be63af7ff33e58bbf6c28fe3816bffd*/




//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/SSLCertificateSocketFactoryTest.java b/tests/tests/net/src/android/net/cts/SSLCertificateSocketFactoryTest.java
//Synthetic comment -- index 258ac4d..444dd96 100644

//Synthetic comment -- @@ -16,23 +16,21 @@

package android.net.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;

import android.net.SSLCertificateSocketFactory;
import android.test.AndroidTestCase;

import java.net.InetAddress;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.net.ssl.SSLPeerUnverifiedException;

@TestTargetClass(SSLCertificateSocketFactory.class)
public class SSLCertificateSocketFactoryTest extends AndroidTestCase {
private SSLCertificateSocketFactory mFactory;
//Synthetic comment -- @@ -70,78 +68,6 @@
assertNotNull(sf);
}

// a host and port that are expected to be available but have
// a cert with a different CN, in this case CN=mtalk.google.com
private static String TEST_CREATE_SOCKET_HOST = "mobile-gtalk.l.google.com";







