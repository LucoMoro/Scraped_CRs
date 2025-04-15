/*Enable "APN roaming protocol" for all platforms

Allows users to change the protocol (the PDP context type) used when
roaming in the same way they already can for when they are in their home
network. This is required in order to make IPv6 and IPv4v6 data roaming
work correctly in 3GPP networks.

See also:http://code.google.com/p/android/issues/detail?id=32631Change-Id:I052842122e5fe96a8779077f0ae9ff4bc83066c5Signed-off-by: Tore Anderson <tore@fud.no>*/




//Synthetic comment -- diff --git a/src/com/android/settings/ApnEditor.java b/src/com/android/settings/ApnEditor.java
//Synthetic comment -- index efde689..f00d30a 100644

//Synthetic comment -- @@ -166,15 +166,7 @@
mProtocol.setOnPreferenceChangeListener(this);

mRoamingProtocol = (ListPreference) findPreference(KEY_ROAMING_PROTOCOL);
        mRoamingProtocol.setOnPreferenceChangeListener(this);

mCarrierEnabled = (CheckBoxPreference) findPreference(KEY_CARRIER_ENABLED);








