/*Add label to AVD Creation dialog for no emulator images installed

It's pretty easy to end up installing a recent Android platform
without also installing an emulator image such as ARM or x86.

When you create an AVD, and you select that platform, the OK button
is disabled, but it isn't obvious why; there's a Target: label
next to an empty and disabled combo box.

This CL simply sets the text of the combo box to "No system images
installed for this target." (while keeping the combobox disabled) to
hopefully guide the user in the right direction.

Change-Id:I5dcc3e75944440f04e1054be35fe68be41de734f*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index d788ccd..4006ac6 100644

//Synthetic comment -- @@ -71,6 +71,7 @@
private ImageFactory mImageFactory;
private ILogger mSdkLog;
private AvdInfo mAvdInfo;
    private boolean mHaveSystemImage;

// A map from manufacturers to their list of devices.
private Map<String, List<Device>> mDeviceMap;
//Synthetic comment -- @@ -668,7 +669,11 @@
}
}

            mHaveSystemImage = systemImages.length > 0;
            if (!mHaveSystemImage) {
                mAbi.add("No system images installed for this target.");
                mAbi.select(0);
            } else if (systemImages.length == 1) {
mAbi.select(0);
}
}
//Synthetic comment -- @@ -730,7 +735,8 @@
return;
}

        if (mTarget.getSelectionIndex() < 0 ||
                !mHaveSystemImage || mAbi.getSelectionIndex() < 0) {
setPageValid(false, error, warning);
return;
}







