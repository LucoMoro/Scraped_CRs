/*Adds documentation to CdmaCellLocation get lat/long methods

Adds better documentation to the getBaseStationLatitude() and
getBaseStationLongitude() methods to indicate the units for latitude
and longitude in CdmaCellLocation. Unlike the Android Location API
methods, latitude and longitude in CdmaCellLocation are not in decimal
degrees but instead in 0.25 seconds due to a more obscure 3GPP standard
underlying the Telephony API.  The current Javadocs on the Android
developer page (http://goo.gl/hvWo6) do not indicate the units for
latitude and longitude in CdmaCellLocation, making it very difficult
for developers to interpret the output of these methods, especially
since 0.25 seconds is not a commonly used unit for latitude and longitude
in application-level code.  This patch adds clear documentation to
these methods so developers can clearly understand the units for
latitude and longitude without having to dig through the Android
platform source code.

Change-Id:I259bfe3b68e3999804877821680fb99a22937651Signed-off-by: Sean Barbeau <sjbarbeau@gmail.com>*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/cdma/CdmaCellLocation.java b/telephony/java/android/telephony/cdma/CdmaCellLocation.java
//Synthetic comment -- index 84db830..b1f400b 100644

//Synthetic comment -- @@ -81,14 +81,26 @@
}

/**
     * @return cdma base station latitude, Integer.MAX_VALUE if unknown
*/
public int getBaseStationLatitude() {
return this.mBaseStationLatitude;
}

/**
     * @return cdma base station longitude, Integer.MAX_VALUE if unknown
*/
public int getBaseStationLongitude() {
return this.mBaseStationLongitude;







