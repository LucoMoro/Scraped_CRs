/*Add support for MNC=00

This adds support for operators with MNC (Mobile Network Code) zero
to add customized resources. For example, it makes it possible to
add a folder called "/res/values-mnc00/" in an application. This will
cause resources in that folder to be used when MNC is zero.
(There is a total of 14 countries that have an operator with MNC
zero.)

Without this fix, the resource framework gets confused, because MNC 0
is normally used when the MNC is undefined (not set).

Bug: 7170488
Change-Id:Ie209ab04e5c24bfad8509af5e0e0e948fad22796*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/MccTable.java b/src/java/com/android/internal/telephony/MccTable.java
//Synthetic comment -- index cb33521..34432a5 100644

//Synthetic comment -- @@ -168,7 +168,7 @@

/**
* Updates MCC and MNC device configuration information for application retrieving
     * correct version of resources.  If either MCC or MNC is 0, they will be ignored (not set).
* @param context Context to act on.
* @param mccmnc truncated imsi with just the MCC and MNC - MNC assumed to be from 4th to end
*/
//Synthetic comment -- @@ -195,9 +195,7 @@
Configuration config = ActivityManagerNative.getDefault().getConfiguration();
if (mcc != 0) {
config.mcc = mcc;
                }
                if (mnc != 0) {
                    config.mnc = mnc;
}
ActivityManagerNative.getDefault().updateConfiguration(config);
} catch (RemoteException e) {







