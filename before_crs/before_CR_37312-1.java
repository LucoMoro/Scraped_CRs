/*Adds missing Bluetooth as network type.

The network connection type for Bluetooth is added so
that it is possible to use Bluetooth as Hotspot during
download from the Internet.

Change-Id:Ic32b083068d8316a41f2c31a3575a28163f6c7f3*/
//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadInfo.java b/src/com/android/providers/downloads/DownloadInfo.java
//Synthetic comment -- index c879e7e..93811c9 100644

//Synthetic comment -- @@ -415,6 +415,9 @@
case ConnectivityManager.TYPE_WIFI:
return DownloadManager.Request.NETWORK_WIFI;

default:
return 0;
}







