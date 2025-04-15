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
     * Latitude is a decimal number as specified in 3GPP2 C.S0005-A v6.0.
     * (http://www.3gpp2.org/public_html/specs/C.S0005-A_v6.0.pdf)
     * It is represented in units of 0.25 seconds and ranges from -1296000
     * to 1296000, both values inclusive (corresponding to a range of -90
     * to +90 degrees). Integer.MAX_VALUE is considered invalid value.
     *
     * @return cdma base station latitude in units of 0.25 seconds, Integer.MAX_VALUE if unknown
*/
public int getBaseStationLatitude() {
return this.mBaseStationLatitude;
}

/**
     * Longitude is a decimal number as specified in 3GPP2 C.S0005-A v6.0.
     * (http://www.3gpp2.org/public_html/specs/C.S0005-A_v6.0.pdf)
     * It is represented in units of 0.25 seconds and ranges from -2592000
     * to 2592000, both values inclusive (corresponding to a range of -180
     * to +180 degrees). Integer.MAX_VALUE is considered invalid value.
     *
     * @return cdma base station longitude in units of 0.25 seconds, Integer.MAX_VALUE if unknown
*/
public int getBaseStationLongitude() {
return this.mBaseStationLongitude;







