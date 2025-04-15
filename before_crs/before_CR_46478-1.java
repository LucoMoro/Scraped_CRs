/*Fix display of button type in Device Creation Dialog

The combo box always had the values "Hardware" and "Software", but it was
being compared with the id for ButtonType, which was "hard" or "soft".http://code.google.com/p/android/issues/detail?id=39941Change-Id:I3b7234a7ddf46d11f5c065a86c42c8cf2c73da12*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/ButtonType.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/ButtonType.java
//Synthetic comment -- index 6ab67d4..6776849 100644

//Synthetic comment -- @@ -17,18 +17,26 @@
package com.android.sdklib.devices;

public enum ButtonType {
    HARD("hard"),
    SOFT("soft");

    private final String mValue;

    private ButtonType(String value) {
        mValue = value;
}

public static ButtonType getEnum(String value) {
for (ButtonType n : values()) {
            if (n.mValue.equals(value)) {
return n;
}
}
//Synthetic comment -- @@ -37,6 +45,11 @@

@Override
public String toString() {
        return mValue;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/DeviceCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/DeviceCreationDialog.java
//Synthetic comment -- index 82972cf..0707ddc 100644

//Synthetic comment -- @@ -301,8 +301,8 @@
generateLabel("Buttons:", tooltip, column2);
mButtons = new Combo(column2, SWT.DROP_DOWN | SWT.READ_ONLY);
mButtons.setToolTipText(tooltip);
        mButtons.add("Software");
        mButtons.add("Hardware");
mButtons.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
mButtons.select(0);
mButtons.addModifyListener(validator);
//Synthetic comment -- @@ -867,8 +867,9 @@
}
mRam.setText(Long.toString(mHardware.getRam().getSizeAsUnit(Storage.Unit.MiB)));
mRamCombo.select(0);
for (int i = 0; i < mButtons.getItemCount(); i++) {
            if (mButtons.getItem(i).equals(mHardware.getButtonType().toString())) {
mButtons.select(i);
break;
}
//Synthetic comment -- @@ -966,7 +967,7 @@
long ram = Long.parseLong(mRam.getText());
Storage.Unit unit = Storage.Unit.getEnum(mRamCombo.getText());
mHardware.setRam(new Storage(ram, unit));
            if (mButtons.getText().equals("Hardware")) {
mHardware.setButtonType(ButtonType.HARD);
} else {
mHardware.setButtonType(ButtonType.SOFT);







