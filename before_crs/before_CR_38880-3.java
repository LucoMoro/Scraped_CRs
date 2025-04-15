/*Telephony: Do not display the data icon if data is not connected.

When updating the status bar for the bluetooth reverse tether
connected status, even if the mobile data state is in
disconnected state, and it shows the data icon based on the
data network type.
Changes made to check the mobile data connected condition while
updating the data icon.

Change-Id:Iba4b6eb424ea953fa2b10af236b9a9c7e66e7a8e*/
//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java b/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java
//Synthetic comment -- index bbb90c8..949ebfe 100644

//Synthetic comment -- @@ -1142,7 +1142,10 @@
mHasMobileDataFeature ? mDataSignalIconId : mWifiIconId;
mContentDescriptionCombinedSignal = mHasMobileDataFeature
? mContentDescriptionDataType : mContentDescriptionWifi;

mDataTypeIconId = 0;
mQSDataTypeIconId = 0;
if (isCdma()) {







