/*Allow user control over whether a skin is displayed.

Change-Id:I07c8b468e9a2c900b7cadd04f256dc67128401f7*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index d2ba1b6..6b219d3 100644

//Synthetic comment -- @@ -114,6 +114,12 @@
* If missing, use the {@link #AVD_INI_SKIN_PATH} key instead.
*/
public final static String AVD_INI_SKIN_NAME = "skin.name"; //$NON-NLS-1$
/**
* AVD/config.ini key name representing the path to the sdcard file.
* If missing, the default name "sdcard.img" will be used for the sdcard, if there's such








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 75fe6b5..d78341b 100644

//Synthetic comment -- @@ -88,6 +88,8 @@
private Combo mAbi;

private Button mKeyboard;
private Combo mFrontCamera;
private Combo mBackCamera;

//Synthetic comment -- @@ -253,6 +255,13 @@
mKeyboard.setText("Hardware keyboard present");

label = new Label(parent, SWT.NONE);
label.setText("Front Camera:");
tooltip = "";
label.setToolTipText(tooltip);
//Synthetic comment -- @@ -908,6 +917,10 @@
mKeyboard.getSelection() ?
HardwareProperties.BOOLEAN_YES : HardwareProperties.BOOLEAN_NO);

if (mFrontCamera.isEnabled()) {
hwProps.put(AvdManager.AVD_INI_CAMERA_FRONT,
mFrontCamera.getText().toLowerCase());
//Synthetic comment -- @@ -1041,6 +1054,8 @@

mKeyboard.setSelection(
props.get(HardwareProperties.HW_KEYBOARD) == HardwareProperties.BOOLEAN_YES);

String cameraFront = props.get(AvdManager.AVD_INI_CAMERA_FRONT);
if (cameraFront != null) {







