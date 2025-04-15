/*AVD: Create New Device fixes.

- Don't crash if a non-integer is set as screen size.

- Display warnings on all missing fields explaining why the
  "create" button is disabled. Don't let successive checks
  overwrite the warnings/errors from the previous check.

- Make ok button larger to accomodate its own text.

Change-Id:I03ddaf67a9e4e07d1f2ff0c4b94b0a8b064000a3*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/DeviceCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/DeviceCreationDialog.java
//Synthetic comment -- index 873a822..2b7025f 100644

//Synthetic comment -- @@ -157,6 +157,11 @@
}
}

validatePage();

return control;
//Synthetic comment -- @@ -431,35 +436,37 @@
public void modifyText(ModifyEvent e) {

if (!mDiagonalLength.getText().isEmpty()) {
                double diagonal = Double.parseDouble(mDiagonalLength.getText());
                double diagonalDp = 160.0 * diagonal;

                // Set the Screen Size
                if (diagonalDp >= 1200) {
                    mSize.select(ScreenSize.getIndex(ScreenSize.getEnum("xlarge")));
                } else if (diagonalDp >= 800) {
                    mSize.select(ScreenSize.getIndex(ScreenSize.getEnum("large")));
                } else if (diagonalDp >= 568) {
                    mSize.select(ScreenSize.getIndex(ScreenSize.getEnum("normal")));
                } else {
                    mSize.select(ScreenSize.getIndex(ScreenSize.getEnum("small")));
                }
                if (!mXDimension.getText().isEmpty() && !mYDimension.getText().isEmpty()) {

                    // Set the density based on which bucket it's closest to
                    double x = Double.parseDouble(mXDimension.getText());
                    double y = Double.parseDouble(mYDimension.getText());
                    double dpi = Math.sqrt(x * x + y * y) / diagonal;
                    double difference = Double.MAX_VALUE;
                    Density bucket = Density.MEDIUM;
                    for (Density d : Density.values()) {
                        if (Math.abs(d.getDpiValue() - dpi) < difference) {
                            difference = Math.abs(d.getDpiValue() - dpi);
                            bucket = d;
                        }
}
                    mDensity.select(Density.getIndex(bucket));
                }
}
}
}
//Synthetic comment -- @@ -575,57 +582,64 @@
}
}

        if (name.isEmpty()) {
valid = false;
}
        if (!validateFloat("Diagonal Length", mDiagonalLength.getText())) {
valid = false;
}
        if (!validateInt("Resolution", mXDimension.getText())) {
valid = false;
}
        if (!validateInt("Resolution", mYDimension.getText())) {
valid = false;
}
        if (mSize.getSelectionIndex() < 0) {
error = "A size bucket must be selected.";
valid = false;
}
        if (mDensity.getSelectionIndex() < 0) {
error = "A screen density bucket must be selected";
valid = false;
}
        if (mRatio.getSelectionIndex() < 0) {
error = "A screen ratio must be selected.";
valid = false;
}
        if (!mNoNav.getSelection() && !mTrackball.getSelection() && !mDpad.getSelection()) {
error = "A mode of hardware navigation, or no navigation, has to be selected.";
valid = false;
}
        if (!validateInt("RAM", mRam.getText())) {
valid = false;
}
        if (mRamCombo.getSelectionIndex() < 0) {
error = "RAM must have a selected unit.";
valid = false;
}
        if (mButtons.getSelectionIndex() < 0) {
error = "A button type must be selected.";
valid = false;
}
        if (mKeyboard.getSelection()) {
            if (!mPortraitKeys.getSelection()
                    && !mPortrait.getSelection()
                    && !mLandscapeKeys.getSelection()
                    && !mLandscape.getSelection()) {
                error = "At least one device state must be enabled.";
                valid = false;
            }
        } else {
            if (!mPortrait.getSelection() && !mLandscape.getSelection()) {
                error = "At least one device state must be enabled";
                valid = false;
}
}
if (mForceCreation.isEnabled() && !mForceCreation.getSelection()) {







