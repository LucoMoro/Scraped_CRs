/*Extend ANDROID with CDMA mobile technology support . Feature Complete

This contribution is final functional step of the first release of the CDMA extension of the
Android telephony layers.
It contains changes in the phone related applications, the application
framework telephony packages and in the RIL daemon library space.
The implementation of the CDMA support requires architectural changes in the
telephony package and extensions of the RIL interface.
The application interface (SDK interface) will be extended to provide
CDMA specific features/information to the phone related application and other
applications.
Where ever possible the actual used radio technology is transparent for the
application using mobile connections.

This increment is tested on the Android emulator with a RIL simulator tool and
also tested on a reference HW platform.
The CDMA extension of the telephony stack can be used for Android phones
supporting either CDMA mobile technology only
or world mode including GSM/WCDMA and CDMA.
The following CDMA technologies are considered: IS-95, CDMA2000 1xRTT, CDMA2000
1x EVDO.

This contribution implements the following functionality:

- start up,
- access the CDMA subscription and other information from memory of from the
  card (either SIM, USIM or RUIM),
- register to the network,
- provides registration status to the application for displaying
- be able to handle incoming and outgoing voice calls,
- provide phone and call settings in the settings application
- provide supplementary services in the settings application
- provide supplementary services by in-call menues
- handles TTY and enhance voice privacy
- supports automatic radio technology change for a world mode phone from
  CDMA to GSM/UMTS or vice versa
- send and receive SMS
- configure and receive Broadcast SMS
- receive WAP Push SMS

Signed-off by : Saverio Labella,    saverio.labella@teleca.com
                Sigmar Lingner,     sigmar.lingner@teleca.com*/
//Synthetic comment -- diff --git a/src/com/android/contacts/SpecialCharSequenceMgr.java b/src/com/android/contacts/SpecialCharSequenceMgr.java
//Synthetic comment -- index f776d51..1ad2a6c 100755

//Synthetic comment -- @@ -155,7 +155,7 @@
sc.progressDialog.show();

// run the query.
                handler.startQuery(ADN_QUERY_TOKEN, sc, Uri.parse("content://sim/adn"),
new String[]{ADN_PHONE_NUMBER_COLUMN_NAME}, null, null, null);
return true;
} catch (NumberFormatException ex) {
//Synthetic comment -- @@ -180,17 +180,19 @@

static boolean handleIMEIDisplay(Context context, String input, boolean useSystemWindow) {
if (input.equals(MMI_IMEI_DISPLAY)) {
            int networkType =
                    ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE))
                    .getNetworkType();
            // check for GSM
            if(networkType == TelephonyManager.NETWORK_TYPE_GPRS ||
                    networkType == TelephonyManager.NETWORK_TYPE_EDGE ||
                    networkType == TelephonyManager.NETWORK_TYPE_UMTS ) {
showIMEIPanel(context, useSystemWindow);
return true;
}
}
return false;
}








