/*Fix issue with qualifier combo box not filled up in device mode DO NOT MERGE

device mode is when the qualifier values must match what a device
would report. For instance nodpi should not be selectable.

Cherry-picked from master to tools_r7

Change-Id:I22cbd5b3522ae17d4d7f61e95ca20deeea825a79*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java
//Synthetic comment -- index fd22298..21703d0 100644

//Synthetic comment -- @@ -537,10 +537,9 @@
private void fillCombo(Combo combo, ResourceEnum[] resEnums) {
for (ResourceEnum resEnum : resEnums) {
// only add the enum if:
            // device mode is false OR (device mode is true and) it's a valid device value.
            // Also, always ignore fake values.
            if ((mDeviceMode == false || resEnum.isValidValueForDevice()) &&
resEnum.isFakeValue() == false) {
combo.add(resEnum.getShortDisplayValue());
}







