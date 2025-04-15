/*Fix reference to setEnabledCompressionMethods

Change-Id:Ia8a6b5b2d3cba15ff82b5d13f7efcba86d22630e*/
//Synthetic comment -- diff --git a/android/main/java/libcore/util/Libcore.java b/android/main/java/libcore/util/Libcore.java
//Synthetic comment -- index cd32c17..d6d89d0 100644

//Synthetic comment -- @@ -46,7 +46,7 @@
}

private static final Class<?> openSslSocketClass;
    private static final Method setEnabledCompressionMethods;
private static final Method setUseSessionTickets;
private static final Method setHostname;
private static final Method setNpnProtocols;
//Synthetic comment -- @@ -57,8 +57,6 @@
try {
openSslSocketClass = Class.forName(
"org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl");
            setEnabledCompressionMethods = openSslSocketClass.getMethod(
                    "setEnabledCompressionMethods", String[].class);
setUseSessionTickets = openSslSocketClass.getMethod(
"setUseSessionTickets", boolean.class);
setHostname = openSslSocketClass.getMethod("setHostname", String.class);
//Synthetic comment -- @@ -71,6 +69,14 @@
} catch (NoSuchMethodException nsme) {
throw new RuntimeException(nsme);
}
}

public static DeflaterOutputStream newDeflaterOutputStream(
//Synthetic comment -- @@ -94,9 +100,11 @@

if (openSslSocketClass.isInstance(socket)) {
try {
                String[] compressionMethods = {"ZLIB"};
                setEnabledCompressionMethods.invoke(socket,
                        new Object[] {compressionMethods});
setUseSessionTickets.invoke(socket, true);
setHostname.invoke(socket, socketHost);
} catch (InvocationTargetException e) {







