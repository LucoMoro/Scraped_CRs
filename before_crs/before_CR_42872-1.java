/*launch: Refresh AVD selector dialog during initial setup

While initially setting up the target launch mode (auto/manual/etc),
make sure that mode change listener is called.

Change-Id:Ib2fa9128909b31275efc1487fe0d192cc8dc3b1e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/EmulatorConfigTab.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/EmulatorConfigTab.java
//Synthetic comment -- index e01bfab..f1c61c7 100644

//Synthetic comment -- @@ -219,14 +219,7 @@
SelectionListener targetModeChangeListener = new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                updateLaunchConfigurationDialog();

                boolean auto = mAutoTargetButton.getSelection();
                mPreferredAvdSelector.setEnabled(auto);
                mPreferredAvdLabel.setEnabled(auto);

                boolean all = mAllDevicesTargetButton.getSelection();
                mDeviceTypeCombo.setEnabled(all);
}
};

//Synthetic comment -- @@ -358,6 +351,17 @@
});
}

/* (non-Javadoc)
* @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
*/
//Synthetic comment -- @@ -427,6 +431,8 @@
mManualTargetButton.setSelection(mode == TargetMode.MANUAL);
mAllDevicesTargetButton.setSelection(multipleDevices);

mDeviceTypeCombo.setEnabled(multipleDevices);
if (multipleDevices) {
int index = 0;







