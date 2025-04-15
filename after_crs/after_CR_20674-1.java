/*Prevent removing default route for default mobile type

The default route for mobile would be accidently removed
but never restored in case of a temporary disconnect.

This solution solves several issues in UMTS and CDMA.

Change-Id:Ib16878bdb26780bf328a696f0820f92a6b7ea102*/




//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
//Synthetic comment -- index 7c9b547..9caa150 100644

//Synthetic comment -- @@ -1322,6 +1322,17 @@
*/
handleDnsConfigurationChange(netType);

        if (mNetAttributes[netType].isDefault() &&
            mNetAttributes[netType].mType == ConnectivityManager.TYPE_MOBILE) {

            // Do not add/remove DefaultRoute for default mobile connection.
            // This should not affect other default connection types ...
            // We do not want to remove the default route since on a cellular network
            // it is likely we'll get a temporary disconnect only to be connected again
            // DHCP/linux kernel should automatically update default route whenever necessary.
            return;
        }

if (mNetTrackers[netType].getNetworkInfo().isConnected()) {
if (mNetAttributes[netType].isDefault()) {
mNetTrackers[netType].addDefaultRoute();







