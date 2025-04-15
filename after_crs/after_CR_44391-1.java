/*Expose requestRouteToHost(int,InetAddress)

The exposed version of requestRouteToHost(int,int) forced apps to
manually convert InetAddress into an int, which is reverted back into
an InetAddress. Additionally, int is not IPv6 friendly.

Fix public issuehttp://code.google.com/p/android/issues/detail?id=7376Change-Id:I44e47a26519b4c4722ea2f998aec497c0cea67d4*/




//Synthetic comment -- diff --git a/core/java/android/net/ConnectivityManager.java b/core/java/android/net/ConnectivityManager.java
//Synthetic comment -- index 5f8793c..812b4b3 100644

//Synthetic comment -- @@ -503,6 +503,7 @@
* @param hostAddress the IP address of the host to which the route is desired
* @return {@code true} on success, {@code false} on failure
*/
    @Deprecated
public boolean requestRouteToHost(int networkType, int hostAddress) {
InetAddress inetAddress = NetworkUtils.intToInetAddress(hostAddress);

//Synthetic comment -- @@ -517,11 +518,12 @@
* Ensure that a network route exists to deliver traffic to the specified
* host via the specified network interface. An attempt to add a route that
* already exists is ignored, but treated as successful.
     * <p>This method requires the caller to hold the permission
     * {@link android.Manifest.permission#CHANGE_NETWORK_STATE}.
* @param networkType the type of the network over which traffic to the specified
* host is to be routed
* @param hostAddress the IP address of the host to which the route is desired
* @return {@code true} on success, {@code false} on failure
*/
public boolean requestRouteToHostAddress(int networkType, InetAddress hostAddress) {
byte[] address = hostAddress.getAddress();







