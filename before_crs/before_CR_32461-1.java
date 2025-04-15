/*Check the sim state in case of CDMA

'no sim' icon on the statusbar is not displayed
in case of CDMA.

Change-Id:I57ad2de2001e653a82261bece312295b292f3404*/
//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java b/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java
//Synthetic comment -- index 135a04c..593b2b1 100644

//Synthetic comment -- @@ -630,8 +630,9 @@
}
} else {
// CDMA case, mDataActivity can be also DATA_ACTIVITY_DORMANT
            if (hasService() && mDataState == TelephonyManager.DATA_CONNECTED) {
                switch (mDataActivity) {
case TelephonyManager.DATA_ACTIVITY_IN:
iconId = mDataIconList[1];
break;
//Synthetic comment -- @@ -645,10 +646,14 @@
default:
iconId = mDataIconList[0];
break;
}
} else {
                iconId = 0;
                visible = false;
}
}








