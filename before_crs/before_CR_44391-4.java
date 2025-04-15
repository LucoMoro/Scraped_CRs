/*Expose requestRouteToHostAddress(int,InetAddress)

The exposed version of requestRouteToHostAddress(int, int) force apps to
manually convert InetAddress into an int, which is reverted back into
an InetAddressi internally. Additionally, int is not IPv6 friendly.

Fix public issuehttp://code.google.com/p/android/issues/detail?id=7376by exposing requestRouteToHost(int, InetAddress).

Change-Id:I44e47a26519b4c4722ea2f998aec497c0cea67d4*/
//Synthetic comment -- diff --git a/core/java/android/net/ConnectivityManager.java b/core/java/android/net/ConnectivityManager.java
//Synthetic comment -- index 6ff1a339..77fec54 100644

//Synthetic comment -- @@ -557,11 +557,12 @@
* Ensure that a network route exists to deliver traffic to the specified
* host via the specified network interface. An attempt to add a route that
* already exists is ignored, but treated as successful.
* @param networkType the type of the network over which traffic to the specified
* host is to be routed
* @param hostAddress the IP address of the host to which the route is desired
* @return {@code true} on success, {@code false} on failure
     * @hide
*/
public boolean requestRouteToHostAddress(int networkType, InetAddress hostAddress) {
byte[] address = hostAddress.getAddress();







