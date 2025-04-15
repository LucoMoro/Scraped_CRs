/*Restore widget listeners in new project page

The widget listeners which update the wizard state when you change the
min sdk version, or the target sdk version, went missing in changeset
4ef523b33a392e6c9ca40, probably because a cut & paste operation of a
widget in WindowBuilder (to move it in the hierarchy) also sometimes
removes associated event handler code in other methods (!).

This CL restores this code.

Change-Id:I2501d5e223bec88ebbe74d6fafcc7d7588d6204e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index 2a6e784..e698ac1 100644

//Synthetic comment -- @@ -544,6 +544,51 @@
}

Object source = e.getSource();
        if (source == mMinSdkCombo) {
            mValues.minSdk = getSelectedMinSdk();
            // If higher than build target, adjust build target
            // TODO: implement

            Integer minSdk = mMinNameToApi.get(mValues.minSdk);
            if (minSdk == null) {
                try {
                    minSdk = Integer.parseInt(mValues.minSdk);
                } catch (NumberFormatException nufe) {
                    minSdk = 1;
                }
            }
            mValues.iconState.minSdk = minSdk.intValue();
            mValues.minSdkLevel = minSdk.intValue();
        } else if (source == mBuildSdkCombo) {
            mValues.target = getSelectedBuildTarget();

            // If lower than min sdk target, adjust min sdk target
            if (mValues.target.getVersion().isPreview()) {
                mValues.minSdk = mValues.target.getVersion().getCodename();
                try {
                    mIgnore = true;
                    mMinSdkCombo.setText(mValues.minSdk);
                } finally {
                    mIgnore = false;
                }
            } else {
                String minSdk = mValues.minSdk;
                int buildApiLevel = mValues.target.getVersion().getApiLevel();
                if (minSdk != null && !minSdk.isEmpty()
                        && Character.isDigit(minSdk.charAt(0))
                        && buildApiLevel < Integer.parseInt(minSdk)) {
                    mValues.minSdk = Integer.toString(buildApiLevel);
                    try {
                        mIgnore = true;
                        setSelectedMinSdk(buildApiLevel);
                    } finally {
                        mIgnore = false;
                    }
                }
            }
        } else if (source == mTargetSdkCombo) {
            mValues.targetSdkLevel = getSelectedTargetSdk();
        }

validatePage();
}
//Synthetic comment -- @@ -560,6 +605,11 @@
return Integer.toString(mMinSdkCombo.getSelectionIndex() + 1);
}

    private int getSelectedTargetSdk() {
        // +1: First API level (at index 0) is 1
        return mTargetSdkCombo.getSelectionIndex() + 1;
    }

private void setSelectedMinSdk(int api) {
mMinSdkCombo.select(api - 1); // -1: First API level (at index 0) is 1
}
//Synthetic comment -- @@ -720,8 +770,8 @@
}
if (status == null || status.getSeverity() != IStatus.ERROR) {
if (mValues.targetSdkLevel < mValues.minSdkLevel) {
                            status = new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                                "The target SDK version should be at least as high as the minimum SDK version");
}
}
}







