/*Make transactGetSockaddr locale safe

Explicitly use Locale.US transactGetSockaddr to avoid
unexpected results in some locales.

Change-Id:If8316e543e21e133dc0c821cc390a2b55fe6fcdf*/




//Synthetic comment -- diff --git a/core/java/android/os/CommonTimeUtils.java b/core/java/android/os/CommonTimeUtils.java
//Synthetic comment -- index 9081ee4..20755d9 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.util.Locale;
import static libcore.io.OsConstants.*;

class CommonTimeUtils {
//Synthetic comment -- @@ -192,10 +193,11 @@
if (AF_INET == type) {
int addr = reply.readInt();
port = reply.readInt();
                    addrStr = String.format(Locale.US, "%d.%d.%d.%d",
                                                       (addr >> 24) & 0xFF,
                                                       (addr >> 16) & 0xFF,
                                                       (addr >>  8) & 0xFF,
                                                        addr        & 0xFF);
} else if (AF_INET6 == type) {
int addr1 = reply.readInt();
int addr2 = reply.readInt();
//Synthetic comment -- @@ -207,11 +209,11 @@
int flowinfo = reply.readInt();
int scope_id = reply.readInt();

                    addrStr = String.format(Locale.US, "[%04X:%04X:%04X:%04X:%04X:%04X:%04X:%04X]",
                                                       (addr1 >> 16) & 0xFFFF, addr1 & 0xFFFF,
                                                       (addr2 >> 16) & 0xFFFF, addr2 & 0xFFFF,
                                                       (addr3 >> 16) & 0xFFFF, addr3 & 0xFFFF,
                                                       (addr4 >> 16) & 0xFFFF, addr4 & 0xFFFF);
}

if (null != addrStr) {







