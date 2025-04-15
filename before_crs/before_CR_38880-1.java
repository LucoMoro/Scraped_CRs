/*Telephony: Do not display the data icon if data is not connected.

Change-Id:Iba4b6eb424ea953fa2b10af236b9a9c7e66e7a8e*/
//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java b/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java
//Synthetic comment -- index 506dd9a..15c28a1 100644

//Synthetic comment -- @@ -1026,7 +1026,10 @@
mHasMobileDataFeature ? mDataSignalIconId : mWifiIconId;
mContentDescriptionCombinedSignal = mHasMobileDataFeature
? mContentDescriptionDataType : mContentDescriptionWifi;

if ((isCdma() && isCdmaEri()) || mPhone.isNetworkRoaming()) {
mDataTypeIconId = R.drawable.stat_sys_data_connected_roam;
} else {







