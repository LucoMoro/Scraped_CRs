/*Enable "APN roaming protocol" for all platforms

Allows users to change the protocol (the PDP context type) used when
roaming in the same way they already can for when they are in their home
network. This is required in order to make IPv6 and IPv4v6 data roaming
work correctly in 3GPP networks.

See also:http://code.google.com/p/android/issues/detail?id=32631Change-Id:I052842122e5fe96a8779077f0ae9ff4bc83066c5Signed-off-by: Tore Anderson <tore@fud.no>*/
//Synthetic comment -- diff --git a/src/com/android/settings/ApnEditor.java b/src/com/android/settings/ApnEditor.java
//Synthetic comment -- index bca521b..79b6d15 100644

//Synthetic comment -- @@ -167,15 +167,7 @@
mProtocol.setOnPreferenceChangeListener(this);

mRoamingProtocol = (ListPreference) findPreference(KEY_ROAMING_PROTOCOL);
        // Only enable this on CDMA phones for now, since it may cause problems on other phone
        // types.  (This screen is not normally accessible on CDMA phones, but is useful for
        // testing.)
        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        if (tm.getCurrentPhoneType() == PhoneConstants.PHONE_TYPE_CDMA) {
            mRoamingProtocol.setOnPreferenceChangeListener(this);
        } else {
            getPreferenceScreen().removePreference(mRoamingProtocol);
        }

mCarrierEnabled = (CheckBoxPreference) findPreference(KEY_CARRIER_ENABLED);








