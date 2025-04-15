/*Fix potential NPE in AvdCreationDialog

Change-Id:Ic71e0242b34979f621e291c808b6a0ee35b711ff*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 8955c52..b545dbd 100644

//Synthetic comment -- @@ -709,7 +709,7 @@
}

// select the abi type
        if (target.getAbiList().length > 0) {
mAbiTypeCombo.setEnabled(target.getAbiList().length > 1);
String abiType = AvdInfo.getPrettyAbiType(mEditAvdInfo.getAbiType());
int n = mAbiTypeCombo.getItemCount();
//Synthetic comment -- @@ -763,7 +763,6 @@

sdcard = props.get(AvdManager.AVD_INI_SDCARD_SIZE);
if (sdcard != null && sdcard.length() > 0) {

String[] values = new String[2];
long sdcardSize = AvdManager.parseSdcardSize(sdcard, values);








