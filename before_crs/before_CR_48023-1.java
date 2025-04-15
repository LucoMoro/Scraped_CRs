/*AVD edit dialog: Fix equality comparison.

Fixeshttp://code.google.com/p/android/issues/detail?id=41188Change-Id:I568b6c9322ce7ecc2d560999719d79ae68722c06*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index b89841f..89aa8ce 100644

//Synthetic comment -- @@ -1177,9 +1177,11 @@
}

mKeyboard.setSelection(
                    props.get(HardwareProperties.HW_KEYBOARD) == HardwareProperties.BOOLEAN_YES);
mSkin.setSelection(
                    props.get(AvdManager.AVD_INI_SKIN_DYNAMIC) == HardwareProperties.BOOLEAN_YES);

String cameraFront = props.get(AvdManager.AVD_INI_CAMERA_FRONT);
if (cameraFront != null) {







