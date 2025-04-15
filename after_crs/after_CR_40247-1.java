/*show 3G icon for CDMA/1xRTT

when config_showMin3G is enabled to true in a CDMA device

Change-Id:I79a4fa200dc406fc7f9f4527165046541961ef69Signed-off-by: Madan Ankapura <mankapur@sta.samsung.com>*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java b/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java
//Synthetic comment -- index 4d22f33..751764c 100644

//Synthetic comment -- @@ -568,18 +568,26 @@
}
break;
case TelephonyManager.NETWORK_TYPE_CDMA:
                    if (!mShowAtLeastThreeGees) {
                        // display 1xRTT for IS95A/B
                        mDataIconList = TelephonyIcons.DATA_1X[mInetCondition];
                        mDataTypeIconId = R.drawable.stat_sys_data_connected_1x;
                        mContentDescriptionDataType = mContext.getString(
                                R.string.accessibility_data_connection_cdma);
                        break;
                    } else {
                        // fall through
                    }
case TelephonyManager.NETWORK_TYPE_1xRTT:
                    if (!mShowAtLeastThreeGees) {
                        mDataIconList = TelephonyIcons.DATA_1X[mInetCondition];
                        mDataTypeIconId = R.drawable.stat_sys_data_connected_1x;
                        mContentDescriptionDataType = mContext.getString(
                                R.string.accessibility_data_connection_cdma);
                        break;
                    } else {
                        // fall through
                    }
case TelephonyManager.NETWORK_TYPE_EVDO_0: //fall through
case TelephonyManager.NETWORK_TYPE_EVDO_A:
case TelephonyManager.NETWORK_TYPE_EVDO_B:







