/*Adds utility method to convert 0.25 secs to decimal degrees

This patch adds a utility method that converts latitude and longitude
in quarter seconds units to decimal degrees units.  The Telephony API
returns CDMA base station latitude and longitude in quarter seconds
due to a 3GPP telecom standard, while the Android Location API, and
the vast majority of application-level code, uses decimal degrees.

For example, to measure the distance from the user's current location
to the base station using the Location API Location.distanceBetween()
method (http://goo.gl/YjO8O), the base station lat and long would need
to be converted to decimal degrees first.

Since most application developers will likely never use lat/long information
in quarter seconds units, and instead will need this information in decimal
degrees, this utility method will frequently be used by anyone querying
base station location data from CdmaCellLocation.

Sample values to test conversion:

0.25 seconds: lat = 399491, long = -1189145
is equivalent to
decimal degrees: lat = 27.742430555555554, long = -82.57951388888888

Change-Id:If03e741f5035a37519f50d4fb2fb3e3eef2505daSigned-off-by: Sean Barbeau <sjbarbeau@gmail.com>*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/cdma/CdmaCellLocation.java b/telephony/java/android/telephony/cdma/CdmaCellLocation.java
//Synthetic comment -- index 84db830..0eaeebc 100644

//Synthetic comment -- @@ -215,6 +215,22 @@
this.mNetworkId == -1);
}


}








