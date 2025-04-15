/*Adds missing Bluetooth as network type.

The network connection type for Bluetooth is added so
that it is possible to use Bluetooth as Hotspot during
download from the Internet.

Change-Id:If8e8a3f69821beef742bc91d7a292a091861b48b*/




//Synthetic comment -- diff --git a/core/java/android/app/DownloadManager.java b/core/java/android/app/DownloadManager.java
//Synthetic comment -- index 17700f9..0b1c524 100644

//Synthetic comment -- @@ -344,6 +344,13 @@
*/
public static final int NETWORK_WIFI = 1 << 1;

        /**
         * Bit flag for {@link #setAllowedNetworkTypes} corresponding to
         * {@link ConnectivityManager#TYPE_BLUETOOTH}.
         * @hide
         */
        public static final int NETWORK_BLUETOOTH = 1 << 2;

private Uri mUri;
private Uri mDestinationUri;
private List<Pair<String, String>> mRequestHeaders = new ArrayList<Pair<String, String>>();







