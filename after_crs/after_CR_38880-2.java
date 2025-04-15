/*Telephony: Do not display the data icon if data is not connected.

Change-Id:Iba4b6eb424ea953fa2b10af236b9a9c7e66e7a8e*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java b/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java
//Synthetic comment -- index d94c6b2..f93cdb0 100644

//Synthetic comment -- @@ -1078,7 +1078,10 @@
mHasMobileDataFeature ? mDataSignalIconId : mWifiIconId;
mContentDescriptionCombinedSignal = mHasMobileDataFeature
? mContentDescriptionDataType : mContentDescriptionWifi;
        }

        if (!mDataConnected) {
            Slog.d(TAG, "refreshViews: Data not connected!! Set no data type icon / Roaming");
mDataTypeIconId = 0;
if (isCdma()) {
if (isCdmaEri()) {







