/*Format change at "Mobile network type" value

Remove "network type Number" from "mobile network type" value
at "About Phone > Status".

For example value "UNKNOWN:0" became "UNKNOWN"
(if value is "UNKNOWN", the word is localized)

The number ":0" did not add much value to the user.

Change-Id:Iee29fa2ab42f2d6eab5d9dbd6f647087ee382648*/
//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/Status.java b/src/com/android/settings/deviceinfo/Status.java
//Synthetic comment -- index 8017af3..1b09f69 100644

//Synthetic comment -- @@ -332,8 +332,11 @@

private void updateNetworkType() {
// Whether EDGE, UMTS, etc...
        setSummaryText(KEY_NETWORK_TYPE, mTelephonyManager.getNetworkTypeName() +
                ":" + mTelephonyManager.getNetworkType());
}

private void updateDataState() {







