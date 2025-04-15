/*Make addAddress locale safe

Explicitly use Locale.US in addAddress to avoid
unexpected results in some locales.

Change-Id:I47dd5e174c4a2e88dc18e014002820cdbf63fcad*/




//Synthetic comment -- diff --git a/core/java/android/net/VpnService.java b/core/java/android/net/VpnService.java
//Synthetic comment -- index fb5263d..89a285a 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;

/**
* VpnService is a base class for applications to extend and build their
//Synthetic comment -- @@ -329,7 +330,8 @@
throw new IllegalArgumentException("Bad address");
}

            mAddresses.append(String.format(Locale.US, " %s/%d", address.getHostAddress(),
                    prefixLength));
return this;
}








