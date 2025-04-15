/*Fix NetworkInterface.getNetworkInterfaces /proc/net/if_inet6 parsing.

It turns out that some devices can have very large interface indexes,
and my code was incorrectly assuming they'd always fit in 8 bits.

Bug:http://code.google.com/p/android/issues/detail?id=34022Change-Id:I388a46ffe45f9706a4e28fb3b2975c991a74d419*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/net/NetworkInterface.java b/luni/src/main/java/java/net/NetworkInterface.java
//Synthetic comment -- index e06b811..ad81f32 100644

//Synthetic comment -- @@ -122,7 +122,9 @@

private static void collectIpv6Addresses(String interfaceName, int interfaceIndex,
List<InetAddress> addresses, List<InterfaceAddress> interfaceAddresses) throws SocketException {
        // Format of /proc/net/if_inet6.
        // All numeric fields are implicit hex,
        // but not necessarily two-digit (http://code.google.com/p/android/issues/detail?id=34022).
// 1. IPv6 address
// 2. interface index
// 3. prefix length
//Synthetic comment -- @@ -130,6 +132,7 @@
// 5. flags
// 6. interface name
// "00000000000000000000000000000001 01 80 10 80       lo"
        // "fe800000000000000000000000000000 407 40 20 80    wlan0"
BufferedReader in = null;
try {
in = new BufferedReader(new FileReader("/proc/net/if_inet6"));
//Synthetic comment -- @@ -139,13 +142,22 @@
if (!line.endsWith(suffix)) {
continue;
}

                // Extract the IPv6 address.
byte[] addressBytes = new byte[16];
for (int i = 0; i < addressBytes.length; ++i) {
addressBytes[i] = (byte) Integer.parseInt(line.substring(2*i, 2*i + 2), 16);
}

                // Extract the prefix length.
                // Skip the IPv6 address and its trailing space.
                int prefixLengthStart = 32 + 1;
                // Skip the interface index and its trailing space.
                prefixLengthStart = line.indexOf(' ', prefixLengthStart) + 1;
                int prefixLengthEnd = line.indexOf(' ', prefixLengthStart);
                short prefixLength = Short.parseShort(line.substring(prefixLengthStart, prefixLengthEnd), 16);

                Inet6Address inet6Address = new Inet6Address(addressBytes, null, interfaceIndex);
addresses.add(inet6Address);
interfaceAddresses.add(new InterfaceAddress(inet6Address, prefixLength));
}







