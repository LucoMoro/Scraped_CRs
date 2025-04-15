/*Remove usage of setEnabledCompressionMethods

Change-Id:Ia8a6b5b2d3cba15ff82b5d13f7efcba86d22630e*/




//Synthetic comment -- diff --git a/android/main/java/libcore/util/Libcore.java b/android/main/java/libcore/util/Libcore.java
//Synthetic comment -- index cd32c17..88aeeaa 100644

//Synthetic comment -- @@ -46,7 +46,6 @@
}

private static final Class<?> openSslSocketClass;
private static final Method setUseSessionTickets;
private static final Method setHostname;
private static final Method setNpnProtocols;
//Synthetic comment -- @@ -57,8 +56,6 @@
try {
openSslSocketClass = Class.forName(
"org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl");
setUseSessionTickets = openSslSocketClass.getMethod(
"setUseSessionTickets", boolean.class);
setHostname = openSslSocketClass.getMethod("setHostname", String.class);
//Synthetic comment -- @@ -94,9 +91,6 @@

if (openSslSocketClass.isInstance(socket)) {
try {
setUseSessionTickets.invoke(socket, true);
setHostname.invoke(socket, socketHost);
} catch (InvocationTargetException e) {







