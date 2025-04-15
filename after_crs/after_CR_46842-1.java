/*Fix display of button type in Device Creation Dialog. DO NOT MERGE

The combo box always had the values "Hardware" and "Software", but it was
being compared with the id for ButtonType, which was "hard" or "soft".http://code.google.com/p/android/issues/detail?id=39941Change-Id:I5658f7f8bb2eaeacce5661dafb1416af17eeb741*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/ButtonType.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/ButtonType.java
//Synthetic comment -- index 6ab67d4..6776849 100644

//Synthetic comment -- @@ -17,18 +17,26 @@
package com.android.sdklib.devices;

public enum ButtonType {
    HARD("hard", "Hardware"),
    SOFT("soft", "Software");

    private final String mId;
    private final String mDescription;

    /**
     * Construct a {@link ButtonType}.
     *
     * @param id identifier for this button type. Persisted on disk when a user creates a device.
     * @param desc User friendly description
     */
    private ButtonType(String id, String desc) {
        mId = id;
        mDescription = desc;
}

public static ButtonType getEnum(String value) {
for (ButtonType n : values()) {
            if (n.mId.equals(value)) {
return n;
}
}
//Synthetic comment -- @@ -37,6 +45,11 @@

@Override
public String toString() {
        return mId;
}

    public String getDescription() {
        return mDescription;
    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/DeviceCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/DeviceCreationDialog.java
//Synthetic comment -- index 82972cf..0707ddc 100644

//Synthetic comment -- @@ -301,8 +301,8 @@
generateLabel("Buttons:", tooltip, column2);
mButtons = new Combo(column2, SWT.DROP_DOWN | SWT.READ_ONLY);
mButtons.setToolTipText(tooltip);
        mButtons.add(ButtonType.SOFT.getDescription());
        mButtons.add(ButtonType.HARD.getDescription());
mButtons.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
mButtons.select(0);
mButtons.addModifyListener(validator);
//Synthetic comment -- @@ -867,8 +867,9 @@
}
mRam.setText(Long.toString(mHardware.getRam().getSizeAsUnit(Storage.Unit.MiB)));
mRamCombo.select(0);

for (int i = 0; i < mButtons.getItemCount(); i++) {
            if (mButtons.getItem(i).equals(mHardware.getButtonType().getDescription())) {
mButtons.select(i);
break;
}
//Synthetic comment -- @@ -966,7 +967,7 @@
long ram = Long.parseLong(mRam.getText());
Storage.Unit unit = Storage.Unit.getEnum(mRamCombo.getText());
mHardware.setRam(new Storage(ram, unit));
            if (mButtons.getText().equals(ButtonType.HARD.getDescription())) {
mHardware.setButtonType(ButtonType.HARD);
} else {
mHardware.setButtonType(ButtonType.SOFT);







