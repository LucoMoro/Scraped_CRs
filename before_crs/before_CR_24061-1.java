/*SmtpSender: Send EHLO that adheres to RFC2821

Adhere to RFC2821 Sec. 4.1.3 Address Literals should be enclosed by brackets and
prefixed with 'IPv6:' if the sender is an IPv6 host*/
//Synthetic comment -- diff --git a/src/com/android/email/mail/transport/SmtpSender.java b/src/com/android/email/mail/transport/SmtpSender.java
//Synthetic comment -- index 8f72631..af6b282 100644

//Synthetic comment -- @@ -31,7 +31,9 @@
import android.util.Base64;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;

//Synthetic comment -- @@ -123,7 +125,18 @@
// Try to get local address in the X.X.X.X format.
InetAddress localAddress = mTransport.getLocalAddress();
if (localAddress != null) {
                localHost = localAddress.getHostAddress();
}
String result = executeSimpleCommand("EHLO " + localHost);








