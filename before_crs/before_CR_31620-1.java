/*Removing static reference from sUnknown

Removing static reference from field sUnknown
allows framework to handle properly resources.
Current implementation holds an instance of
sUnknown, final user sees "Unknown" field on the
IMEI SV [ Menu > About > Status ] even if the
language set is other than english.

Change-Id:I6345f578577e631782c0598bfa44ff4de04c6261*/
//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/Status.java b/src/com/android/settings/deviceinfo/Status.java
//Synthetic comment -- index c315acd..434dfe7 100644

//Synthetic comment -- @@ -115,7 +115,7 @@
private Preference mSignalStrength;
private Preference mUptime;

    private static String sUnknown;

private Preference mBatteryStatus;
private Preference mBatteryLevel;
//Synthetic comment -- @@ -188,9 +188,7 @@
mBatteryStatus = findPreference(KEY_BATTERY_STATUS);

mRes = getResources();
        if (sUnknown == null) {
            sUnknown = mRes.getString(R.string.device_info_default);
        }

mPhone = PhoneFactory.getDefaultPhone();
// Note - missing in zaku build, be careful later...







