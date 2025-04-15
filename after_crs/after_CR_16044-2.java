/*Fix issue with qualifier combo box not filled up in device mode

device mode is when the qualifier values must match what a device
would report. For instance nodpi should not be selectable.

Change-Id:I902697d72ffad196fcaf41b1e6387cdfcbbc18da*/




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







