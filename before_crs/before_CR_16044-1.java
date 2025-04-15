/*Fix issue with qualifier combo box not filled up in device mode

device mode is when the qualifier values must match what a device
would report. For instance nodpi should not be selectable.

Change-Id:I902697d72ffad196fcaf41b1e6387cdfcbbc18da*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java
//Synthetic comment -- index fd22298..3cfa627 100644

//Synthetic comment -- @@ -537,10 +537,9 @@
private void fillCombo(Combo combo, ResourceEnum[] resEnums) {
for (ResourceEnum resEnum : resEnums) {
// only add the enum if:
            // - it's not a fake value. Those are never added as they are used for internal purpose
            //   only.
            // - if it's a valid value for device only if mDeviceMode is true.
            if ((mDeviceMode == false || resEnum.isValidValueForDevice() == false) &&
resEnum.isFakeValue() == false) {
combo.add(resEnum.getShortDisplayValue());
}







