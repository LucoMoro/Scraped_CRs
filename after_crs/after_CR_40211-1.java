/*SystemUI: Fix data activity overlay not being removed when data is disabled

If mobile data connection is disabled while there is active data
activity, the data activity overlay will not be removed and will
be stuck there. This patch fixes that.

Change-Id:I4cc1a14bb2e2d85687752657608e5989adfe9af1*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java b/packages/SystemUI/src/com/android/systemui/statusbar/policy/NetworkController.java
//Synthetic comment -- index 4d22f33..e4795ba 100644

//Synthetic comment -- @@ -968,6 +968,8 @@
combinedActivityIconId = mMobileActivityIconId;
combinedSignalIconId = mDataSignalIconId; // set by updateDataIcon()
mContentDescriptionCombinedSignal = mContentDescriptionDataType;
            } else {
                mMobileActivityIconId = 0;
}
}








