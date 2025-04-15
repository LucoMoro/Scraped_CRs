/*Remove airplane icon when totally disconnected device is not in airplane mode

Linaro Bug #873281
Snowball: Airplane mode enabling/disabling does not work as expectedhttps://bugs.launchpad.net/bugs/873281Change-Id:Ide752f4cc31dfbadde8f11bd1fd9fc408d25c716Signed-off-by: Veli-Matti Anttila <Veli-Matti.Anttila@elektrobit.com>*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java b/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java
//Synthetic comment -- index 506dd9a..87d6519 100644

//Synthetic comment -- @@ -1022,6 +1022,17 @@

combinedLabel = context.getString(R.string.status_bar_settings_signal_meter_disconnected);
// On devices without mobile radios, we want to show the wifi icon

            // Remove airplane icon if not in airplane mode.
            if ((R.drawable.stat_sys_signal_flightmode == mDataSignalIcondId)
                && !mAirplaneMode) {
                mDataSignalIconId = R.drawable.stat_sys_signal_null;
            }
            if ((R.drawable.stat_sys_signal_flightmode == mPhoneSignalIconId)
                && !mAirplaneMode) {
                mPhoneSignalIconId = R.drawable.stat_sys_signal_null;
            }

combinedSignalIconId =
mHasMobileDataFeature ? mDataSignalIconId : mWifiIconId;
mContentDescriptionCombinedSignal = mHasMobileDataFeature







